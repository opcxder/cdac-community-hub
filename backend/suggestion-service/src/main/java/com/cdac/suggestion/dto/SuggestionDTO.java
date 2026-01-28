package com.cdac.suggestion.dto;

import com.cdac.suggestion.model.Suggestion;
import com.cdac.suggestion.model.SuggestionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SuggestionDTO {

    private Long suggestionId;
    private String suggestionText;
    private SuggestionCategory category;
    private LocalDateTime createdAt;
    private String username;

    public static SuggestionDTO from(Suggestion suggestion, String username) {
        return new SuggestionDTO(
                suggestion.getSuggestionId(),
                suggestion.getSuggestionText(),
                suggestion.getCategory(),
                suggestion.getCreatedAt(),
                username
        );
    }
}
