package com.cdac.hostel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.hostel.model.HostelCategoriesMapping;

/**
 * Repository interface for HostelCategoriesMapping entity operations.
 * Manages the many-to-many relationship between hostels and categories.
 */
@Repository
public interface CategoryMappingRepository extends JpaRepository<HostelCategoriesMapping, Long> {

    /**
     * Finds all category mappings for a specific hostel.
     * Used to retrieve all categories associated with a hostel.
     *
     * @param hostelId The ID of the hostel
     * @return List of category mappings for the hostel
     */
    List<HostelCategoriesMapping> findByHostelId(Long hostelId);

    /**
     * Deletes a specific hostel-category mapping.
     * Used when removing a category from a hostel.
     *
     * @param hostelId The ID of the hostel
     * @param categoryId The ID of the category
     */
    @Transactional
    void deleteByHostelIdAndCategoryId(Long hostelId, Long categoryId);
}
