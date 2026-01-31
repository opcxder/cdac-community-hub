package com.cdac.admin.client;

import java.util.List;

import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingFoodDto;

public interface FoodServiceClient {

	List<PendingFoodDto> getPendingFood();

	void approveFood(Long foodId);

	void rejectFood(Long foodId, String reason);

	List<PendingCategoryDto> getPendingCategories();

	void approveCategory(Long categoryId);

	void rejectCategory(Long categoryId, String reason);

}
