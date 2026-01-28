package com.cdac.food.dto;

import com.cdac.food.model.PriceRange;
import jakarta.validation.constraints.*;
import java.util.List;

/**
 * DTO for creating/updating food places
 */
public class FoodPlaceRequest {
    
    @NotBlank(message = "Place name is required")
    @Size(max = 200, message = "Place name must not exceed 200 characters")
    private String placeName;
    
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Address is required")
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;
    
    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;
    
    @Size(max = 200, message = "Locality must not exceed 200 characters")
    private String locality;
    
    @Size(max = 200, message = "Landmark must not exceed 200 characters")
    private String landmark;
    
    @Size(max = 500, message = "Map location must not exceed 500 characters")
    private String mapLocation;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Contact info must contain only numbers, spaces, +, -, and parentheses")
    @Size(max = 15, message = "Contact info must not exceed 15 characters")
    private String contactInfo;
    
    @NotNull(message = "Price range is required")
    private PriceRange priceRange;
    
    @NotNull(message = "Best for category is required")
    private Long bestForCategoryId;
    
    @NotNull(message = "Submitted by user ID is required")
    private Long submittedByUserId;
    
    @NotEmpty(message = "At least one category must be selected")
    @Size(max = 10, message = "Maximum 10 categories allowed")
    private List<Long> categoryIds;

    // Constructors
    public FoodPlaceRequest() {
    }

    // Getters and Setters
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public Long getBestForCategoryId() {
        return bestForCategoryId;
    }

    public void setBestForCategoryId(Long bestForCategoryId) {
        this.bestForCategoryId = bestForCategoryId;
    }

    public Long getSubmittedByUserId() {
        return submittedByUserId;
    }

    public void setSubmittedByUserId(Long submittedByUserId) {
        this.submittedByUserId = submittedByUserId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
