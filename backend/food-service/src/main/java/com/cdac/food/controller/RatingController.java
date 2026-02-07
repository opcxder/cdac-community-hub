package com.cdac.food.controller;

import com.cdac.food.dto.RatingDTO;
import com.cdac.food.dto.RatingRequest;
import com.cdac.food.dto.RatingStatsDTO;
import com.cdac.food.dto.ReplyDTO;
import com.cdac.food.service.RatingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing ratings and reviews.
 * Exposes endpoints for rating places, replying to reviews, and retrieving ratings.
 * 
 * Authentication is handled by API Gateway, which forwards userId via X-User-Id header.
 */
@RestController
@RequestMapping("/api/food")
public class RatingController {

    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    /**
     * Parse userId from X-User-Id header
     */
    private Long parseUserId(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            logger.error("Invalid X-User-Id header: {}", userIdHeader);
            return null;
        }
    }

    
    @PostMapping("/places/{placeId}/rate")
    public ResponseEntity<?> ratePlace(
            @PathVariable Long placeId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @Valid @RequestBody RatingRequest request) {
        
        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for rating place: {}", placeId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }
        
        logger.info("Request to rate place: {}, userId: {}, rating: {}", placeId, userId, request.getRating());
        return ResponseEntity.ok(ratingService.ratePlace(placeId, userId, request));
    }

    
    @GetMapping("/places/{placeId}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatingsForPlace(@PathVariable Long placeId) {
        logger.info("📡 [FOOD-CONTROLLER] Received request to get ratings for placeId={}", placeId);
        List<RatingDTO> ratings = ratingService.getRatingsForPlace(placeId);
        logger.info("✅ [FOOD-CONTROLLER] Returning {} ratings for placeId={}", ratings.size(), placeId);
        return ResponseEntity.ok(ratings);
    }
 
    /**
     * Add a reply to a review.
     */
    @PostMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<?> addReply(
            @PathVariable Long ratingId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestBody Map<String, String> request) {
        
        logger.info("📡 [FOOD-CONTROLLER] Received reply request for ratingId={}, X-User-Id={}", ratingId, userIdHeader);

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.error("❌ [FOOD-CONTROLLER] No userId in X-User-Id header for adding reply to rating: {}", ratingId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }
        
        String replyText = request.get("replyText");
        logger.info("📝 [FOOD-CONTROLLER] Processing reply: ratingId={}, userId={}, textLength={}", 
                   ratingId, userId, replyText != null ? replyText.length() : 0);
        
        return ResponseEntity.ok(ratingService.addReply(ratingId, userId, replyText));
    }
    
    /**
     * Get user's existing rating for a food place.
     * Returns 404 if user hasn't rated this place yet.
     */
    @GetMapping("/places/{placeId}/my-rating")
    public ResponseEntity<?> getMyRating(
            @PathVariable Long placeId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for getting my rating: placeId={}", placeId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }
        
        logger.debug("Request to get user rating: placeId={}, userId={}", placeId, userId);
        
        RatingDTO rating = ratingService.getUserRating(placeId, userId);
        
        if (rating == null) {
            logger.debug("No rating found for user: placeId={}, userId={}", placeId, userId);
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(rating);
    }
    
    /**
     * Get rating statistics for a food place.
     * Includes average rating, total count, and breakdown by star value.
     */
    @GetMapping("/places/{placeId}/rating-stats")
    public ResponseEntity<RatingStatsDTO> getRatingStats(@PathVariable Long placeId) {
        logger.debug("Request to get rating stats for place: {}", placeId);
        return ResponseEntity.ok(ratingService.getRatingStats(placeId));
    }
    
    /**
     * Update an existing rating/review.
     * User must have already rated this place.
     */
    @PutMapping("/places/{placeId}/rate")
    public ResponseEntity<?> updateRating(
            @PathVariable Long placeId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @Valid @RequestBody RatingRequest request) {
        
        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for updating rating: placeId={}", placeId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }
        
        logger.info("Request to update rating: placeId={}, userId={}, rating={}", placeId, userId, request.getRating());
        return ResponseEntity.ok(ratingService.updateRating(placeId, userId, request));
    }
}
