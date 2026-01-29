package com.cdac.hostel.dto;

public class MultiCriteriaRatingRequest {
    private Integer cleanlinessRating;
    private Integer foodQualityRating;
    private Integer safetyRating;
    private Integer locationRating;
    private Integer affordabilityRating;
    private String reviewText;

    // Getters and Setters
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
}
