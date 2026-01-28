package com.cdac.food.dto;

import jakarta.validation.constraints.*;

/**
 * DTO for submitting a rating/review for a food place
 */
public class RatingRequest {
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;  // 1-5 stars
    
    @Size(max = 1000, message = "Review text must not exceed 1000 characters")
    private String reviewText;

    // Constructors
    public RatingRequest() {
    }

    public RatingRequest(Integer rating, String reviewText) {
        this.rating = rating;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
