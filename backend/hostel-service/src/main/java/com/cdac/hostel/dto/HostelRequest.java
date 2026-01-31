package com.cdac.hostel.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for hostel submission requests from frontend.
 * Maps frontend field names to backend Hostel entity.
 */
public class HostelRequest {

    private String hostelName;
    private String description;
    private String address;
    private String city;
    private String locality;
    private String landmark;
    private String mapLocation;
    private String distanceFromCDAC; // Frontend sends this
    private BigDecimal monthlyRentMin;
    private BigDecimal monthlyRentMax;
    private String contactPersonName;
    private String contactPhone; // Frontend sends this
    private List<String> facilities; // ["wifi", "ac", "mess", "laundry"]
    private List<String> roomTypes; // ["single", "double", "triple", "quad"]
    private Long categoryId;

    // Getters and Setters
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

    public String getDistanceFromCDAC() {
        return distanceFromCDAC;
    }

    public void setDistanceFromCDAC(String distanceFromCDAC) {
        this.distanceFromCDAC = distanceFromCDAC;
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

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<String> facilities) {
        this.facilities = facilities;
    }

    public List<String> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<String> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
