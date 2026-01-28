package com.cdac.admin.dto;

public class PendingFoodDto {
     
	private Long foodId;
	private String foodName;
	private String foodCategory;
	private String foodStatus;
	
	public PendingFoodDto() {
		super();
	}

	public PendingFoodDto(Long foodId, String foodName, String foodCategory, String foodStatus) {
		super();
		this.foodId = foodId;
		this.foodName = foodName;
		this.foodCategory = foodCategory;
		this.foodStatus = foodStatus;
	}

	public Long getFoodId() {
		return foodId;
	}

	public void setFoodId(Long foodId) {
		this.foodId = foodId;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public String getFoodCategory() {
		return foodCategory;
	}

	public void setFoodCategory(String foodCategory) {
		this.foodCategory = foodCategory;
	}

	public String getFoodStatus() {
		return foodStatus;
	}

	public void setFoodStatus(String foodStatus) {
		this.foodStatus = foodStatus;
	}
	
	
	
	
	
}
