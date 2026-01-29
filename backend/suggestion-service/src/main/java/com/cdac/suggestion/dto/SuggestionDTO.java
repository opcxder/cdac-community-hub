package com.cdac.suggestion.dto;

import com.cdac.suggestion.model.Suggestion;
import com.cdac.suggestion.model.SuggestionCategory;

import java.time.LocalDateTime;

public class SuggestionDTO {

    private Long suggestionId;
    private String suggestionText;
    private SuggestionCategory category;
    private LocalDateTime createdAt;
    private String username;

    public SuggestionDTO(Long suggestionId, String suggestionText, SuggestionCategory category, LocalDateTime createdAt,
            String username) {
        this.suggestionId = suggestionId;
        this.suggestionText = suggestionText;
        this.category = category;
        this.createdAt = createdAt;
        this.username = username;
    }

    public static SuggestionDTO from(Suggestion suggestion, String username) {
        return new SuggestionDTO(
                suggestion.getSuggestionId(),
                suggestion.getSuggestionText(),
                suggestion.getCategory(),
                suggestion.getCreatedAt(),
                username);
    }

    // Getters and Setters
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

    public SuggestionCategory getCategory() {
        return category;
    }

    public void setCategory(SuggestionCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
