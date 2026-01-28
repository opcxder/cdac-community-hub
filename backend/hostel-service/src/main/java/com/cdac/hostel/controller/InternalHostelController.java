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

    // ========== Hostel Admin Operations ==========

    /**
     * Retrieves all pending hostels awaiting admin approval.
     * Called by Admin Service.
     *
     * @return List of pending hostels
     */
    @GetMapping("/pending")
    public List<Hostel> getPendingHostels() {
        return hostelService.getPendingHostels();
    }

    /**
     * Approves a pending hostel, making it visible to public users.
     * Called by Admin Service after admin approval.
     *
     * @param hostelId The ID of the hostel to approve
     * @return The approved hostel entity
     */
    @PostMapping("/{hostelId}/approve")
    public Hostel approveHostel(@PathVariable Long hostelId) {
        return hostelService.approveHostel(hostelId);
    }

    /**
     * Rejects a pending hostel with an optional reason.
     * Called by Admin Service after admin rejection.
     *
     * @param hostelId The ID of the hostel to reject
     * @param reason Optional reason for rejection
     * @return The rejected hostel entity
     */
    @PostMapping("/{hostelId}/reject")
    public Hostel rejectHostel(
            @PathVariable Long hostelId,
            @RequestParam(required = false) String reason) {

        return hostelService.rejectHostel(hostelId, reason);
    }

    // ========== Category Admin Operations ==========

    /**
     * Retrieves all pending categories awaiting admin approval.
     * Called by Admin Service.
     *
     * @return List of pending categories
     */
    @GetMapping("/categories/pending")
    public List<HostelCategory> getPendingCategories() {
        return categoryService.getPendingCategories();
    }

    /**
     * Approves a pending category, making it available for use.
     * Called by Admin Service after admin approval.
     *
     * @param categoryId The ID of the category to approve
     * @return The approved category entity
     */
    @PostMapping("/categories/{categoryId}/approve")
    public HostelCategory approveCategory(@PathVariable Long categoryId) {
        return categoryService.approveCategory(categoryId);
    }

    /**
     * Rejects a pending category.
     * Called by Admin Service after admin rejection.
     *
     * @param categoryId The ID of the category to reject
     * @return The rejected category entity
     */
    @PostMapping("/categories/{categoryId}/reject")
    public HostelCategory rejectCategory(@PathVariable Long categoryId) {
        return categoryService.rejectCategory(categoryId);
    }
}


