package com.cdac.hostel.dto;

public class RatingDTO {

    private Long ratingId;
    private Long hostelId;
    private Long userId;

    private Double cleanlinessRating;
    private Double foodQualityRating;
    private Double safetyRating;
    private Double locationRating;
    private Double affordabilityRating;

    private Double overallRating;
    private String reviewText;

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

    public Double getCleanlinessRating() {
        return cleanlinessRating;
    }

    public void setCleanlinessRating(Double cleanlinessRating) {
        this.cleanlinessRating = cleanlinessRating;
    }

    public Double getFoodQualityRating() {
        return foodQualityRating;
    }

    public void setFoodQualityRating(Double foodQualityRating) {
        this.foodQualityRating = foodQualityRating;
    }

    public Double getSafetyRating() {
        return safetyRating;
    }

    public void setSafetyRating(Double safetyRating) {
        this.safetyRating = safetyRating;
    }

    public Double getLocationRating() {
        return locationRating;
    }

    public void setLocationRating(Double locationRating) {
        this.locationRating = locationRating;
    }

    public Double getAffordabilityRating() {
        return affordabilityRating;
    }

    public void setAffordabilityRating(Double affordabilityRating) {
        this.affordabilityRating = affordabilityRating;
    }

    public Double getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Double overallRating) {
        this.overallRating = overallRating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
