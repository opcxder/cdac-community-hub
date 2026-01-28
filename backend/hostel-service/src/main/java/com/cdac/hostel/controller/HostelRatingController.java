package com.cdac.hostel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.hostel.dto.MultiCriteriaRatingRequest;
import com.cdac.hostel.dto.RatingDTO;
import com.cdac.hostel.model.HostelRating;
import com.cdac.hostel.service.HostelRatingService;

@RestController
@RequestMapping("/api/hostels")
public class HostelRatingController {

    @Autowired
    private HostelRatingService ratingService;

    //1.Rating a Hostel -- Post Method
    @PostMapping("/{hostelId}/rate")
    public HostelRating rateHostel(
            @PathVariable Long hostelId,
            @RequestParam Long userId,
            @RequestBody MultiCriteriaRatingRequest request) {

        return ratingService.rateHostel(hostelId, userId, request);
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
