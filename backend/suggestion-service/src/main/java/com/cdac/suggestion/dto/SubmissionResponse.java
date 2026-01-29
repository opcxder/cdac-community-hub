package com.cdac.suggestion.dto;

public class SubmissionResponse {

    private Long suggestionId;
    private String message;

    public SubmissionResponse(Long suggestionId, String message) {
        this.suggestionId = suggestionId;
        this.message = message;
    }

    // Getters and Setters
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
}
