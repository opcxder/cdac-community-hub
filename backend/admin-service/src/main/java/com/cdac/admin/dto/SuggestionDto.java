package com.cdac.admin.dto;

public class SuggestionDto {

	private Long suggestionId;
	private String suggestionText;
	private Long userId;
	private String username;
	private String category;
	private String createdAt;
	
	public SuggestionDto() {
		super();
	}
	
	public SuggestionDto(Long suggestionId, String suggestionText, Long userId, String username, String category, String createdAt) {
		super();
		this.suggestionId = suggestionId;
		this.suggestionText = suggestionText;
		this.userId = userId;
		this.username = username;
		this.category = category;
		this.createdAt = createdAt;
	}

	public Long getSuggestionId() {
		return suggestionId;
	}

	public void setSuggestionId(Long suggestionId) {
		this.suggestionId = suggestionId;
	}

	public String getSuggestionText() {
		return suggestionText;
	}

	public void setSuggestionText(String suggestionText) {
		this.suggestionText = suggestionText;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
