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
import lombok.Data;

/**
 * Entity representing a multi-criteria rating for a hostel.
 * Each user can rate a hostel only once across 5 different criteria:
 * cleanliness, food quality, safety, location, and affordability.
 * The overall rating is calculated as the average of these 5 criteria.
 */
@Entity
@Data
@Table(
    name = "hostel_ratings",
    uniqueConstraints = @UniqueConstraint(
        name = "unique_user_hostel_rating",
        columnNames = {"hostelId", "userId"}
    )
)
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
}

