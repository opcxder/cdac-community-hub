package com.cdac.hostel.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for hostel data including images and room types.
 * Used for admin panel and public display.
 */
public class HostelDTO {

    private Long hostelId;
    private String hostelName;
    private String description;
    private String address;
    private String city;
    private String locality;
    private String landmark;
    private String mapLocation;
    private String distanceFromCdac;
    private BigDecimal monthlyRentMin;
    private BigDecimal monthlyRentMax;
    private Boolean hasWifi;
    private Boolean hasAc;
    private Boolean hasMess;
    private Boolean hasLaundry;
    private String contactPersonName;
    private String contactPersonPhone;
    private Long submittedByUserId;
    private String status;
    private String rejectionReason;
    private List<String> imageUrls;
    private List<Integer> roomCapacities; // [1, 2, 3] for single, double, triple
    private List<String> categories;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Integer> getRoomCapacities() {
        return roomCapacities;
    }

    public void setRoomCapacities(List<Integer> roomCapacities) {
        this.roomCapacities = roomCapacities;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
