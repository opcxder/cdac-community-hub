package com.cdac.hostel.model;

import java.math.BigDecimal;
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
 * Entity representing a hostel in the system.
 * Hostels are submitted by users and require admin approval before being visible to the public.
 * Supports detailed information including location, pricing, facilities, and room capacity.
 */
@Entity
@Data
@Table(name = "hostels")
public class Hostel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostelId;

    @Column(nullable = false, length = 200)
    private String hostelName;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Location Information
    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(length = 100)
    private String city;

    @Column(length = 200)
    private String locality;

    @Column(length = 200)
    private String landmark;

    @Column(length = 500)
    private String mapLocation;

    @Column(length = 50)
    private String distanceFromCdac;

    // Pricing Information
    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyRentMin;

    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyRentMax;

    // Facility Flags
    private Boolean hasWifi;
    private Boolean hasAc;
    private Boolean hasMess;
    private Boolean hasLaundry;

    // Room Capacity (e.g., 1, 2, 3, 5, 11 members per room)
    @Column(name = "room_capacity")
    private Integer roomCapacity;

    // Contact Information
    @Column(length = 100)
    private String contactPersonName;

    @Column(length = 15)
    private String contactPersonPhone;

    // Submission and Approval Tracking
    @Column(nullable = false)
    private Long submittedByUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HostelStatus status = HostelStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    // Timestamps
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;

    private Timestamp approvedAt;
}

