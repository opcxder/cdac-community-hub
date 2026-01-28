package com.cdac.hostel.dto;

import lombok.Data;

/**
 * DTO representing a ranked hostel with its Bayesian average rating.
 * Used for displaying hostels sorted by their calculated ranking score.
 */
@Data
public class RankedHostelDTO {
    private Long hostelId;
    private String hostelName;
    private Double bayesianAverage;
    private Double simpleAverage;
    private Long ratingCount;
}
