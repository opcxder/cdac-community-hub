package com.cdac.hostel.model;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a hostel category.
 * Categories are user-submitted and require admin approval before being visible.
 * Examples: PG, Hostel, Boys Only, Girls Only, Co-living
 * Users can create new categories dynamically while submitting hostels.
 */
@Entity
@Data
@Table(name = "hostel_categories")
public class HostelCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true, nullable = false, length = 100)
    private String categoryName;

    /**
     * ID of the user who created this category.
     * No foreign key constraint as user data is in separate auth_db database.
     */
    private Long createdByUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CategoryStatus status = CategoryStatus.PENDING;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    private Timestamp approvedAt;
}
