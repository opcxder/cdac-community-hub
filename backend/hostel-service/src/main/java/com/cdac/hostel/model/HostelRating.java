package com.cdac.hostel.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * Entity representing a multi-criteria rating for a hostel.
 * Each user can rate a hostel only once across 5 different criteria:
 * cleanliness, food quality, safety, location, and affordability.
 * The overall rating is calculated as the average of these 5 criteria.
 */
@Entity
@Table(name = "hostel_ratings", uniqueConstraints = @UniqueConstraint(name = "unique_user_hostel_rating", columnNames = {
        "hostelId", "userId" }))
public class HostelRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ratingId;

    @Column(nullable = false)
    private Long hostelId;

    @Column(nullable = false)
    private Long userId;

    // Multi-criteria ratings (each 1-5 stars)
    @Column(nullable = false)
    private Integer cleanlinessRating;

    @Column(nullable = false)
    private Integer foodQualityRating;

    @Column(nullable = false)
    private Integer safetyRating;

    @Column(nullable = false)
    private Integer locationRating;

    @Column(nullable = false)
    private Integer affordabilityRating;

    @Column(columnDefinition = "TEXT")
    private String reviewText;

    // Timestamps for tracking rating creation and updates
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Timestamp updatedAt;

    // Getters and Setters
    public Long getRatingId() {
        return ratingId;
    }

    public void setRatingId(Long ratingId) {
        this.ratingId = ratingId;
    }

    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(Integer cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public Integer getFoodQualityRating() {
        return foodQualityRating;
    }

    public void setFoodQualityRating(Integer foodQualityRating) {
        this.foodQualityRating = foodQualityRating;
    }

    public Integer getSafetyRating() {
        return safetyRating;
    }

    public void setSafetyRating(Integer safetyRating) {
        this.safetyRating = safetyRating;
    }

    public Integer getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(Integer locationRating) {
        this.locationRating = locationRating;
    }

    public Integer getAffordabilityRating() {
        return affordabilityRating;
    }

    public void setAffordabilityRating(Integer affordabilityRating) {
        this.affordabilityRating = affordabilityRating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
