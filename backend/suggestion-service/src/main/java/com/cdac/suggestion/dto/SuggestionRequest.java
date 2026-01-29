package com.cdac.suggestion.dto;

import com.cdac.suggestion.model.SuggestionCategory;

public class SuggestionRequest {

    private String suggestionText;
    private SuggestionCategory category;

    // Getters and Setters
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
}
