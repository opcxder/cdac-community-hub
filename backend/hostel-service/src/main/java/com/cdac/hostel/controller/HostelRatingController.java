package com.cdac.hostel.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.hostel.dto.MultiCriteriaRatingRequest;
import com.cdac.hostel.dto.RatingDTO;
import com.cdac.hostel.model.HostelRating;
import com.cdac.hostel.service.HostelRatingService;

/**
 * Controller for hostel ratings.
 * Authentication is handled by API Gateway, which forwards userId via X-User-Id header.
 */
@RestController
@RequestMapping("/api/hostel/hostels")
public class HostelRatingController {

    private static final Logger logger = LoggerFactory.getLogger(HostelRatingController.class);

    @Autowired
    private HostelRatingService ratingService;

    /**
     * Parse userId from X-User-Id header
     */
    private Long parseUserId(String userIdHeader) {
        if (userIdHeader == null || userIdHeader.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(userIdHeader);
        } catch (NumberFormatException e) {
            logger.error("Invalid X-User-Id header: {}", userIdHeader);
            return null;
        }
    }

    //1.Rating a Hostel -- Post Method
    @PostMapping("/{hostelId}/rate")
    public ResponseEntity<?> rateHostel(
            @PathVariable Long hostelId,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestBody MultiCriteriaRatingRequest request) {

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for rating hostel: {}", hostelId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        logger.info("Request to rate hostel: hostelId={}, userId={}", hostelId, userId);
        return ResponseEntity.ok(ratingService.rateHostel(hostelId, userId, request));
    }
    
    
 // 2. VIEW ALL RATINGS OF A HOSTEL
    @GetMapping("/{hostelId}/ratings")
    public List<HostelRating> getRatingsByHostel(
            @PathVariable Long hostelId) {

        return ratingService.getRatingsByHostel(hostelId);
    }

	
	
	  // 3️ VIEW AVERAGE / SUMMARY RATING OF A HOSTEL
	  
	  @GetMapping("/{hostelId}/ratings/summary") 
	  public RatingDTO getRatingSummary(@PathVariable Long hostelId) {
	  
	  return ratingService.getRatingSummary(hostelId); 
	  }
	 
	 
    
}
