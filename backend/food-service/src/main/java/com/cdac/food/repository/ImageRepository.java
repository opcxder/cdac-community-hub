package com.cdac.food.repository;

import com.cdac.food.model.FoodPlaceImage;
import com.cdac.food.model.FoodPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for FoodPlaceImage entity.
 * Provides database operations for food place images.
 */
@Repository
public interface ImageRepository extends JpaRepository<FoodPlaceImage, Long> {
    
    /**
     * Find all images for a specific food place.
     * Used to display all images on food place detail page.
     * 
     * @param foodPlace the FoodPlace entity
     * @return list of images for the food place
     */
    List<FoodPlaceImage> findByFoodPlace(FoodPlace foodPlace);
    
    /**
     * Find an image by its Cloudinary public ID.
     * Used to check if an image exists before deletion.
     * 
     * @param cloudinaryPublicId the Cloudinary public ID
     * @return Optional containing the image if found
     */
    Optional<FoodPlaceImage> findByCloudinaryPublicId(String cloudinaryPublicId);
    
    /**
     * Count total number of images for a specific food place.
     * Used to enforce the 1-5 images limit.
     * 
     * @param foodPlace the FoodPlace entity
     * @return count of images
     */
    long countByFoodPlace(FoodPlace foodPlace);
    
    /**
     * Delete all images associated with a specific food place.
     * Used when a food place is deleted (cascade delete).
     * Note: This is typically handled by JPA cascade, but available if needed.
     * 
     * @param foodPlace the FoodPlace entity
     */
    void deleteByFoodPlace(FoodPlace foodPlace);
}
