package com.cdac.hostel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cdac.hostel.model.CategoryStatus;
import com.cdac.hostel.model.HostelCategory;

/**
 * Repository interface for HostelCategory entity operations.
 * Provides methods for category retrieval and filtering by status.
 */
@Repository
public interface CategoryRepository extends JpaRepository<HostelCategory, Long> {

    /**
     * Finds all categories with a specific status (PENDING, APPROVED, REJECTED).
     *
     * @param status The category status to filter by
     * @return List of categories with the specified status
     */
    List<HostelCategory> findByStatus(CategoryStatus status);

    /**
     * Finds a category by its exact name.
     * Used to check for duplicate category names before creation.
     *
     * @param categoryName The category name to search for
     * @return Optional containing the category if found
     */
    Optional<HostelCategory> findByCategoryName(String categoryName);
}
