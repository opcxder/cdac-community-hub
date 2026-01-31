package com.cdac.suggestion.controller;

import com.cdac.suggestion.dto.SubmissionResponse;
import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.dto.SuggestionRequest;
import com.cdac.suggestion.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private static final Logger logger = LoggerFactory.getLogger(SuggestionController.class);

    @Autowired
    private SuggestionService suggestionService;

    @PostMapping
    public SubmissionResponse submitSuggestion(
            @RequestBody SuggestionRequest request,
            @RequestParam(required = false) Long userId) {

        logger.info("📝 Received suggestion submission request: suggestionText={}, category={}, userId={}",
                request.getSuggestionText(), request.getCategory(), userId);

        // Use userId from request body if available, otherwise from query param
        Long finalUserId = (userId != null) ? userId : 1L; // Fallback to 1L if not provided

        logger.info("📝 Processing suggestion with userId={}", finalUserId);

        Long id = suggestionService.submitSuggestion(finalUserId, request);

        logger.info("✅ Suggestion submitted successfully: suggestionId={}", id);

        return new SubmissionResponse(
                id,
                "Thank you for your feedback. Admin will review it.");
    }

    @GetMapping("/user")
    public List<SuggestionDTO> getUserSuggestions(@RequestParam(required = false) Long userId) {
        Long finalUserId = (userId != null) ? userId : 1L;
        return suggestionService.getUserSuggestions(finalUserId);
    }

    /**
     * Public endpoint for community to view all suggestions
     */
    @GetMapping
    public List<SuggestionDTO> getAllSuggestions() {
        logger.info("💡 [PUBLIC] Fetching all suggestions for community view");
        List<SuggestionDTO> suggestions = suggestionService.getAllSuggestions();
        logger.info("💡 [PUBLIC] Returning {} suggestions", suggestions.size());
        return suggestions;
    }
}
