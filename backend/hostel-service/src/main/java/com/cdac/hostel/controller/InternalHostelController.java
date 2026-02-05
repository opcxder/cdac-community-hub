package com.cdac.hostel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cdac.hostel.model.Hostel;
import com.cdac.hostel.model.HostelCategory;
import com.cdac.hostel.service.CategoryService;
import com.cdac.hostel.service.HostelService;

/**
 * REST controller for internal admin operations.
 * These endpoints are called by the Admin Service for approval workflows.
 * Not meant for direct public access.
 */
@RestController
@RequestMapping("/internal/hostels")
public class InternalHostelController {

    @Autowired
    private HostelService hostelService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/pending")
    public List<com.cdac.hostel.dto.HostelDTO> getPendingHostels() {
        return hostelService.getPendingHostels();
    }

 
    @PostMapping("/{hostelId}/approve")
    public Hostel approveHostel(@PathVariable Long hostelId) {
        return hostelService.approveHostel(hostelId);
    }

    
    @PostMapping("/{hostelId}/reject")
    public Hostel rejectHostel(
            @PathVariable Long hostelId,
            @RequestParam(required = false) String reason) {

        return hostelService.rejectHostel(hostelId, reason);
    }


    @GetMapping("/categories/pending")
    public List<HostelCategory> getPendingCategories() {
        return categoryService.getPendingCategories();
    }


    @PostMapping("/categories/{categoryId}/approve")
    public HostelCategory approveCategory(@PathVariable Long categoryId) {
        return categoryService.approveCategory(categoryId);
    }

       @PostMapping("/categories/{categoryId}/reject")
    public HostelCategory rejectCategory(
            @PathVariable Long categoryId,
            @RequestBody java.util.Map<String, String> body) {
        String reason = body.getOrDefault("reason", "Rejected by admin");
        return categoryService.rejectCategory(categoryId, reason);
    }
}
