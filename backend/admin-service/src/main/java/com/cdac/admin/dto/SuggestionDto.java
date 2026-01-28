package com.cdac.admin.dto;

public class SuggestionDto {

	private Long suggestionId;
	private String message;
	private String createdBy;
	private String createdAt;
	
	public SuggestionDto() {
		super();
	}
	
	public SuggestionDto(Long suggestionId, String message, String createdBy, String createdAt) {
		super();
		this.suggestionId = suggestionId;
		this.message = message;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
	}
	public Long getSuggestionId() {
		return suggestionId;
	}
	public void setSuggestionId(Long suggestionId) {
		this.suggestionId = suggestionId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
	
}
