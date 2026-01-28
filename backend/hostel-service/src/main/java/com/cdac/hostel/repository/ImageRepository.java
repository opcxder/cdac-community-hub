package com.cdac.hostel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.hostel.model.HostelImage;

/**
 * Repository interface for HostelImage entity operations.
 * Manages hostel image storage and retrieval.
 */
@Repository
public interface ImageRepository extends JpaRepository<HostelImage, Long> {

    /**
     * Finds all images for a specific hostel, ordered by display order.
     *
     * @param hostelId The ID of the hostel
     * @return List of images ordered by displayOrder
     */
    List<HostelImage> findByHostelIdOrderByDisplayOrderAsc(Long hostelId);

    /**
     * Counts the number of images for a specific hostel.
     * Used to enforce the 5-image limit.
     *
     * @param hostelId The ID of the hostel
     * @return Number of images for the hostel
     */
    long countByHostelId(Long hostelId);

    /**
     * Deletes all images for a specific hostel.
     * Used when a hostel is deleted.
     *
     * @param hostelId The ID of the hostel
     */
    @Transactional
    void deleteByHostelId(Long hostelId);
}
