package com.cdac.hostel.dto;

/**
 * DTO representing a ranked hostel with its Bayesian average rating.
 * Used for displaying hostels sorted by their calculated ranking score.
 */
public class RankedHostelDTO {
    private Long hostelId;
    private String hostelName;
    private Double bayesianAverage;
    private Double simpleAverage;
    private Long ratingCount;

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

    public Double getBayesianAverage() {
        return bayesianAverage;
    }

    public void setBayesianAverage(Double bayesianAverage) {
        this.bayesianAverage = bayesianAverage;
    }

    public Double getSimpleAverage() {
        return simpleAverage;
    }

    public void setSimpleAverage(Double simpleAverage) {
        this.simpleAverage = simpleAverage;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }
}
