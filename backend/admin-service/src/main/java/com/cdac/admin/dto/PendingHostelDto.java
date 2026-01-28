package com.cdac.admin.dto;

public class PendingHostelDto {
    
	 private Long hostelId;
	 private String name;
	 private String location;
	 private String status;
	 
	 public PendingHostelDto() {
		super();
	 }

	 public PendingHostelDto(Long hostelId, String name, String location, String status) {
		super();
		this.hostelId = hostelId;
		this.name = name;
		this.location = location;
		this.status = status;
	 }

	 public Long getHostelId() {
		 return hostelId;
	 }

	 public void setHostelId(Long hostelId) {
		 this.hostelId = hostelId;
	 }

	 public String getName() {
		 return name;
	 }

	 public void setName(String name) {
		 this.name = name;
	 }

	 public String getLocation() {
		 return location;
	 }

	 public void setLocation(String location) {
		 this.location = location;
	 }

	 public String getStatus() {
		 return status;
	 }

	 public void setStatus(String status) {
		 this.status = status;
	 }
	 
	 
	 
	 
	 
	
}
