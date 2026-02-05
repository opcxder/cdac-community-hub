package com.cdac.food.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for rating statistics of a food place.
 * Includes average rating, total count, and breakdown by star value (1-5).
 */
public class RatingStatsDTO {
    
    private Double averageRating;
    private Integer totalRatings;
    private Map<Integer, Integer> ratingBreakdown = new HashMap<>();

    // Constructors
    public RatingStatsDTO() {
    }

    public RatingStatsDTO(Double averageRating, Integer totalRatings, Map<Integer, Integer> ratingBreakdown) {
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.ratingBreakdown = ratingBreakdown;
    }

    // Getters and Setters
    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }

    public Map<Integer, Integer> getRatingBreakdown() {
        return ratingBreakdown;
    }

    public void setRatingBreakdown(Map<Integer, Integer> ratingBreakdown) {
        this.ratingBreakdown = ratingBreakdown;
    }
}
