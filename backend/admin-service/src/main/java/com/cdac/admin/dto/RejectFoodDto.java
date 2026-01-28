package com.cdac.admin.dto;

public class RejectFoodDto {
     private String foodReason;

	 public RejectFoodDto() {
		super();
	 }

	 public RejectFoodDto(String foodReason) {
		super();
		this.foodReason = foodReason;
	 }

	 public String getFoodReason() {
		 return foodReason;
	 }

	 public void setFoodReason(String foodReason) {
		 this.foodReason = foodReason;
	 }
     
     
}
