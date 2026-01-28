package com.cdac.hostel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.hostel.model.HostelCategory;
import com.cdac.hostel.service.CategoryService;

/**
 * REST controller for public category operations.
 * Handles category creation and retrieval for end users.
 */
@RestController
@RequestMapping("/api/hostel/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Creates a new category submission.
     * Category will be in PENDING status until approved by admin.
     *
     * @param request Map containing "categoryName" field
     * @param userId ID of the user creating the category
     * @return The created category with 201 CREATED status
     */
    @PostMapping
    public ResponseEntity<HostelCategory> createCategory(
            @RequestBody java.util.Map<String, String> request,
            @RequestParam Long userId) {

        String categoryName = request.get("categoryName");
        HostelCategory category = categoryService.createCategory(categoryName, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    /**
     * Retrieves all approved categories.
     * Only approved categories are visible to public users.
     *
     * @return List of approved categories
     */
    @GetMapping
    public ResponseEntity<List<HostelCategory>> getApprovedCategories() {
        List<HostelCategory> categories = categoryService.getApprovedCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Retrieves a single category by ID.
     *
     * @param categoryId The ID of the category
     * @return The category entity
     */
    @GetMapping("/{categoryId}")
    public ResponseEntity<HostelCategory> getCategoryById(@PathVariable Long categoryId) {
        HostelCategory category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok(category);
    }
}
