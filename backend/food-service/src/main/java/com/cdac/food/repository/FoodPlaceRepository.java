package com.cdac.food.repository;

import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodPlace;
import com.cdac.food.model.PriceRange;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for FoodPlace entity.
 * Provides database operations for food places including search and filtering.
 */
@Repository
public interface FoodPlaceRepository extends JpaRepository<FoodPlace, Long> {
    
    /**
     * Find all food places submitted by a specific user.
     * Used to show user's submitted places in their profile.
     * 
     * @param submittedByUserId the ID of the user who submitted the places
     * @return list of food places submitted by the user
     */
    List<FoodPlace> findBySubmittedByUserId(Long submittedByUserId);
    
    /**
     * Find all food places with a specific status.
     * Used to get approved places for public or pending places for admin.
     * 
     * @param status the approval status (PENDING, APPROVED, REJECTED)
     * @return list of food places with the given status
     */
    List<FoodPlace> findByStatus(ApprovalStatus status);
    
    /**
     * Find all food places with a specific status with pagination.
     * Used for public listing of approved places with pagination support.
     * 
     * @param status the approval status
     * @param pageable pagination information
     * @return page of food places with the given status
     */
    Page<FoodPlace> findByStatus(ApprovalStatus status, Pageable pageable);
    
    /**
     * Search food places by name (case-insensitive) with specific status.
     * Used for search functionality on public pages.
     * 
     * @param placeName the search term for place name
     * @param status the approval status
     * @param pageable pagination information
     * @return page of matching food places
     */
    Page<FoodPlace> findByPlaceNameContainingIgnoreCaseAndStatus(
        String placeName, 
        ApprovalStatus status, 
        Pageable pageable
    );
    
    /**
     * Find food places by price range and status with pagination.
     * Used to filter places by price category.
     * 
     * @param priceRange the price range filter
     * @param status the approval status
     * @param pageable pagination information
     * @return page of food places matching the criteria
     */
    Page<FoodPlace> findByPriceRangeAndStatus(
        PriceRange priceRange, 
        ApprovalStatus status, 
        Pageable pageable
    );
    
    /**
     * Find food places by city and status with pagination.
     * Used to filter places by city.
     * 
     * @param city the city name
     * @param status the approval status
     * @param pageable pagination information
     * @return page of food places in the specified city
     */
    Page<FoodPlace> findByCityAndStatus(
        String city, 
        ApprovalStatus status, 
        Pageable pageable
    );
    
    /**
     * Advanced search for food places with multiple optional filters.
     * Searches by name, filters by price range, city, and status.
     * 
     * @param placeName search term for place name (can be null)
     * @param priceRange price range filter (can be null)
     * @param city city filter (can be null)
     * @param status approval status (required)
     * @param pageable pagination information
     * @return page of food places matching all provided criteria
     */
    @Query("SELECT fp FROM FoodPlace fp WHERE " +
           "(:placeName IS NULL OR LOWER(fp.placeName) LIKE LOWER(CONCAT('%', :placeName, '%'))) AND " +
           "(:priceRange IS NULL OR fp.priceRange = :priceRange) AND " +
           "(:city IS NULL OR fp.city = :city) AND " +
           "fp.status = :status")
    Page<FoodPlace> searchFoodPlaces(
        @Param("placeName") String placeName,
        @Param("priceRange") PriceRange priceRange,
        @Param("city") String city,
        @Param("status") ApprovalStatus status,
        Pageable pageable
    );
    
    /**
     * Find all food places ordered by creation date (newest first).
     * Used for admin dashboard.
     * 
     * @return list of all food places ordered by creation date descending
     */
    List<FoodPlace> findAllByOrderByCreatedAtDesc();
}
