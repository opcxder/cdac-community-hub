package com.cdac.hostel.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.hostel.exception.ImageUploadException;
import com.cdac.hostel.exception.InvalidRequestException;
import com.cdac.hostel.exception.ResourceNotFoundException;
import com.cdac.hostel.model.HostelImage;
import com.cdac.hostel.repository.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 * Service layer for hostel image management.
 * Handles image upload to Cloudinary, deletion, and database operations.
 * Enforces the 5-image limit per hostel.
 */
@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);
    private static final int MAX_IMAGES_PER_HOSTEL = 5;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads multiple images for a hostel.
     * Enforces the 5-image limit per hostel.
     *
     * @param hostelId The ID of the hostel
     * @param files    The image files to upload
     * @return List of uploaded image URLs
     */
    public List<String> uploadImages(Long hostelId, MultipartFile[] files) {
        logger.info("Uploading {} images for hostel: hostelId={}", files.length, hostelId);

        // Check image limit
        long currentImageCount = imageRepository.countByHostelId(hostelId);
        if (currentImageCount + files.length > MAX_IMAGES_PER_HOSTEL) {
            throw new InvalidRequestException(
                    "Cannot upload " + files.length + " images. Maximum " + MAX_IMAGES_PER_HOSTEL
                            + " images allowed per hostel. Current count: " + currentImageCount);
        }

        List<String> uploadedUrls = new java.util.ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            try {
                // Upload to Cloudinary
                Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "hostel-service/hostels",
                                "resource_type", "image"));

                String imageUrl = (String) uploadResult.get("secure_url");
                String publicId = (String) uploadResult.get("public_id");

                // Save to database
                HostelImage hostelImage = new HostelImage();
                hostelImage.setHostelId(hostelId);
                hostelImage.setImageUrl(imageUrl);
                hostelImage.setPublicId(publicId);
                // Calculate display order: current count + loop index + 1
                hostelImage.setDisplayOrder((int) currentImageCount + i + 1);

                imageRepository.save(hostelImage);
                uploadedUrls.add(imageUrl);

                logger.debug("Uploaded image {}/{}: url={}", i + 1, files.length, imageUrl);

            } catch (IOException e) {
                logger.error("Failed to upload image {} for hostelId={}: {}", file.getOriginalFilename(), hostelId,
                        e.getMessage());
                throw new ImageUploadException("Failed to upload image: " + file.getOriginalFilename(), e);
            }
        }

        logger.info("Successfully uploaded {} images for hostelId={}", uploadedUrls.size(), hostelId);
        return uploadedUrls;
    }

    /**
     * Uploads a single image (Legacy support, internal use).
     */
    public HostelImage uploadImage(Long hostelId, MultipartFile file, Integer displayOrder) {
        return null; // Not used anymore by controller
    }

    /**
     * Retrieves all images for a specific hostel, ordered by display order.
     *
     * @param hostelId The ID of the hostel
     * @return List of images ordered by displayOrder
     */
    public List<HostelImage> getImagesByHostel(Long hostelId) {
        logger.debug("Fetching images for hostel: hostelId={}", hostelId);
        List<HostelImage> images = imageRepository.findByHostelIdOrderByDisplayOrderAsc(hostelId);
        logger.info("Retrieved {} images for hostelId={}", images.size(), hostelId);
        return images;
    }

    /**
     * Deletes an image from both Cloudinary and database.
     *
     * @param imageId The ID of the image to delete
     * @throws ResourceNotFoundException if image not found
     * @throws ImageUploadException      if deletion fails
     */
    public void deleteImage(Long imageId) {
        logger.info("Deleting image: imageId={}", imageId);

        HostelImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> {
                    logger.error("Image deletion failed - Image not found: imageId={}", imageId);
                    return new ResourceNotFoundException("Image", imageId);
                });

        try {
            // Delete from Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> deleteResult = cloudinary.uploader().destroy(image.getPublicId(),
                    ObjectUtils.emptyMap());
            logger.info("Image deleted from Cloudinary: publicId={}, result={}",
                    image.getPublicId(), deleteResult.get("result"));

            // Delete from database
            imageRepository.deleteById(imageId);
            logger.info("Image deleted from database: imageId={}", imageId);

        } catch (IOException e) {
            logger.error("Image deletion failed for imageId={}: {}", imageId, e.getMessage(), e);
            throw new ImageUploadException("Failed to delete image: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes all images for a specific hostel.
     * Used when a hostel is deleted.
     *
     * @param hostelId The ID of the hostel
     */
    public void deleteAllImagesForHostel(Long hostelId) {
        logger.info("Deleting all images for hostel: hostelId={}", hostelId);

        List<HostelImage> images = imageRepository.findByHostelIdOrderByDisplayOrderAsc(hostelId);

        for (HostelImage image : images) {
            try {
                cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
                logger.debug("Deleted image from Cloudinary: publicId={}", image.getPublicId());
            } catch (IOException e) {
                logger.warn("Failed to delete image from Cloudinary: publicId={}, error={}",
                        image.getPublicId(), e.getMessage());
            }
        }

        imageRepository.deleteByHostelId(hostelId);
        logger.info("Deleted {} images for hostelId={}", images.size(), hostelId);
    }
}
