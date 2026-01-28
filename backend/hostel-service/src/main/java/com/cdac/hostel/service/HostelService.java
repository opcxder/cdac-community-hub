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
 * All hostels start in PENDING status and require admin approval before being visible to users.
 */
@Service
public class HostelService {

    private static final Logger logger = LoggerFactory.getLogger(HostelService.class);

    @Autowired
    private HostelRepository hostelRepository;

    @Autowired
    private AuthServiceClient authClient;

    /**
     * Creates a new hostel submission.
     * Validates that the submitting user exists before creating the hostel.
     * New hostels are created with PENDING status and require admin approval.
     *
     * @param hostel The hostel entity to create
     * @param userId The ID of the user submitting the hostel
     * @return The created hostel entity
     * @throws RuntimeException if user does not exist
     */
    public Hostel createHostel(Hostel hostel, Long userId) {
        logger.info("Creating new hostel submission: name={}, submittedBy={}", 
                    hostel.getHostelName(), userId);
        
        // Validate user exists via Auth Service
        if (!authClient.userExists(userId)) {
            logger.error("Hostel creation failed - User not found: userId={}", userId);
            throw new ResourceNotFoundException("User not found");
        }
        
        // Set submission metadata
        hostel.setSubmittedByUserId(userId);
        hostel.setStatus(HostelStatus.PENDING);
        
        Hostel savedHostel = hostelRepository.save(hostel);
        logger.info("Hostel created successfully: hostelId={}, name={}, status={}", 
                    savedHostel.getHostelId(), savedHostel.getHostelName(), savedHostel.getStatus());
        
        return savedHostel;
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
     * Used by admin interface to review submissions.
     *
     * @return List of pending hostels
     */
    public List<Hostel> getPendingHostels() {
        logger.debug("Fetching all pending hostels for admin review");
        List<Hostel> hostels = hostelRepository.findByStatus(HostelStatus.PENDING);
        logger.info("Retrieved {} pending hostels", hostels.size());
        return hostels;
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
     * @param reason Optional reason for rejection
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

        hostel.setStatus(HostelStatus.REJECTED);
        hostel.setRejectionReason(reason);
        
        Hostel rejectedHostel = hostelRepository.save(hostel);
        logger.info("Hostel rejected successfully: hostelId={}, name={}", 
                    rejectedHostel.getHostelId(), rejectedHostel.getHostelName());
        
        return rejectedHostel;
    }
}

