package com.cdac.suggestion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SubmissionResponse {

    private Long suggestionId;
    private String message;
}
