package com.cdac.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingFoodDto;
import com.cdac.admin.dto.RejectFoodDto;
import com.cdac.admin.service.FoodAdminService;

@RestController
@RequestMapping("/api/admin/foods")
public class FoodAdminController {

	private final FoodAdminService foodAdminService;

	public FoodAdminController(FoodAdminService foodAdminService) {
		this.foodAdminService = foodAdminService;
	}

	@GetMapping("/pending")
	public ResponseEntity<List<PendingFoodDto>> getPendingFood() {
		List<PendingFoodDto> pendingFoods = foodAdminService.getPendingFoods();
		return ResponseEntity.ok(pendingFoods);
	}

	@PostMapping("/{id}/approve")
	public ResponseEntity<Void> approveFood(@PathVariable("id") Long foodId) {
		foodAdminService.approveFood(foodId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/reject")
	public ResponseEntity<Void> rejectFood(@PathVariable("id") Long id, @RequestBody RejectFoodDto rejectFood) {
		foodAdminService.rejectFood(id, rejectFood.getFoodReason());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/categories/pending")
	public ResponseEntity<List<PendingCategoryDto>> getPendingCategories() {
		return ResponseEntity.ok(foodAdminService.getPendingCategories());
	}

	@PostMapping("/categories/{id}/approve")
	public ResponseEntity<Void> approveCategory(@PathVariable Long id) {
		foodAdminService.approveCategory(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/categories/{id}/reject")
	public ResponseEntity<Void> rejectCategory(
			@PathVariable Long id,
			@RequestBody com.cdac.admin.dto.RejectCategoryDto dto) {
		foodAdminService.rejectCategory(id, dto.getReason());
		return ResponseEntity.ok().build();
	}

}
