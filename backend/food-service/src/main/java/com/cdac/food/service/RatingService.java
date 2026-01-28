package com.cdac.food.service;

import com.cdac.food.dto.RatingDTO;
import com.cdac.food.dto.RatingRequest;
import com.cdac.food.dto.ReplyDTO;
import com.cdac.food.dto.UserDTO;
import com.cdac.food.exception.DuplicateRatingException;
import com.cdac.food.exception.ResourceNotFoundException;
import com.cdac.food.exception.UserNotFoundException;
import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodPlace;
import com.cdac.food.model.FoodPlaceRating;
import com.cdac.food.model.FoodReviewReply;
import com.cdac.food.repository.FoodPlaceRepository;
import com.cdac.food.repository.RatingRepository;
import com.cdac.food.repository.ReplyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing ratings and reviews.
 * Handles rating submission, reply functionality, and rating retrieval.
 */
@Service
public class RatingService {
    
    private static final Logger logger = LoggerFactory.getLogger(RatingService.class);
    
    private final RatingRepository ratingRepository;
    private final ReplyRepository replyRepository;
    private final FoodPlaceRepository foodPlaceRepository;
    private final AuthServiceClient authServiceClient;
    
    public RatingService(RatingRepository ratingRepository,
                        ReplyRepository replyRepository,
                        FoodPlaceRepository foodPlaceRepository,
                        AuthServiceClient authServiceClient) {
        this.ratingRepository = ratingRepository;
        this.replyRepository = replyRepository;
        this.foodPlaceRepository = foodPlaceRepository;
        this.authServiceClient = authServiceClient;
    }
    
    /**
     * Submit a rating/review for a food place.
     * Each user can rate a place only once (enforced by database unique constraint).
     * Only approved food places can be rated.
     * 
     * @param placeId the ID of the food place to rate
     * @param userId the ID of the user submitting the rating
     * @param request the rating request containing rating value and review text
     * @return RatingDTO of the created rating
     * @throws RuntimeException if user does not exist
     * @throws RuntimeException if food place not found or not approved
     * @throws RuntimeException if user already rated this place
     */
    @Transactional
    public RatingDTO ratePlace(Long placeId, Long userId, RatingRequest request) {
        logger.info("User {} rating place {}: rating={}/5", userId, placeId, request.getRating());
        
        // Validate user exists
        if (!authServiceClient.userExists(userId)) {
            logger.error("Rating failed - user not found: userId={}", userId);
            throw new UserNotFoundException("User not found: " + userId);
        }
        
        // Validate food place exists and is approved
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Rating failed - place not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });
        
        if (place.getStatus() != ApprovalStatus.APPROVED) {
            logger.error("Rating failed - place not approved: placeId={}, status={}", 
                        placeId, place.getStatus());
            throw new IllegalStateException("Cannot rate a food place that is not approved");
        }
        
        // Check if user already rated this place (duplicate prevention)
        if (ratingRepository.existsByFoodPlaceAndUserId(place, userId)) {
            logger.error("Rating failed - duplicate rating: placeId={}, userId={}", placeId, userId);
            throw new DuplicateRatingException("You have already rated this food place");
        }
        
        // Validate rating value (1-5)
        if (request.getRating() < 1 || request.getRating() > 5) {
            logger.error("Rating failed - invalid rating value: {}", request.getRating());
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        // Create rating
        FoodPlaceRating rating = new FoodPlaceRating();
        rating.setFoodPlace(place);
        rating.setUserId(userId);
        rating.setRating(request.getRating());
        rating.setReviewText(request.getReviewText());
        rating.setCreatedAt(LocalDateTime.now());
        rating.setUpdatedAt(LocalDateTime.now());
        
        FoodPlaceRating saved = ratingRepository.save(rating);
        
        logger.info("Rating created successfully: ratingId={}, placeId={}, userId={}, rating={}/5", 
                   saved.getRatingId(), placeId, userId, request.getRating());
        
        return mapToDTO(saved);
    }
    
    /**
     * Get all ratings for a specific food place.
     * Includes replies for each rating.
     * 
     * @param placeId the ID of the food place
     * @return list of ratings with replies
     * @throws RuntimeException if food place not found
     */
    public List<RatingDTO> getRatingsForPlace(Long placeId) {
        logger.debug("Fetching ratings for place: placeId={}", placeId);
        
        // Validate food place exists
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Place not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });
        
        List<FoodPlaceRating> ratings = ratingRepository.findByFoodPlaceOrderByCreatedAtDesc(place);
        
        logger.debug("Found {} ratings for place: placeId={}", ratings.size(), placeId);
        
        return ratings.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get average rating for a food place.
     * 
     * @param placeId the ID of the food place
     * @return average rating (1.0 to 5.0) or 0.0 if no ratings
     */
    public Double getAverageRating(Long placeId) {
        logger.debug("Calculating average rating for place: placeId={}", placeId);
        
        Double avgRating = ratingRepository.calculateAverageRating(placeId);
        Double result = avgRating != null ? avgRating : 0.0;
        
        logger.debug("Average rating for place {}: {}/5", placeId, result);
        
        return result;
    }
    
    /**
     * Add a reply to a rating/review.
     * Users can reply to reviews to create discussion threads.
     * 
     * @param ratingId the ID of the rating to reply to
     * @param userId the ID of the user posting the reply
     * @param replyText the reply text
     * @return ReplyDTO of the created reply
     * @throws RuntimeException if user does not exist
     * @throws RuntimeException if rating not found
     */
    @Transactional
    public ReplyDTO addReply(Long ratingId, Long userId, String replyText) {
        logger.info("User {} replying to rating {}", userId, ratingId);
        
        // Validate user exists
        if (!authServiceClient.userExists(userId)) {
            logger.error("Reply failed - user not found: userId={}", userId);
            throw new UserNotFoundException("User not found: " + userId);
        }
        
        // Validate rating exists
        FoodPlaceRating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> {
                    logger.error("Reply failed - rating not found: ratingId={}", ratingId);
                    return new ResourceNotFoundException("Rating not found: " + ratingId);
                });
        
        // Validate reply text is not empty
        if (replyText == null || replyText.trim().isEmpty()) {
            logger.error("Reply failed - empty reply text");
            throw new IllegalArgumentException("Reply text cannot be empty");
        }
        
        // Create reply
        FoodReviewReply reply = new FoodReviewReply();
        reply.setRating(rating);
        reply.setUserId(userId);
        reply.setReplyText(replyText.trim());
        reply.setCreatedAt(LocalDateTime.now());
        
        FoodReviewReply saved = replyRepository.save(reply);
        
        logger.info("Reply created successfully: replyId={}, ratingId={}, userId={}", 
                   saved.getReplyId(), ratingId, userId);
        
        return mapReplyToDTO(saved);
    }
    
    /**
     * Map FoodPlaceRating entity to RatingDTO.
     * Includes user information and replies.
     * 
     * @param rating the FoodPlaceRating entity
     * @return RatingDTO
     */
    private RatingDTO mapToDTO(FoodPlaceRating rating) {
        RatingDTO dto = new RatingDTO();
        dto.setRatingId(rating.getRatingId());
        dto.setUserId(rating.getUserId());
        dto.setRating(rating.getRating());
        dto.setReviewText(rating.getReviewText());
        dto.setCreatedAt(rating.getCreatedAt());
        dto.setUpdatedAt(rating.getUpdatedAt());
        
        // Fetch username from Auth Service directly
        try {
            UserDTO user = authServiceClient.getUserById(rating.getUserId());
            dto.setUsername(user.getUsername());
        } catch (Exception e) {
            logger.warn("Failed to fetch username for userId={}: {}", 
                       rating.getUserId(), e.getMessage());
            dto.setUsername("User" + rating.getUserId());
        }
        
        // Map replies
        if (rating.getReplies() != null && !rating.getReplies().isEmpty()) {
            dto.setReplies(rating.getReplies().stream()
                    .map(this::mapReplyToDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    /**
     * Map FoodReviewReply entity to ReplyDTO.
     * Includes user information.
     * 
     * @param reply the FoodReviewReply entity
     * @return ReplyDTO
     */
    private ReplyDTO mapReplyToDTO(FoodReviewReply reply) {
        ReplyDTO dto = new ReplyDTO();
        dto.setReplyId(reply.getReplyId());
        dto.setUserId(reply.getUserId());
        dto.setReplyText(reply.getReplyText());
        dto.setCreatedAt(reply.getCreatedAt());
        
        // Fetch username from Auth Service directly
        try {
            UserDTO user = authServiceClient.getUserById(reply.getUserId());
            dto.setUsername(user.getUsername());
        } catch (Exception e) {
            logger.warn("Failed to fetch username for userId={}: {}", 
                       reply.getUserId(), e.getMessage());
            dto.setUsername("User" + reply.getUserId());
        }
        
        return dto;
    }
}
