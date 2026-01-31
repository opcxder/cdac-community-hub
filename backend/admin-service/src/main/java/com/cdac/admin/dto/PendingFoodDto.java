package com.cdac.admin.dto;

import java.util.List;

/**
 * DTO for pending food places from food-service.
 * Must match FoodPlaceDTO structure exactly for proper deserialization.
 */
public class PendingFoodDto {

	private Long placeId;
	private String placeName;
	private String description;
	private String address;
	private String city;
	private String locality;
	private String landmark;
	private String mapLocation;
	private String contactInfo;
	private String priceRange; // String representation of PriceRange enum
	private Long bestForCategoryId;
	private Long submittedByUserId;
	private String status; // String representation of ApprovalStatus enum
	private String rejectionReason;
	private List<String> imageUrls;
	private List<String> categories; // Category names only
	private Double averageRating;

	public PendingFoodDto(long l, String string, String string2, String string3) {
		super();
	}

	// Getters and Setters
	public Long getPlaceId() {
		return placeId;
	}

	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getMapLocation() {
		return mapLocation;
	}

	public void setMapLocation(String mapLocation) {
		this.mapLocation = mapLocation;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public Long getBestForCategoryId() {
		return bestForCategoryId;
	}

	public void setBestForCategoryId(Long bestForCategoryId) {
		this.bestForCategoryId = bestForCategoryId;
	}

	public Long getSubmittedByUserId() {
		return submittedByUserId;
	}

	public void setSubmittedByUserId(Long submittedByUserId) {
		this.submittedByUserId = submittedByUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Double averageRating) {
		this.averageRating = averageRating;
	}

	@Override
	public String toString() {
		return "PendingFoodDto{" +
				"placeId=" + placeId +
				", placeName='" + placeName + '\'' +
				", description='" + description + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", locality='" + locality + '\'' +
				", status='" + status + '\'' +
				", imageUrls=" + (imageUrls != null ? imageUrls.size() + " images" : "no images") +
				'}';
	}
}
