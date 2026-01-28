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

    /**
     * Create a new category.
     * 
     * @param categoryDTO the category data
     * @param userId the ID of the user creating the category (simulated from token or request)
     * @return the created category (pending approval)
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO, 
            @RequestParam Long userId) {
        logger.info("Request to create category: {}, userId: {}", categoryDTO.getCategoryName(), userId);
        // Assuming the DTO comes with a name, but for creation we might just need name and userId.
        // The Service takes (String name, Long userId).
        // Let's assume the body has the name.
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO.getCategoryName(), userId));
    }

    /**
     * Get all approved categories.
     * Public endpoint.
     * 
     * @return list of approved categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllApprovedCategories() {
        logger.debug("Request to get all approved categories");
        return ResponseEntity.ok(categoryService.getAllApprovedCategories());
    }

    /**
     * Get a category by ID.
     * 
     * @param id the category ID
     * @return the category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        logger.debug("Request to get category by ID: {}", id);
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
}
