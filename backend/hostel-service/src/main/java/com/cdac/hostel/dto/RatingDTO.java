package com.cdac.hostel.dto;

 
import lombok.Data;

@Data
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
}
