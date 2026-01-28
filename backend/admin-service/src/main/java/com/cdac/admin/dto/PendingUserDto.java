package com.cdac.admin.dto;

public class PendingUserDto {
   
	
	private Long userId;
	private String username;
	private String email;
	private String status;
	public PendingUserDto() {
		super();
	}
	public PendingUserDto(Long userId, String username, String email, String status) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.status = status;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
