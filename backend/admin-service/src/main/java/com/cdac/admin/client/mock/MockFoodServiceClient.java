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
		log.info("Mock: Fetching the pending food" );
		
		List<PendingFoodDto> food = new ArrayList<>();
		food.add(new PendingFoodDto(1L , "Pista Hourse" , "Best Biryani" , "PENDING"));
		food.add(new PendingFoodDto(2L , "Amruttulya" , "Best Chai" , "PENDING"));
		food.add(new PendingFoodDto(3L , "Laxmi Marrt" , "Everyday Things" , "PENDING"));
		return food;
	}

	@Override
	public void approveFood(Long foodId) {
		log.info("Mock : Approving food id: {} ", foodId );
		
	}

	@Override
	public void rejectFood(Long foodId, String reason) {
		log.info("Mock: rejecting the food with id {} for {}" , foodId , reason);
		
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


}
