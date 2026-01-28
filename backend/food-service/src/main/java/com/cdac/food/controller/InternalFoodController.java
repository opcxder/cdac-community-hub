package com.cdac.food.controller;

import com.cdac.food.dto.CategoryDTO;
import com.cdac.food.dto.FoodPlaceDTO;
import com.cdac.food.service.CategoryService;
import com.cdac.food.service.FoodPlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for internal/admin operations.
 * Exposes endpoints for Admin Service to manage approvals.
 * These endpoints should typically be secured or internal-only.
 */
@RestController
@RequestMapping("/internal")
public class InternalFoodController {

    private static final Logger logger = LoggerFactory.getLogger(InternalFoodController.class);

    private final FoodPlaceService foodPlaceService;
    private final CategoryService categoryService;

    public InternalFoodController(FoodPlaceService foodPlaceService, CategoryService categoryService) {
        this.foodPlaceService = foodPlaceService;
        this.categoryService = categoryService;
    }

    // --- Food Place Admin Operations ---

    @GetMapping("/places/pending")
    public ResponseEntity<List<FoodPlaceDTO>> getPendingPlaces() {
        logger.info("Admin request: get pending food places");
        return ResponseEntity.ok(foodPlaceService.getPendingPlaces());
    }

    @PostMapping("/places/{id}/approve")
    public ResponseEntity<FoodPlaceDTO> approvePlace(@PathVariable Long id) {
        logger.info("Admin request: approve food place {}", id);
        return ResponseEntity.ok(foodPlaceService.approvePlace(id));
    }

    @PostMapping("/places/{id}/reject")
    public ResponseEntity<FoodPlaceDTO> rejectPlace(
            @PathVariable Long id, 
            @RequestBody Map<String, String> body) {
        String reason = body.getOrDefault("reason", "Rejected by admin");
        logger.info("Admin request: reject food place {} with reason: {}", id, reason);
        return ResponseEntity.ok(foodPlaceService.rejectPlace(id, reason));
    }

    @GetMapping("/places/{id}")
    public ResponseEntity<FoodPlaceDTO> getFoodPlace(@PathVariable Long id) {
        logger.debug("Internal request: get food place {}", id);
        // This might return even pending/rejected places if needed for admin view
        return ResponseEntity.ok(foodPlaceService.getFoodPlaceById(id));
    }

    // --- Category Admin Operations ---

    @GetMapping("/categories/pending")
    public ResponseEntity<List<CategoryDTO>> getPendingCategories() {
        logger.info("Admin request: get pending categories");
        return ResponseEntity.ok(categoryService.getPendingCategories());
    }

    @PostMapping("/categories/{id}/approve")
    public ResponseEntity<CategoryDTO> approveCategory(@PathVariable Long id) {
        logger.info("Admin request: approve category {}", id);
        return ResponseEntity.ok(categoryService.approveCategory(id));
    }

    @PostMapping("/categories/{id}/reject")
    public ResponseEntity<CategoryDTO> rejectCategory(@PathVariable Long id) {
        logger.info("Admin request: reject category {}", id);
        return ResponseEntity.ok(categoryService.rejectCategory(id));
    }
}
