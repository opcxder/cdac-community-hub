package com.cdac.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PendingHostelDto {

	private Long hostelId;
	private String hostelName;
	private String description;
	private String address;
	private String city;
	private String locality;
	private String landmark;
	private String mapLocation;
	private String distanceFromCdac;
	private BigDecimal monthlyRentMin;
	private BigDecimal monthlyRentMax;
	private Integer roomCapacity;
	private Boolean hasWifi;
	private Boolean hasAc;
	private Boolean hasMess;
	private Boolean hasLaundry;
	private String contactPersonName;
	private String contactPersonPhone;
	private String status;
	private String rejectionReason;
	private Long submittedByUserId;
	private LocalDateTime createdAt;
	private List<String> categories;
	private List<String> imageUrls;

	public PendingHostelDto() {
		super();
	}
	
	

	// Getters and Setters
	public Long getHostelId() {
		return hostelId;
	}

	public void setHostelId(Long hostelId) {
		this.hostelId = hostelId;
	}

	public String getHostelName() {
		return hostelName;
	}

	public void setHostelName(String hostelName) {
		this.hostelName = hostelName;
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

	public String getDistanceFromCdac() {
		return distanceFromCdac;
	}

	public void setDistanceFromCdac(String distanceFromCdac) {
		this.distanceFromCdac = distanceFromCdac;
	}

	public BigDecimal getMonthlyRentMin() {
		return monthlyRentMin;
	}

	public void setMonthlyRentMin(BigDecimal monthlyRentMin) {
		this.monthlyRentMin = monthlyRentMin;
	}

	public BigDecimal getMonthlyRentMax() {
		return monthlyRentMax;
	}

	public void setMonthlyRentMax(BigDecimal monthlyRentMax) {
		this.monthlyRentMax = monthlyRentMax;
	}

	public Integer getRoomCapacity() {
		return roomCapacity;
	}

	public void setRoomCapacity(Integer roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public Boolean getHasWifi() {
		return hasWifi;
	}

	public void setHasWifi(Boolean hasWifi) {
		this.hasWifi = hasWifi;
	}

	public Boolean getHasAc() {
		return hasAc;
	}

	public void setHasAc(Boolean hasAc) {
		this.hasAc = hasAc;
	}

	public Boolean getHasMess() {
		return hasMess;
	}

	public void setHasMess(Boolean hasMess) {
		this.hasMess = hasMess;
	}

	public Boolean getHasLaundry() {
		return hasLaundry;
	}

	public void setHasLaundry(Boolean hasLaundry) {
		this.hasLaundry = hasLaundry;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonPhone() {
		return contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
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

	public Long getSubmittedByUserId() {
		return submittedByUserId;
	}

	public void setSubmittedByUserId(Long submittedByUserId) {
		this.submittedByUserId = submittedByUserId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	
	

	

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	@Override
	public String toString() {
		return "PendingHostelDto{" +
				"hostelId=" + hostelId +
				", hostelName='" + hostelName + '\'' +
				", description='" + description + '\'' +
				", address='" + address + '\'' +
				", city='" + city + '\'' +
				", locality='" + locality + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
