package com.cdac.admin.client.mock;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.cdac.admin.client.FoodServiceClient;
import com.cdac.admin.dto.PendingCategoryDto;
import com.cdac.admin.dto.PendingFoodDto;

@Service
@Profile("dev")
public class MockFoodServiceClient implements FoodServiceClient {

	private static final Logger log = LoggerFactory.getLogger(MockFoodServiceClient.class);

	@Override
	public List<PendingFoodDto> getPendingFood() {
		log.info("Mock: Fetching the pending food");

		List<PendingFoodDto> food = new ArrayList<>();
		
		// Food 1: Pista House
		PendingFoodDto food1 = new PendingFoodDto();
		food1.setPlaceId(1L);
		food1.setPlaceName("Pista House");
		food1.setDescription("Best Biryani in town with authentic Hyderabadi flavors");
		food1.setAddress("MG Road, Koregaon Park");
		food1.setCity("Pune");
		food1.setLocality("Koregaon Park");
		food1.setLandmark("Near CDAC Main Gate");
		food1.setPriceRange("MODERATE");
		food1.setStatus("PENDING");
		food1.setSubmittedByUserId(101L);
		food1.setImageUrls(List.of("https://via.placeholder.com/400x300?text=Pista+House"));
		food1.setCategories(List.of("Biryani", "Indian", "Non-Veg"));
		food.add(food1);
		
		// Food 2: Amrutulya
		PendingFoodDto food2 = new PendingFoodDto();
		food2.setPlaceId(2L);
		food2.setPlaceName("Amrutulya");
		food2.setDescription("Best Chai and snacks spot for students");
		food2.setAddress("University Road");
		food2.setCity("Pune");
		food2.setLocality("Aundh");
		food2.setLandmark("Opposite CDAC Campus");
		food2.setPriceRange("BUDGET");
		food2.setStatus("PENDING");
		food2.setSubmittedByUserId(102L);
		food2.setImageUrls(List.of("https://via.placeholder.com/400x300?text=Amrutulya"));
		food2.setCategories(List.of("Chai", "Snacks", "Fast Food"));
		food.add(food2);
		
		// Food 3: Laxmi Mart
		PendingFoodDto food3 = new PendingFoodDto();
		food3.setPlaceId(3L);
		food3.setPlaceName("Laxmi Mart");
		food3.setDescription("Grocery and everyday essentials");
		food3.setAddress("Housing Board Colony");
		food3.setCity("Pune");
		food3.setLocality("Aundh");
		food3.setPriceRange("BUDGET");
		food3.setStatus("PENDING");
		food3.setSubmittedByUserId(103L);
		food3.setImageUrls(List.of("https://via.placeholder.com/400x300?text=Laxmi+Mart"));
		food3.setCategories(List.of("Grocery", "General Store"));
		food.add(food3);
		
		return food;
	}

	@Override
	public void approveFood(Long foodId) {
		log.info("Mock : Approving food id: {} ", foodId);

	}

	@Override
	public void rejectFood(Long foodId, String reason) {
		log.info("Mock: rejecting the food with id {} for {}", foodId, reason);

	}

	@Override
	public List<PendingCategoryDto> getPendingCategories() {
		log.info("Mock: fetching pending food categories");

		List<PendingCategoryDto> categories = new ArrayList<>();
		categories.add(new PendingCategoryDto(1L, "Biryani", "PENDING"));
		categories.add(new PendingCategoryDto(2L, "Snacks", "PENDING"));
		return categories;
	}

	@Override
	public void approveCategory(Long categoryId) {
		log.info("Mock: approved category {}", categoryId);
	}

	@Override
	public void rejectCategory(Long categoryId, String reason) {
		log.info("Mock: rejected category {} with reason: {}", categoryId, reason);
	}

}
