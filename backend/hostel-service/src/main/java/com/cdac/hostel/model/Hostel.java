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

/**
 * Entity representing a hostel in the system.
 * Hostels are submitted by users and require admin approval before being
 * visible to the public.
 * Supports detailed information including location, pricing, facilities, and
 * room capacity.
 */
@Entity
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

    // Getters and Setters
    public Long getHostelId() {
        return hostelId;
    }

    public void setHostelId(Long hostelId) {
        this.hostelId = hostelId;
    }

    public String getHostelName() {
        return hostelName;
    }

    public void setHostelName(String hostelName) {
        this.hostelName = hostelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    public String getDistanceFromCdac() {
        return distanceFromCdac;
    }

    public void setDistanceFromCdac(String distanceFromCdac) {
        this.distanceFromCdac = distanceFromCdac;
    }

    public BigDecimal getMonthlyRentMin() {
        return monthlyRentMin;
    }

    public void setMonthlyRentMin(BigDecimal monthlyRentMin) {
        this.monthlyRentMin = monthlyRentMin;
    }

    public BigDecimal getMonthlyRentMax() {
        return monthlyRentMax;
    }

    public void setMonthlyRentMax(BigDecimal monthlyRentMax) {
        this.monthlyRentMax = monthlyRentMax;
    }

    public Boolean getHasWifi() {
        return hasWifi;
    }

    public void setHasWifi(Boolean hasWifi) {
        this.hasWifi = hasWifi;
    }

    public Boolean getHasAc() {
        return hasAc;
    }

    public void setHasAc(Boolean hasAc) {
        this.hasAc = hasAc;
    }

    public Boolean getHasMess() {
        return hasMess;
    }

    public void setHasMess(Boolean hasMess) {
        this.hasMess = hasMess;
    }

    public Boolean getHasLaundry() {
        return hasLaundry;
    }

    public void setHasLaundry(Boolean hasLaundry) {
        this.hasLaundry = hasLaundry;
    }

    public Integer getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public Long getSubmittedByUserId() {
        return submittedByUserId;
    }

    public void setSubmittedByUserId(Long submittedByUserId) {
        this.submittedByUserId = submittedByUserId;
    }

    public HostelStatus getStatus() {
        return status;
    }

    public void setStatus(HostelStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
    }
}
