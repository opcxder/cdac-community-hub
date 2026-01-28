package com.cdac.food.repository;

import com.cdac.food.model.FoodReviewReply;
import com.cdac.food.model.FoodPlaceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodReviewReply entity.
 * Provides database operations for review replies.
 */
@Repository
public interface ReplyRepository extends JpaRepository<FoodReviewReply, Long> {
    
    /**
     * Find all replies for a specific rating ordered by creation date (oldest first).
     * Used to display reply threads in chronological order.
     * 
     * @param rating the FoodPlaceRating entity
     * @return list of replies ordered by creation date ascending
     */
    List<FoodReviewReply> findByRatingOrderByCreatedAtAsc(FoodPlaceRating rating);
    
    /**
     * Find all replies submitted by a specific user.
     * Used to show user's reply history in their profile.
     * 
     * @param userId the ID of the user who posted the replies
     * @return list of replies ordered by creation date descending
     */
    List<FoodReviewReply> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Count total number of replies for a specific rating.
     * Used to display reply count on reviews.
     * 
     * @param rating the FoodPlaceRating entity
     * @return count of replies
     */
    long countByRating(FoodPlaceRating rating);
    
    /**
     * Delete all replies associated with a specific rating.
     * Used when a rating is deleted (cascade delete).
     * Note: This is typically handled by JPA cascade, but available if needed.
     * 
     * @param rating the FoodPlaceRating entity
     */
    void deleteByRating(FoodPlaceRating rating);
}
