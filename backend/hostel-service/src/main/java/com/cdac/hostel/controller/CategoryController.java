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

import com.cdac.hostel.model.HostelCategory;
import com.cdac.hostel.service.CategoryService;

/**
 * REST controller for public category operations.
 * Handles category creation and retrieval for end users.
 * Authentication is handled by API Gateway, which forwards userId via X-User-Id header.
 */
@RestController
@RequestMapping("/api/hostel/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

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

    /**
     * Creates a new category submission.
     * Category will be in PENDING status until approved by admin.
     *
     * @param request Map containing "categoryName" field
     * @return The created category with 201 CREATED status
     */
    @PostMapping
    public ResponseEntity<?> createCategory(
            @RequestBody java.util.Map<String, String> request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for creating category");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }

        String categoryName = request.get("categoryName");
        logger.info("Request to create hostel category: {}, userId: {}", categoryName, userId);
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
        logger.info("🏠 [HOSTEL-CATEGORY-CONTROLLER] GET /api/hostel/categories - Fetching all approved categories");

        List<HostelCategory> categories = categoryService.getApprovedCategories();

        logger.info("📤 [HOSTEL-CATEGORY-CONTROLLER] Returning {} approved categories", categories.size());
        if (categories.isEmpty()) {
            logger.warn("⚠️  [HOSTEL-CATEGORY-CONTROLLER] No approved categories found in database!");
        }

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
