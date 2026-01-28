package com.cdac.food.controller;

import com.cdac.food.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing food place images.
 * Exposes endpoints for uploading and deleting images.
 */
@RestController
@RequestMapping("/api/food")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Upload images for a food place.
     * POST /api/food/places/{placeId}/images
     * 
     * @param placeId the food place ID
     * @param files the image files (can be multiple)
     * @return list of uploaded image URLs
     */
    @PostMapping("/places/{placeId}/images")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @PathVariable Long placeId,
            @RequestParam("images") MultipartFile[] files) {
        logger.info("Request to upload {} images for place: {}", files.length, placeId);
        List<String> imageUrls = imageService.uploadImages(placeId, files);
        
        Map<String, Object> response = new HashMap<>();
        response.put("imageUrls", imageUrls);
        response.put("message", "Images uploaded successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Delete an image.
     * DELETE /api/food/images/{imageId}
     * 
     * @param imageId the image ID
     * @return success message
     */
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable Long imageId) {
        logger.info("Request to delete image: {}", imageId);
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(Map.of("message", "Image deleted successfully"));
    }
}
