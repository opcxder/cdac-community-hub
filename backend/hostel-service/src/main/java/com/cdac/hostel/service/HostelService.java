package com.cdac.hostel.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.hostel.client.AuthServiceClient;
import com.cdac.hostel.model.Hostel;
import com.cdac.hostel.model.HostelStatus;
import com.cdac.hostel.repository.HostelRepository;
import com.cdac.hostel.exception.ResourceNotFoundException;

/**
 * Service layer for hostel management operations.
 * Handles hostel creation, retrieval, and admin approval/rejection workflows.
 * All hostels start in PENDING status and require admin approval before being
 * visible to users.
 */
@Service
public class HostelService {

    private static final Logger logger = LoggerFactory.getLogger(HostelService.class);

    @Autowired
    private HostelRepository hostelRepository;

    @Autowired
    private AuthServiceClient authClient;

    @Autowired
    private ImageService imageService;

    @Autowired
    private com.cdac.hostel.repository.HostelRoomTypeRepository roomTypeRepository;

    @Autowired
    private com.cdac.hostel.repository.ImageRepository imageRepository;

    /**
     * Creates a new hostel submission from HostelRequest DTO.
     * Maps frontend field names to backend entity fields.
     * Handles amenities as booleans and room types as separate records.
     *
     * @param request The hostel request DTO from frontend
     * @param userId  The ID of the user submitting the hostel
     * @return The created hostel entity
     * @throws RuntimeException if user does not exist
     */
    @org.springframework.transaction.annotation.Transactional
    public Hostel createHostel(com.cdac.hostel.dto.HostelRequest request, Long userId) {
        logger.info("Creating new hostel submission: name={}, submittedBy={}",
                request.getHostelName(), userId);

        // Validate user exists via Auth Service
        if (!authClient.userExists(userId)) {
            logger.error("Hostel creation failed - User not found: userId={}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        // Create hostel entity and map fields
        Hostel hostel = new Hostel();
        hostel.setHostelName(request.getHostelName());
        hostel.setDescription(request.getDescription());
        hostel.setAddress(request.getAddress());
        hostel.setCity(request.getCity());
        hostel.setLocality(request.getLocality());
        hostel.setLandmark(request.getLandmark());
        hostel.setMapLocation(request.getMapLocation());

        // Map distanceFromCDAC (frontend) to distanceFromCdac (backend)
        hostel.setDistanceFromCdac(request.getDistanceFromCDAC());

        hostel.setMonthlyRentMin(request.getMonthlyRentMin());
        hostel.setMonthlyRentMax(request.getMonthlyRentMax());
        hostel.setContactPersonName(request.getContactPersonName());

        // Map contactPhone (frontend) to contactPersonPhone (backend)
        hostel.setContactPersonPhone(request.getContactPhone());

        // Map facilities array to individual boolean fields
        if (request.getFacilities() != null) {
            hostel.setHasWifi(request.getFacilities().contains("wifi"));
            hostel.setHasAc(request.getFacilities().contains("ac"));
            hostel.setHasMess(request.getFacilities().contains("mess"));
            hostel.setHasLaundry(request.getFacilities().contains("laundry"));
        } else {
            // Set to false if not provided
            hostel.setHasWifi(false);
            hostel.setHasAc(false);
            hostel.setHasMess(false);
            hostel.setHasLaundry(false);
        }

        // Set submission metadata
        hostel.setSubmittedByUserId(userId);
        hostel.setStatus(HostelStatus.PENDING);

        Hostel savedHostel = hostelRepository.save(hostel);

        // Save room types as separate records
        if (request.getRoomTypes() != null && !request.getRoomTypes().isEmpty()) {
            for (String roomType : request.getRoomTypes()) {
                Integer capacity = mapRoomTypeToCapacity(roomType);
                if (capacity != null) {
                    com.cdac.hostel.model.HostelRoomType roomTypeEntity = new com.cdac.hostel.model.HostelRoomType(
                            savedHostel.getHostelId(), capacity);
                    roomTypeRepository.save(roomTypeEntity);
                    logger.debug("Added room type: {} (capacity: {}) for hostel: {}",
                            roomType, capacity, savedHostel.getHostelId());
                }
            }
        }

        logger.info(
                "Hostel created successfully: hostelId={}, name={}, status={}, amenities=[wifi:{}, ac:{}, mess:{}, laundry:{}]",
                savedHostel.getHostelId(), savedHostel.getHostelName(), savedHostel.getStatus(),
                savedHostel.getHasWifi(), savedHostel.getHasAc(), savedHostel.getHasMess(),
                savedHostel.getHasLaundry());

        return savedHostel;
    }

    /**
     * Maps room type string to capacity integer
     */
    private Integer mapRoomTypeToCapacity(String roomType) {
        if (roomType == null)
            return null;

        switch (roomType.toLowerCase()) {
            case "single":
                return 1;
            case "double":
                return 2;
            case "triple":
                return 3;
            case "quad":
                return 4;
            default:
                logger.warn("Unknown room type: {}", roomType);
                return null;
        }
    }

    /**
     * Retrieves all approved hostels visible to public users.
     *
     * @return List of approved hostels
     */
    public List<Hostel> getApprovedHostels() {
        logger.debug("Fetching all approved hostels");
        List<Hostel> hostels = hostelRepository.findByStatus(HostelStatus.APPROVED);
        logger.info("Retrieved {} approved hostels", hostels.size());
        return hostels;
    }

    /**
     * Retrieves all pending hostels awaiting admin approval.
     * Returns HostelDTO with images and room types included.
     * Used by admin interface to review submissions.
     *
     * @return List of pending hostels with complete data
     */
    public List<com.cdac.hostel.dto.HostelDTO> getPendingHostels() {
        logger.debug("Fetching all pending hostels for admin review");
        List<Hostel> hostels = hostelRepository.findByStatus(HostelStatus.PENDING);
        logger.info("Retrieved {} pending hostels", hostels.size());

        return hostels.stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Maps Hostel entity to HostelDTO including images and room types.
     */
    private com.cdac.hostel.dto.HostelDTO mapToDTO(Hostel hostel) {
        com.cdac.hostel.dto.HostelDTO dto = new com.cdac.hostel.dto.HostelDTO();

        // Basic fields
        dto.setHostelId(hostel.getHostelId());
        dto.setHostelName(hostel.getHostelName());
        dto.setDescription(hostel.getDescription());
        dto.setAddress(hostel.getAddress());
        dto.setCity(hostel.getCity());
        dto.setLocality(hostel.getLocality());
        dto.setLandmark(hostel.getLandmark());
        dto.setMapLocation(hostel.getMapLocation());
        dto.setDistanceFromCdac(hostel.getDistanceFromCdac());
        dto.setMonthlyRentMin(hostel.getMonthlyRentMin());
        dto.setMonthlyRentMax(hostel.getMonthlyRentMax());

        // Amenities
        dto.setHasWifi(hostel.getHasWifi());
        dto.setHasAc(hostel.getHasAc());
        dto.setHasMess(hostel.getHasMess());
        dto.setHasLaundry(hostel.getHasLaundry());

        // Contact
        dto.setContactPersonName(hostel.getContactPersonName());
        dto.setContactPersonPhone(hostel.getContactPersonPhone());

        // Status
        dto.setSubmittedByUserId(hostel.getSubmittedByUserId());
        dto.setStatus(hostel.getStatus().toString());
        dto.setRejectionReason(hostel.getRejectionReason());

        // Fetch images
        List<String> imageUrls = imageRepository.findByHostelIdOrderByDisplayOrderAsc(hostel.getHostelId())
                .stream()
                .map(img -> img.getImageUrl())
                .collect(java.util.stream.Collectors.toList());
        dto.setImageUrls(imageUrls);

        // Fetch room types
        List<Integer> roomCapacities = roomTypeRepository.findByHostelId(hostel.getHostelId())
                .stream()
                .map(rt -> rt.getCapacity())
                .collect(java.util.stream.Collectors.toList());
        dto.setRoomCapacities(roomCapacities);

        logger.debug("Mapped hostel {} with {} images and {} room types",
                hostel.getHostelId(), imageUrls.size(), roomCapacities.size());

        return dto;
    }

    /**
     * Approves a pending hostel, making it visible to public users.
     * Sets the approval timestamp and changes status to APPROVED.
     *
     * @param hostelId The ID of the hostel to approve
     * @return The approved hostel entity
     * @throws RuntimeException if hostel not found
     */
    public Hostel approveHostel(Long hostelId) {
        logger.info("Approving hostel: hostelId={}", hostelId);

        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> {
                    logger.error("Hostel approval failed - Hostel not found: hostelId={}", hostelId);
                    throw new ResourceNotFoundException("Hostel", hostelId);
                });

        hostel.setStatus(HostelStatus.APPROVED);
        hostel.setApprovedAt(new Timestamp(System.currentTimeMillis()));

        Hostel approvedHostel = hostelRepository.save(hostel);
        logger.info("Hostel approved successfully: hostelId={}, name={}",
                approvedHostel.getHostelId(), approvedHostel.getHostelName());

        return approvedHostel;
    }

    /**
     * Rejects a pending hostel with an optional reason.
     * Rejected hostels are not visible to public users.
     *
     * @param hostelId The ID of the hostel to reject
     * @param reason   Optional reason for rejection
     * @return The rejected hostel entity
     * @throws RuntimeException if hostel not found
     */
    public Hostel rejectHostel(Long hostelId, String reason) {
        logger.info("Rejecting hostel: hostelId={}, reason={}", hostelId, reason);

        Hostel hostel = hostelRepository.findById(hostelId)
                .orElseThrow(() -> {
                    logger.error("Hostel rejection failed - Hostel not found: hostelId={}", hostelId);
                    throw new ResourceNotFoundException("Hostel", hostelId);

                });

        // Delete all associated images from Cloudinary and database
        try {
            imageService.deleteAllImagesForHostel(hostelId);
            logger.info("Deleted all images for rejected hostel: hostelId={}", hostelId);
        } catch (Exception e) {
            logger.error("Failed to delete images for hostel: hostelId={}, error={}",
                    hostelId, e.getMessage());
            // Continue with rejection even if image deletion fails
        }

        hostel.setStatus(HostelStatus.REJECTED);
        hostel.setRejectionReason(reason);

        Hostel rejectedHostel = hostelRepository.save(hostel);
        logger.info("Hostel rejected successfully: hostelId={}, name={}",
                rejectedHostel.getHostelId(), rejectedHostel.getHostelName());

        return rejectedHostel;
    }
}
