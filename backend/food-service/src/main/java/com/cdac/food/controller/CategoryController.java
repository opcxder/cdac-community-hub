package com.cdac.food.controller;

import com.cdac.food.dto.CategoryDTO;
import com.cdac.food.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing food categories.
 * Exposes endpoints for creating and retrieving categories.
 */
@RestController
@RequestMapping("/api/food/categories")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

   
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            @RequestParam Long userId) {
        logger.info("Request to create category: {}, userId: {}", categoryDTO.getCategoryName(), userId);
        // Assuming the DTO comes with a name, but for creation we might just need name
        // and userId.
        // The Service takes (String name, Long userId).
        // Let's assume the body has the name.
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
