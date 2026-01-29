package com.cdac.food.service;

import com.cdac.food.exception.ResourceNotFoundException;
import com.cdac.food.model.FoodPlace;
import com.cdac.food.model.FoodPlaceImage;
import com.cdac.food.repository.FoodPlaceRepository;
import com.cdac.food.repository.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Service layer for managing food place images.
 * Handles image upload to Cloudinary and deletion.
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final int MAX_IMAGES_PER_PLACE = 5;

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;
    private final FoodPlaceRepository foodPlaceRepository;

    public ImageService(Cloudinary cloudinary,
            ImageRepository imageRepository,
            FoodPlaceRepository foodPlaceRepository) {
        this.cloudinary = cloudinary;
        this.imageRepository = imageRepository;
        this.foodPlaceRepository = foodPlaceRepository;
    }

    /**
     * Upload images for a food place to Cloudinary.
     * Supports 1-5 images per food place.
     * 
     * @param placeId the ID of the food place
     * @param files   array of image files to upload
     * @return list of image URLs
     * @throws RuntimeException if food place not found
     * @throws RuntimeException if image limit exceeded
     * @throws RuntimeException if upload fails
     */
    @Transactional
    public List<String> uploadImages(Long placeId, MultipartFile[] files) {
        logger.info("Uploading {} images for place: placeId={}", files.length, placeId);

        // Validate food place exists
        FoodPlace place = foodPlaceRepository.findById(placeId)
                .orElseThrow(() -> {
                    logger.error("Image upload failed - place not found: placeId={}", placeId);
                    return new ResourceNotFoundException("Food place not found: " + placeId);
                });

        // Check current image count
        long currentImageCount = imageRepository.countByFoodPlace(place);
        long totalAfterUpload = currentImageCount + files.length;

        if (totalAfterUpload > MAX_IMAGES_PER_PLACE) {
            logger.error("Image upload failed - limit exceeded: placeId={}, current={}, uploading={}, max={}",
                    placeId, currentImageCount, files.length, MAX_IMAGES_PER_PLACE);
            throw new RuntimeException(String.format(
                    "Cannot upload %d images. Food place already has %d images. Maximum is %d images per place.",
                    files.length, currentImageCount, MAX_IMAGES_PER_PLACE));
        }

        List<String> imageUrls = new ArrayList<>();

        // Upload each file to Cloudinary
        for (MultipartFile file : files) {
            try {
                logger.debug("Uploading image to Cloudinary: filename={}, size={} bytes",
                        file.getOriginalFilename(), file.getSize());

                // Upload to Cloudinary in cdac/food folder
                Map<String, Object> uploadResult = cloudinary.uploader().upload(
                        file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "cdac/food",
                                "resource_type", "image"));
                // Extract secure URL and public ID
                String imageUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                logger.debug("Image uploaded to Cloudinary: url={}, publicId={}", imageUrl, publicId);

                // Save image record to database
                FoodPlaceImage image = new FoodPlaceImage();
                image.setFoodPlace(place);
                image.setImageUrl(imageUrl);
                image.setCloudinaryPublicId(publicId);
                image.setUploadedAt(LocalDateTime.now());

                imageRepository.save(image);

                imageUrls.add(imageUrl);

                logger.info("Image saved successfully: placeId={}, imageUrl={}", placeId, imageUrl);

            } catch (IOException e) {
                logger.error("Failed to upload image for place {}: {}", placeId, e.getMessage(), e);
                throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
            }
        }

        logger.info("Successfully uploaded {} images for place: placeId={}", imageUrls.size(), placeId);

        return imageUrls;
    }

    /**
     * Delete an image from Cloudinary and database.
     * 
     * @param imageId the ID of the image to delete
     * @throws RuntimeException if image not found
     * @throws RuntimeException if Cloudinary deletion fails
     */
    @Transactional
    public void deleteImage(Long imageId) {
        logger.info("Deleting image: imageId={}", imageId);

        // Find image record
        FoodPlaceImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> {
                    logger.error("Image deletion failed - not found: imageId={}", imageId);
                    return new ResourceNotFoundException("Image not found: " + imageId);
                });

        try {
            // Delete from Cloudinary
            logger.debug("Deleting image from Cloudinary: publicId={}", image.getCloudinaryPublicId());

            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().destroy(
                    image.getCloudinaryPublicId(),
                    ObjectUtils.emptyMap());
            logger.debug("Cloudinary deletion result: {}", result);

            // Delete from database
            imageRepository.delete(image);

            logger.info("Image deleted successfully: imageId={}, publicId={}",
                    imageId, image.getCloudinaryPublicId());

        } catch (IOException e) {
            logger.error("Failed to delete image from Cloudinary: imageId={}, publicId={}, error={}",
                    imageId, image.getCloudinaryPublicId(), e.getMessage(), e);
            throw new RuntimeException("Image deletion failed: " + e.getMessage(), e);
        }
    }
}
