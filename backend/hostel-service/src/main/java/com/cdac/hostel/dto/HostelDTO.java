package com.cdac.hostel.dto;

 
import java.math.BigDecimal;
import lombok.Data;

@Data
public class HostelDTO {

    private Long hostelId;
    private String hostelName;
    private String description;
    private String address;
    private String city;
    private String locality;

    private BigDecimal monthlyRentMin;
    private BigDecimal monthlyRentMax;

    private Boolean hasWifi;
    private Boolean hasAc;
    private Boolean hasMess;
    private Boolean hasLaundry;

    private Double overallRating;
    private Long ratingCount;
}
