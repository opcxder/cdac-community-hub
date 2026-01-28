package com.cdac.food.repository;

import com.cdac.food.model.ApprovalStatus;
import com.cdac.food.model.FoodCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for FoodCategory entity.
 * Provides database operations for food categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<FoodCategory, Long> {
    
    /**
     * Find a category by its name (case-sensitive).
     * Used to check for duplicate category names.
     * 
     * @param categoryName the name of the category
     * @return Optional containing the category if found
     */
    Optional<FoodCategory> findByCategoryName(String categoryName);
    
    /**
     * Find all categories with a specific status.
     * Used to get approved categories for public display or pending categories for admin.
     * 
     * @param status the approval status (PENDING, APPROVED, REJECTED)
     * @return list of categories with the given status
     */
    List<FoodCategory> findByStatus(ApprovalStatus status);
    
    /**
     * Find all categories ordered by creation date (newest first).
     * Used for admin dashboard to see recently submitted categories.
     * 
     * @return list of all categories ordered by creation date descending
     */
    List<FoodCategory> findAllByOrderByCreatedAtDesc();
}
