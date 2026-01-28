package com.cdac.food.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.food.dto.FoodPlaceDTO;
import com.cdac.food.dto.FoodPlaceRequest;
import com.cdac.food.service.FoodPlaceService;

import jakarta.validation.Valid;

/**
 * Controller for managing food places.
 * Exposes endpoints for creating, searching, and retrieving food places.
 */
@RestController
@RequestMapping("/api/food")
public class FoodPlaceController {

    private static final Logger logger = LoggerFactory.getLogger(FoodPlaceController.class);

    private final FoodPlaceService foodPlaceService;

    public FoodPlaceController(FoodPlaceService foodPlaceService) {
        this.foodPlaceService = foodPlaceService;
    }

    /**
     * Create a new food place.
     * 
     * @param request the food place creation request
     * @return the created food place (pending approval)
     */
    @PostMapping("/places")
    public ResponseEntity<FoodPlaceDTO> createFoodPlace(@Valid @RequestBody FoodPlaceRequest request) {
        logger.info("Request to create food place: {}, submittedBy: {}", request.getPlaceName(),
                request.getSubmittedByUserId());
        return ResponseEntity.ok(foodPlaceService.createFoodPlace(request));
    }

    /**
     * Get food places with search, filters, and pagination.
     * Only returns approved places.
     * 
     * @param page       page number (0-indexed, default 0)
     * @param size       page size (default 20)
     * @param search     search term for place name (optional)
     * @param priceRange price range filter (optional)
     * @param categoryId category filter (optional)
     * @return page of food places
     */
    @GetMapping("/places")
    public ResponseEntity<Page<FoodPlaceDTO>> getPlaces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String priceRange,
            @RequestParam(required = false) Long categoryId) {

        logger.info("Fetching food places: page={}, size={}, search={}, priceRange={}, categoryId={}",
                page, size, search, priceRange, categoryId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FoodPlaceDTO> places = foodPlaceService.searchPlaces(search, priceRange, categoryId, pageable);

        return ResponseEntity.ok(places);
    }

    /**
     * Get a food place by ID.
     * 
     * @param id the food place ID
     * @return the food place details
     */
    @GetMapping("/places/{id}")
    public ResponseEntity<FoodPlaceDTO> getFoodPlaceById(@PathVariable Long id) {
        logger.debug("Request to get food place by ID: {}", id);
        return ResponseEntity.ok(foodPlaceService.getFoodPlaceById(id));
    }
}
