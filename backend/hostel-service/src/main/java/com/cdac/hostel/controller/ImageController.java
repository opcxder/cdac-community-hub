package com.cdac.hostel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/hostel/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * Uploads an image for a hostel.
     * Maximum 5 images allowed per hostel.
     *
     * @param hostelId The ID of the hostel
     * @param file The image file to upload
     * @param displayOrder The display order (1-5)
     * @return The created HostelImage entity with 201 CREATED status
     */
    @PostMapping("/upload")
    public ResponseEntity<HostelImage> uploadImage(
            @RequestParam Long hostelId,
            @RequestParam MultipartFile file,
            @RequestParam Integer displayOrder) {

        HostelImage image = imageService.uploadImage(hostelId, file, displayOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
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
