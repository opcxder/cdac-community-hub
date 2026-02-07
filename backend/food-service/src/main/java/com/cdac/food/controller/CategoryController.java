package com.cdac.food.controller;

import com.cdac.food.dto.CategoryDTO;
import com.cdac.food.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing food categories.
 * Exposes endpoints for creating and retrieving categories.
 * Authentication is handled by API Gateway, which forwards userId via X-User-Id header.
 */
@RestController
@RequestMapping("/api/food/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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

   
    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        
        Long userId = parseUserId(userIdHeader);
        if (userId == null) {
            logger.warn("⚠️ No userId in X-User-Id header for creating category");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication required"));
        }
        
        logger.info("Request to create category: {}, userId: {}", categoryDTO.getCategoryName(), userId);
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO.getCategoryName(), userId));
    }

   
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllApprovedCategories() {
        logger.info("📥 [CATEGORY-CONTROLLER] GET /api/food/categories - Fetching all approved categories");

        List<CategoryDTO> categories = categoryService.getAllApprovedCategories();

        logger.info("📤 [CATEGORY-CONTROLLER] Returning {} approved categories", categories.size());
        if (categories.isEmpty()) {
            logger.warn("⚠️  [CATEGORY-CONTROLLER] No approved categories found in database!");
        } else {
            logger.debug("📋 [CATEGORY-CONTROLLER] Categories: {}",
                    categories.stream().map(CategoryDTO::getCategoryName).toList());
        }

        return ResponseEntity.ok(categories);
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.debug("Request to get category by ID: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
