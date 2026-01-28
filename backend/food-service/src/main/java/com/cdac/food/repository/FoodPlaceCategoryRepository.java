package com.cdac.food.repository;

import com.cdac.food.model.FoodPlaceCategory;
import com.cdac.food.model.FoodPlace;
import com.cdac.food.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodPlaceCategory entity (junction table).
 * Provides database operations for the many-to-many relationship between FoodPlace and FoodCategory.
 */
@Repository
public interface FoodPlaceCategoryRepository extends JpaRepository<FoodPlaceCategory, Long> {
    
    /**
     * Find all category associations for a specific food place.
     * Used to get all categories a food place belongs to.
     * 
     * @param foodPlace the FoodPlace entity
     * @return list of FoodPlaceCategory associations
     */
    List<FoodPlaceCategory> findByFoodPlace(FoodPlace foodPlace);
    
    /**
     * Find all food place associations for a specific category.
     * Used to get all food places in a category.
     * 
     * @param category the FoodCategory entity
     * @return list of FoodPlaceCategory associations
     */
    List<FoodPlaceCategory> findByCategory(FoodCategory category);
    
    /**
     * Check if a food place is associated with a specific category.
     * Used to prevent duplicate category assignments.
     * 
     * @param foodPlace the FoodPlace entity
     * @param category the FoodCategory entity
     * @return true if association exists, false otherwise
     */
    boolean existsByFoodPlaceAndCategory(FoodPlace foodPlace, FoodCategory category);
    
    /**
     * Delete all category associations for a specific food place.
     * Used when updating food place categories.
     * 
     * @param foodPlace the FoodPlace entity
     */
    void deleteByFoodPlace(FoodPlace foodPlace);
    
    /**
     * Get all food places that belong to a specific category (with approved status).
     * Custom query to join and filter by food place status.
     * 
     * @param categoryId the ID of the category
     * @param status the approval status (typically APPROVED)
     * @return list of FoodPlaceCategory associations for approved places
     */
    @Query("SELECT fpc FROM FoodPlaceCategory fpc " +
           "WHERE fpc.category.categoryId = :categoryId " +
           "AND fpc.foodPlace.status = :status")
    List<FoodPlaceCategory> findByCategoryIdAndFoodPlaceStatus(
        @Param("categoryId") Long categoryId,
        @Param("status") com.cdac.food.model.ApprovalStatus status
    );
}
