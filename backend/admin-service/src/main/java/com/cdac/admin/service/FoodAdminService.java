package com.cdac.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cdac.admin.client.FoodServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingFoodDto;

@Service
public class FoodAdminService {

	private final FoodServiceClient foodServiceClient;

	public FoodAdminService(FoodServiceClient foodServiceClient) {
		this.foodServiceClient = foodServiceClient;
	}

	public List<PendingFoodDto> getPendingFoods() {
		return foodServiceClient.getPendingFood();
	}

	public void approveFood(Long foodId) {
		foodServiceClient.approveFood(foodId);
	}

	public void rejectFood(Long foodId, String message) {
		foodServiceClient.rejectFood(foodId, message);
	}

	public List<PendingCategoryDto> getPendingCategories() {
		return foodServiceClient.getPendingCategories();
	}

	public void approveCategory(Long categoryId) {
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category id");
		}
		foodServiceClient.approveCategory(categoryId);
	}

	public void rejectCategory(Long categoryId, String reason) {
		if (categoryId == null || categoryId <= 0) {
			throw new IllegalArgumentException("Invalid category id");
		}
		if (reason == null || reason.trim().isEmpty()) {
			throw new IllegalArgumentException("Rejection reason is required");
		}
		foodServiceClient.rejectCategory(categoryId, reason);
	}

}
