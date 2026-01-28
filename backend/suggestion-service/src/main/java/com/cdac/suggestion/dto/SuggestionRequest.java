package com.cdac.suggestion.dto;

import com.cdac.suggestion.model.SuggestionCategory;
import lombok.Data;

@Data
public class SuggestionRequest {

    private String suggestionText;
    private SuggestionCategory category;
}
