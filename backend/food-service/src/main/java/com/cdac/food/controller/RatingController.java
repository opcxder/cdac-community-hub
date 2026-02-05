package com.cdac.food.controller;

import com.cdac.food.dto.RatingDTO;
import com.cdac.food.dto.RatingRequest;
import com.cdac.food.dto.RatingStatsDTO;
import com.cdac.food.dto.ReplyDTO;
import com.cdac.food.service.RatingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing ratings and reviews.
 * Exposes endpoints for rating places, replying to reviews, and retrieving ratings.
 */
@RestController
@RequestMapping("/api/food")
public class RatingController {

    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    
    @PostMapping("/places/{placeId}/rate")
    public ResponseEntity<RatingDTO> ratePlace(
            @PathVariable Long placeId,
            @RequestParam Long userId,
            @Valid @RequestBody RatingRequest request) {
        logger.info("Request to rate place: {}, userId: {}, rating: {}", placeId, userId, request.getRating());
        return ResponseEntity.ok(ratingService.ratePlace(placeId, userId, request));
    }

    
    @GetMapping("/places/{placeId}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatingsForPlace(@PathVariable Long placeId) {
        logger.debug("Request to get ratings for place: {}", placeId);
        return ResponseEntity.ok(ratingService.getRatingsForPlace(placeId));
    }

   
    /**
     * Add a reply to a review.
     */
    @PostMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<ReplyDTO> addReply(
            @PathVariable Long ratingId,
            @RequestParam Long userId,
            @RequestParam String replyText) {
        logger.debug("Request to add reply: ratingId={}, userId={}", ratingId, userId);
        return ResponseEntity.ok(ratingService.addReply(ratingId, userId, replyText));
    }
    
    /**
     * Get user's existing rating for a food place.
     * Returns 404 if user hasn't rated this place yet.
     */
    @GetMapping("/places/{placeId}/my-rating")
    public ResponseEntity<RatingDTO> getMyRating(
            @PathVariable Long placeId,
            @RequestParam Long userId) {
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
    public ResponseEntity<RatingDTO> updateRating(
            @PathVariable Long placeId,
            @RequestParam Long userId,
            @Valid @RequestBody RatingRequest request) {
        logger.info("Request to update rating: placeId={}, userId={}, rating={}", placeId, userId, request.getRating());
        return ResponseEntity.ok(ratingService.updateRating(placeId, userId, request));
    }
}
