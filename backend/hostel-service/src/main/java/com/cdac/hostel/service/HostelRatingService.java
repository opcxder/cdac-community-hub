package com.cdac.hostel.service;



import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.hostel.client.AuthServiceClient;
import com.cdac.hostel.dto.MultiCriteriaRatingRequest;
import com.cdac.hostel.dto.RatingDTO;
import com.cdac.hostel.model.HostelRating;
import com.cdac.hostel.model.HostelReviewReply;
import com.cdac.hostel.repository.HostelRatingRepository;
import com.cdac.hostel.repository.ReviewReplyRepository;

import com.cdac.hostel.exception.DuplicateResourceException;
import com.cdac.hostel.exception.ResourceNotFoundException;
/**
 * Service layer for hostel rating and reply operations.
 * Implements multi-criteria rating system where users rate hostels across 5 dimensions:
 * cleanliness, food quality, safety, location, and affordability.
 * Also handles review replies from hostel owners/admins.
 */
@Service
public class HostelRatingService {

    private static final Logger logger = LoggerFactory.getLogger(HostelRatingService.class);

    @Autowired
    private HostelRatingRepository ratingRepository;

    @Autowired
    private ReviewReplyRepository replyRepository;

    @Autowired
    private AuthServiceClient authClient;

    /**
     * Creates a new multi-criteria rating for a hostel.
     * Validates user existence and prevents duplicate ratings.
     * Each rating consists of 5 separate criteria scores (1-5 each).
     *
     * @param hostelId The ID of the hostel being rated
     * @param userId The ID of the user submitting the rating
     * @param req The rating request containing all 5 criteria scores and optional review text
     * @return The created rating entity
     * @throws RuntimeException if user not found or user already rated this hostel
     */
    public HostelRating rateHostel(
            Long hostelId, Long userId, MultiCriteriaRatingRequest req) {

        logger.info("User {} attempting to rate hostel {}", userId, hostelId);
        
        // Validate user exists via Auth Service
        if (!authClient.userExists(userId)) {
            logger.error("Rating failed - User not found: userId={}", userId);
            throw new ResourceNotFoundException("User not found");
        }

        // Check for duplicate rating (one rating per user per hostel)
        ratingRepository.findByHostelIdAndUserId(hostelId, userId)
            .ifPresent(r -> {
                logger.warn("Duplicate rating attempt: userId={}, hostelId={}, existingRatingId={}", 
                           userId, hostelId, r.getRatingId());
                throw new DuplicateResourceException("User already rated this hostel");
            });

        // Create rating entity with all 5 criteria
        HostelRating rating = new HostelRating();
        rating.setHostelId(hostelId);
        rating.setUserId(userId);
        rating.setCleanlinessRating(req.getCleanlinessRating());
        rating.setFoodQualityRating(req.getFoodQualityRating());
        rating.setSafetyRating(req.getSafetyRating());
        rating.setLocationRating(req.getLocationRating());
        rating.setAffordabilityRating(req.getAffordabilityRating());
        rating.setReviewText(req.getReviewText());

        HostelRating savedRating = ratingRepository.save(rating);
        
        // Calculate overall rating for logging
        double overall = (req.getCleanlinessRating() + req.getFoodQualityRating() + 
                         req.getSafetyRating() + req.getLocationRating() + 
                         req.getAffordabilityRating()) / 5.0;
        
        logger.info("Rating created successfully: ratingId={}, hostelId={}, userId={}, overall={}", 
                    savedRating.getRatingId(), hostelId, userId, overall);

        return savedRating;
    }
    
    /**
     * Retrieves all ratings for a specific hostel.
     *
     * @param hostelId The ID of the hostel
     * @return List of all ratings for the hostel
     */
    public List<HostelRating> getRatingsByHostel(Long hostelId) {
        logger.debug("Fetching all ratings for hostel: hostelId={}", hostelId);
        List<HostelRating> ratings = ratingRepository.findByHostelId(hostelId);
        logger.info("Retrieved {} ratings for hostelId={}", ratings.size(), hostelId);
        return ratings;
    }
    
    /**
     * Calculates and returns the rating summary for a hostel.
     * Computes the average for each of the 5 criteria individually,
     * plus an overall average across all criteria.
     *
     * @param hostelId The ID of the hostel
     * @return RatingDTO containing individual criterion averages and overall average
     */
    public RatingDTO getRatingSummary(Long hostelId) {
        logger.debug("Calculating rating summary for hostel: hostelId={}", hostelId);

        List<HostelRating> ratings = ratingRepository.findByHostelId(hostelId);

        RatingDTO dto = new RatingDTO();
        dto.setHostelId(hostelId);

        // Handle case where hostel has no ratings yet
        if (ratings == null || ratings.isEmpty()) {
            logger.info("No ratings found for hostelId={}", hostelId);
            dto.setOverallRating(0.0);
            return dto;
        }

        // Calculate sum of each criterion across all ratings
        double cleanlinessSum = 0;
        double foodQualitySum = 0;
        double safetySum = 0;
        double locationSum = 0;
        double affordabilitySum = 0;

        int count = ratings.size();

        for (HostelRating rating : ratings) {
            cleanlinessSum += rating.getCleanlinessRating();
            foodQualitySum += rating.getFoodQualityRating();
            safetySum += rating.getSafetyRating();
            locationSum += rating.getLocationRating();
            affordabilitySum += rating.getAffordabilityRating();
        }

        // Calculate average for each criterion
        double cleanlinessAvg = cleanlinessSum / count;
        double foodQualityAvg = foodQualitySum / count;
        double safetyAvg = safetySum / count;
        double locationAvg = locationSum / count;
        double affordabilityAvg = affordabilitySum / count;

        // Calculate overall average (average of the 5 criterion averages)
        double overallAvg =
                (cleanlinessAvg +
                 foodQualityAvg +
                 safetyAvg +
                 locationAvg +
                 affordabilityAvg) / 5;

        // Populate DTO with calculated averages
        dto.setCleanlinessRating(cleanlinessAvg);
        dto.setFoodQualityRating(foodQualityAvg); 
        dto.setSafetyRating(safetyAvg);
        dto.setLocationRating(locationAvg); 
        dto.setAffordabilityRating(affordabilityAvg);
        dto.setOverallRating(overallAvg);

        logger.info("Rating summary calculated for hostelId={}: overall={}, count={}", 
                    hostelId, overallAvg, count);

        return dto;
    }
  // ADD THESE METHODS TO HostelRatingService.java BEFORE THE CLOSING BRACE }

    // ========== Review Reply Methods ==========

    /**
     * Creates a reply to a hostel rating/review.
     * Validates user existence and prevents duplicate replies.
     * Only one reply is allowed per rating.
     *
     * @param ratingId The ID of the rating to reply to
     * @param userId The ID of the user posting the reply (hostel owner/admin)
     * @param replyText The reply text content
     * @return The created reply entity
     * @throws RuntimeException if user not found, rating not found, or reply already exists
     */
    public HostelReviewReply createReply(Long ratingId, Long userId, String replyText) {
        logger.info("User {} attempting to reply to rating {}", userId, ratingId);

        // Validate user exists
        if (!authClient.userExists(userId)) {
            logger.error("Reply failed - User not found: userId={}", userId);
           throw new ResourceNotFoundException("User not found");
        }

        // Verify rating exists
        ratingRepository.findById(ratingId)
                .orElseThrow(() -> {
                    logger.error("Reply failed - Rating not found: ratingId={}", ratingId);
                   throw new ResourceNotFoundException("Rating", ratingId);
                });

        // Check for duplicate reply
        replyRepository.findByRatingId(ratingId)
                .ifPresent(r -> {
                    logger.warn("Duplicate reply attempt: ratingId={}, existingReplyId={}", 
                               ratingId, r.getReplyId());
                    throw new DuplicateResourceException("This rating already has a reply");
                });

        // Create reply
        HostelReviewReply reply = new HostelReviewReply();
        reply.setRatingId(ratingId);
        reply.setRepliedByUserId(userId);
        reply.setReplyText(replyText);

        HostelReviewReply savedReply = replyRepository.save(reply);
        logger.info("Reply created successfully: replyId={}, ratingId={}, userId={}", 
                    savedReply.getReplyId(), ratingId, userId);

        return savedReply;
    }

    /**
     * Retrieves the reply for a specific rating, if it exists.
     *
     * @param ratingId The ID of the rating
     * @return The reply if found, null otherwise
     */
    public HostelReviewReply getReplyForRating(Long ratingId) {
        logger.debug("Fetching reply for rating: ratingId={}", ratingId);
        return replyRepository.findByRatingId(ratingId).orElse(null);
    }

    /**
     * Deletes a reply to a rating.
     *
     * @param replyId The ID of the reply to delete
     * @throws RuntimeException if reply not found
     */
    public void deleteReply(Long replyId) {
        logger.info("Deleting reply: replyId={}", replyId);

        replyRepository.findById(replyId)
                .orElseThrow(() -> {
                    logger.error("Reply deletion failed - Reply not found: replyId={}", replyId);
                    throw new ResourceNotFoundException("Reply", replyId);

                });

        replyRepository.deleteById(replyId);
        logger.info("Reply deleted successfully: replyId={}", replyId);
    }

}

