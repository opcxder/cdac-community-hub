package com.cdac.hostel.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing an image associated with a hostel.
 * Each hostel can have 1-5 images stored in Cloudinary.
 * Only the Cloudinary URL is stored in the database, not the actual image file.
 */
@Entity
@Table(name = "hostel_images")
public class HostelImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false)
    private Long hostelId;

    /**
     * Cloudinary URL of the uploaded image.
     * Format: https://res.cloudinary.com/{cloud_name}/image/upload/{public_id}
     */
    @Column(nullable = false, length = 500)
    private String imageUrl;

    /**
     * Cloudinary public ID for the image.
     * Used for deletion and transformation operations.
     */
    @Column(nullable = false, length = 255)
    private String publicId;

    /**
     * Display order of the image (1-5).
     * Lower numbers appear first in the gallery.
     */
    @Column(nullable = false)
    private Integer displayOrder;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp uploadedAt;

    // Getters and Setters
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Timestamp getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
