package com.cdac.suggestion.controller;

import com.cdac.suggestion.dto.SubmissionResponse;
import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.dto.SuggestionRequest;
import com.cdac.suggestion.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    private static final Long USER_ID = 1L;

    @PostMapping
    public SubmissionResponse submitSuggestion(@RequestBody SuggestionRequest request) {

        Long id = suggestionService.submitSuggestion(USER_ID, request);

        return new SubmissionResponse(
                id,
                "Thank you for your feedback. Admin will review it."
        );
    }

    @GetMapping("/user")
    public List<SuggestionDTO> getUserSuggestions() {
        return suggestionService.getUserSuggestions(USER_ID);
    }
}
