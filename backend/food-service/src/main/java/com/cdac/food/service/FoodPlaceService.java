package com.cdac.food.service;

import com.cdac.food.dto.FoodPlaceDTO;
import com.cdac.food.dto.FoodPlaceRequest;
import com.cdac.food.dto.UserDTO;
import com.cdac.food.exception.ResourceNotFoundException;
import com.cdac.food.exception.UserNotFoundException;
import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodCategory;
import com.cdac.food.model.FoodPlace;
import com.cdac.food.model.FoodPlaceCategory;
import com.cdac.food.model.FoodPlaceImage;
import com.cdac.food.model.PriceRange;
import com.cdac.food.repository.CategoryRepository;
import com.cdac.food.repository.FoodPlaceCategoryRepository;
import com.cdac.food.repository.FoodPlaceRepository;
import com.cdac.food.repository.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing food places.
 * Handles food place creation, approval workflow, search, and retrieval.
 */
@Service
public class FoodPlaceService {
    
    private static final Logger logger = LoggerFactory.getLogger(FoodPlaceService.class);
    
    private final FoodPlaceRepository foodPlaceRepository;
    private final CategoryRepository categoryRepository;
    private final FoodPlaceCategoryRepository foodPlaceCategoryRepository;
    private final RatingRepository ratingRepository;
    private final AuthServiceClient authServiceClient;
    
    public FoodPlaceService(FoodPlaceRepository foodPlaceRepository,
                           CategoryRepository categoryRepository,
                           FoodPlaceCategoryRepository foodPlaceCategoryRepository,
                           RatingRepository ratingRepository,
                           AuthServiceClient authServiceClient) {
        this.foodPlaceRepository = foodPlaceRepository;
        this.categoryRepository = categoryRepository;
        this.foodPlaceCategoryRepository = foodPlaceCategoryRepository;
        this.ratingRepository = ratingRepository;
        this.authServiceClient = authServiceClient;
    }
    
    /**
     * Create a new food place.
     * Food place is created with PENDING status and requires admin approval.
     * 
     * @param request the food place creation request
     * @return FoodPlaceDTO of the created food place
     * @throws RuntimeException if user does not exist
     * @throws RuntimeException if category does not exist
     */
    @Transactional
    public FoodPlaceDTO createFoodPlace(FoodPlaceRequest request) {
        logger.info("Creating food place: name={}, userId={}", 
                   request.getPlaceName(), request.getSubmittedByUserId());
        
        // Validate user exists
        if (!authServiceClient.userExists(request.getSubmittedByUserId())) {
            logger.error("Food place creation failed - user not found: userId={}", 
                        request.getSubmittedByUserId());
            throw new UserNotFoundException("User not found: " + request.getSubmittedByUserId());
        }
        
        // Create food place entity
        FoodPlace foodPlace = new FoodPlace();
        foodPlace.setPlaceName(request.getPlaceName());
        foodPlace.setDescription(request.getDescription());
        foodPlace.setAddress(request.getAddress());
        foodPlace.setCity(request.getCity());
        foodPlace.setLocality(request.getLocality());
        foodPlace.setLandmark(request.getLandmark());
        foodPlace.setMapLocation(request.getMapLocation());
        foodPlace.setContactInfo(request.getContactInfo());
        foodPlace.setPriceRange(request.getPriceRange());
        foodPlace.setBestForCategoryId(request.getBestForCategoryId());
        foodPlace.setSubmittedByUserId(request.getSubmittedByUserId());
        foodPlace.setStatus(ApprovalStatus.PENDING);
        foodPlace.setCreatedAt(LocalDateTime.now());
        
        // Save food place first to get ID
        FoodPlace savedPlace = foodPlaceRepository.save(foodPlace);
        
        logger.debug("Food place entity saved: placeId={}", savedPlace.getPlaceId());
        
        // Handle category associations
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            logger.debug("Adding {} categories to food place", request.getCategoryIds().size());
            
            for (Long categoryId : request.getCategoryIds()) {
                FoodCategory category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> {
                            logger.error("Category not found: categoryId={}", categoryId);
                            return new ResourceNotFoundException("Category not found: " + categoryId);
                        });
                
                FoodPlaceCategory fpc = new FoodPlaceCategory(savedPlace, category);
                savedPlace.getPlaceCategories().add(fpc);
            }
            
            foodPlaceRepository.save(savedPlace);
        }
        
        logger.info("Food place created successfully: placeId={}, name={}, status={}", 
                   savedPlace.getPlaceId(), savedPlace.getPlaceName(), savedPlace.getStatus());
        
        return mapToDTO(savedPlace);
    }
    
    /**
     * Get all approved food places (public API).
     * Only returns food places with APPROVED status.
     * 
     * @return list of approved food places
     */
    public List<FoodPlaceDTO> getAllApprovedFoodPlaces() {
        logger.debug("Fetching all approved food places");
        
        List<FoodPlace> places = foodPlaceRepository.findByStatus(ApprovalStatus.APPROVED);
        
        logger.debug("Found {} approved food places", places.size());
        
        return places.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all food places with pagination (public API).
     * Only returns approved food places.
     * 
     * @param pageable pagination information
     * @return page of approved food places
     */
    public Page<FoodPlaceDTO> getAllApprovedFoodPlaces(Pageable pageable) {
        logger.debug("Fetching approved food places with pagination: page={}, size={}", 
                    pageable.getPageNumber(), pageable.getPageSize());
        
        Page<FoodPlace> places = foodPlaceRepository.findByStatus(ApprovalStatus.APPROVED, pageable);
        
        logger.debug("Found {} approved food places (page {} of {})", 
                    places.getNumberOfElements(), 
                    places.getNumber() + 1, 
                    places.getTotalPages());
        
        return places.map(this::mapToDTO);
    }
    
    /**
     * Get all pending food places awaiting admin approval.
     * 
     * @return list of pending food places
     */
    public List<FoodPlaceDTO> getPendingPlaces() {
        logger.debug("Fetching pending food places for admin approval");
        
        List<FoodPlace> places = foodPlaceRepository.findByStatus(ApprovalStatus.PENDING);
        
        logger.info("Found {} pending food places", places.size());
        
        return places.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get a food place by ID.
     * 
     * @param placeId the ID of the food place
     * @return FoodPlaceDTO
     * @throws RuntimeException if food place not found
     */
    public FoodPlaceDTO getFoodPlaceById(Long placeId) {
        logger.debug("Fetching food place by ID: {}", placeId);
        
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Food place not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });
        
        return mapToDTO(place);
    }
    
    /**
     * Search food places with multiple filters and pagination.
     * Only returns approved food places.
     * 
     * @param placeName search term for place name (optional)
     * @param priceRange price range filter (optional)
     * @param city city filter (optional)
     * @param pageable pagination information
     * @return page of matching food places
     */
    public Page<FoodPlaceDTO> searchFoodPlaces(String placeName, 
                                               PriceRange priceRange, 
                                               String city, 
                                               Pageable pageable) {
        logger.debug("Searching food places: name={}, priceRange={}, city={}, page={}", 
                    placeName, priceRange, city, pageable.getPageNumber());
        
        Page<FoodPlace> places = foodPlaceRepository.searchFoodPlaces(
            placeName, 
            priceRange, 
            city, 
            ApprovalStatus.APPROVED, 
            pageable
        );
        
        logger.debug("Search found {} results (page {} of {})", 
                    places.getNumberOfElements(), 
                    places.getNumber() + 1, 
                    places.getTotalPages());
        
        return places.map(this::mapToDTO);
    }
    
    /**
     * Search food places with filters and pagination (document specification).
     * 
     * @param search search term for place name (optional)
     * @param priceRange price range filter as string (optional)
     * @param categoryId category filter (optional)
     * @param pageable pagination information
     * @return page of matching food places
     */
    public Page<FoodPlaceDTO> searchPlaces(String search, 
                                          String priceRange, 
                                          Long categoryId, 
                                          Pageable pageable) {
        logger.info("Searching food places: search={}, priceRange={}, categoryId={}, page={}", 
                   search, priceRange, categoryId, pageable.getPageNumber());
        
        // Convert string priceRange to enum
        PriceRange priceRangeEnum = null;
        if (priceRange != null && !priceRange.trim().isEmpty()) {
            try {
                priceRangeEnum = PriceRange.valueOf(priceRange.toUpperCase().replace("-", "_"));
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid price range: {}", priceRange);
            }
        }
        
        // For now, use the existing search method (categoryId filter can be added later)
        Page<FoodPlace> places = foodPlaceRepository.searchFoodPlaces(
            search, 
            priceRangeEnum, 
            null, // city parameter - not used in this specification
            ApprovalStatus.APPROVED, 
            pageable
        );
        
        logger.debug("Search found {} results (page {} of {})", 
                    places.getNumberOfElements(), 
                    places.getNumber() + 1, 
                    places.getTotalPages());
        
        return places.map(this::mapToDTO);
    }
    
    /**
     * Approve a pending food place.
     * Sets status to APPROVED and records approval timestamp.
     * 
     * @param placeId the ID of the food place to approve
     * @return FoodPlaceDTO of the approved food place
     * @throws RuntimeException if food place not found
     */
    @Transactional
    public FoodPlaceDTO approvePlace(Long placeId) {
        logger.info("Approving food place: placeId={}", placeId);
        
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Food place approval failed - not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });
        
        // Update status and approval timestamp
        place.setStatus(ApprovalStatus.APPROVED);
        place.setApprovedAt(LocalDateTime.now());
        
        FoodPlace saved = foodPlaceRepository.save(place);
        
        logger.info("Food place approved successfully: placeId={}, name={}", 
                   saved.getPlaceId(), saved.getPlaceName());
        
        return mapToDTO(saved);
    }
    
    /**
     * Reject a pending food place.
     * Sets status to REJECTED and records rejection reason.
     * 
     * @param placeId the ID of the food place to reject
     * @param reason the reason for rejection
     * @return FoodPlaceDTO of the rejected food place
     * @throws RuntimeException if food place not found
     */
    @Transactional
    public FoodPlaceDTO rejectPlace(Long placeId, String reason) {
        logger.info("Rejecting food place: placeId={}, reason={}", placeId, reason);
        
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Food place rejection failed - not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });
        
        // Update status and rejection reason
        place.setStatus(ApprovalStatus.REJECTED);
        place.setRejectionReason(reason);
        
        FoodPlace saved = foodPlaceRepository.save(place);
        
        logger.info("Food place rejected successfully: placeId={}, name={}", 
                   saved.getPlaceId(), saved.getPlaceName());
        
        return mapToDTO(saved);
    }
    
    /**
     * Map FoodPlace entity to FoodPlaceDTO.
     * Includes average rating calculation and category names.
     * 
     * @param place the FoodPlace entity
     * @return FoodPlaceDTO
     */
    private FoodPlaceDTO mapToDTO(FoodPlace place) {
        FoodPlaceDTO dto = new FoodPlaceDTO();
        dto.setPlaceId(place.getPlaceId());
        dto.setPlaceName(place.getPlaceName());
        dto.setDescription(place.getDescription());
        dto.setAddress(place.getAddress());
        dto.setCity(place.getCity());
        dto.setLocality(place.getLocality());
        dto.setLandmark(place.getLandmark());
        dto.setMapLocation(place.getMapLocation());
        dto.setContactInfo(place.getContactInfo());
        dto.setPriceRange(place.getPriceRange());
        dto.setBestForCategoryId(place.getBestForCategoryId());
        dto.setSubmittedByUserId(place.getSubmittedByUserId());
        dto.setStatus(place.getStatus());
        dto.setRejectionReason(place.getRejectionReason());
        
        // Calculate average rating
        Double avgRating = ratingRepository.calculateAverageRating(place.getPlaceId());
        dto.setAverageRating(avgRating != null ? avgRating : 0.0);
        
        // Map category names
        if (place.getPlaceCategories() != null && !place.getPlaceCategories().isEmpty()) {
            dto.setCategories(place.getPlaceCategories().stream()
                    .map(pc -> pc.getCategory().getCategoryName())
                    .collect(Collectors.toList()));
        }
        
        // Map image URLs
        if (place.getImages() != null && !place.getImages().isEmpty()) {
            dto.setImageUrls(place.getImages().stream()
                    .map(FoodPlaceImage::getImageUrl)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
