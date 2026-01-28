package com.cdac.hostel.dto;

import lombok.Data;

@Data
public class MultiCriteriaRatingRequest {
    private Integer cleanlinessRating;
    private Integer foodQualityRating;
    private Integer safetyRating;
    private Integer locationRating;
    private Integer affordabilityRating;
    private String reviewText;
}
