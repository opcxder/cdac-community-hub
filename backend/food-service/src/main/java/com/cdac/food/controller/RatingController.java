package com.cdac.food.controller;

import com.cdac.food.dto.RatingDTO;
import com.cdac.food.dto.RatingRequest;
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

    /**
     * Submit a rating for a food place.
     * POST /api/food/places/{placeId}/rate
     * 
     * @param placeId the food place ID
     * @param userId the user ID (passed as query param for now, effectively from context)
     * @param request the rating details
     * @return the created rating
     */
    @PostMapping("/places/{placeId}/rate")
    public ResponseEntity<RatingDTO> ratePlace(
            @PathVariable Long placeId,
            @RequestParam Long userId,
            @Valid @RequestBody RatingRequest request) {
        logger.info("Request to rate place: {}, userId: {}, rating: {}", placeId, userId, request.getRating());
        return ResponseEntity.ok(ratingService.ratePlace(placeId, userId, request));
    }

    /**
     * Get all ratings for a food place.
     * GET /api/food/places/{placeId}/ratings
     * 
     * @param placeId the food place ID
     * @return list of ratings with replies
     */
    @GetMapping("/places/{placeId}/ratings")
    public ResponseEntity<List<RatingDTO>> getRatingsForPlace(@PathVariable Long placeId) {
        logger.debug("Request to get ratings for place: {}", placeId);
        return ResponseEntity.ok(ratingService.getRatingsForPlace(placeId));
    }

    /**
     * Reply to a review.
     * POST /api/food/ratings/{ratingId}/reply
     * 
     * @param ratingId the rating ID
     * @param userId the user ID replying
     * @param request body containing "replyText"
     * @return the created reply
     */
    @PostMapping("/ratings/{ratingId}/reply")
    public ResponseEntity<ReplyDTO> replyToReview(
            @PathVariable Long ratingId,
            @RequestParam Long userId,
            @RequestBody Map<String, String> request) {
        String replyText = request.get("replyText");
        logger.info("Request to reply to rating: {}, userId: {}", ratingId, userId);
        return ResponseEntity.ok(ratingService.addReply(ratingId, userId, replyText));
    }
}
