package com.cdac.food.repository;

import com.cdac.food.model.FoodPlaceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for FoodPlaceRating entity.
 * Provides database operations for ratings and reviews.
 */
@Repository
public interface RatingRepository extends JpaRepository<FoodPlaceRating, Long> {
    
    /**
     * Find all ratings for a specific food place ordered by creation date (newest first).
     * Used to display reviews on food place detail page.
     * 
     * @param foodPlace the FoodPlace entity
     * @return list of ratings ordered by creation date descending
     */
    List<FoodPlaceRating> findByFoodPlaceOrderByCreatedAtDesc(com.cdac.food.model.FoodPlace foodPlace);
    
    /**
     * Check if a rating already exists for a specific user and place combination.
     * Used to enforce the one-rating-per-user-per-place rule.
     * CRITICAL: This prevents duplicate ratings.
     * 
     * @param foodPlace the FoodPlace entity
     * @param userId the ID of the user
     * @return true if a rating exists, false otherwise
     */
    boolean existsByFoodPlaceAndUserId(com.cdac.food.model.FoodPlace foodPlace, Long userId);
    
    /**
     * Find a specific rating by food place and user.
     * Used to retrieve existing rating for update operations.
     * 
     * @param foodPlace the FoodPlace entity
     * @param userId the ID of the user
     * @return Optional containing the rating if found
     */
    Optional<FoodPlaceRating> findByFoodPlaceAndUserId(com.cdac.food.model.FoodPlace foodPlace, Long userId);
    
    /**
     * Calculate the average rating for a specific food place.
     * Used to display average rating on food place listings and details.
     * 
     * @param placeId the ID of the food place
     * @return the average rating (1.0 to 5.0) or null if no ratings exist
     */
    @Query("SELECT AVG(r.rating) FROM FoodPlaceRating r WHERE r.foodPlace.placeId = :placeId")
    Double calculateAverageRating(@Param("placeId") Long placeId);
    
    /**
     * Count total number of ratings for a specific food place.
     * Used to display rating count alongside average rating.
     * 
     * @param foodPlace the FoodPlace entity
     * @return count of ratings
     */
    long countByFoodPlace(com.cdac.food.model.FoodPlace foodPlace);
    
    /**
     * Find all ratings submitted by a specific user.
     * Used to show user's rating history in their profile.
     * 
     * @param userId the ID of the user
     * @return list of ratings submitted by the user
     */
    List<FoodPlaceRating> findByUserIdOrderByCreatedAtDesc(Long userId);
}
