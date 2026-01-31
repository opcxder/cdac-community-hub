package com.cdac.hostel.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.hostel.model.HostelImage;
import com.cdac.hostel.service.ImageService;

/**
 * REST controller for hostel image operations.
 * Handles image upload, retrieval, and deletion.
 */
@RestController
@RequestMapping("/api/hostel")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * Upload images for a hostel.
     * POST /api/hostel/hostels/{hostelId}/images
     *
     * @param hostelId The ID of the hostel
     * @param files    The image files (can be multiple)
     * @return Map containing list of uploaded image URLs
     */
    @PostMapping("/hostels/{hostelId}/images")
    public ResponseEntity<Map<String, Object>> uploadImages(
            @PathVariable Long hostelId,
            @RequestParam("images") MultipartFile[] files) {

        List<String> imageUrls = imageService.uploadImages(hostelId, files);

        Map<String, Object> response = new HashMap<>();
        response.put("imageUrls", imageUrls);
        response.put("message", "Images uploaded successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all images for a specific hostel.
     * Images are ordered by display order.
     *
     * @param hostelId The ID of the hostel
     * @return List of images for the hostel
     */
    @GetMapping("/hostel/{hostelId}")
    public ResponseEntity<List<HostelImage>> getImagesByHostel(@PathVariable Long hostelId) {
        List<HostelImage> images = imageService.getImagesByHostel(hostelId);
        return ResponseEntity.ok(images);
    }

    /**
     * Deletes a specific image.
     * Removes the image from both Cloudinary and database.
     *
     * @param imageId The ID of the image to delete
     * @return 204 NO CONTENT status
     */
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
