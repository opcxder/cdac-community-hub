package com.cdac.suggestion.controller;

import com.cdac.suggestion.dto.SubmissionResponse;
import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.dto.SuggestionRequest;
import com.cdac.suggestion.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private static final Logger logger = LoggerFactory.getLogger(SuggestionController.class);

    @Autowired
    private SuggestionService suggestionService;

    @PostMapping
    public ResponseEntity<?> submitSuggestion(
            @RequestBody SuggestionRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {

        logger.info("📝 Received suggestion submission: suggestionText={}, category={}", 
                request.getSuggestionText(), request.getCategory());

        // Extract userId from X-User-Id header (set by API Gateway)
        Long userId = null;
        if (userIdHeader != null) {
            try {
                userId = Long.parseLong(userIdHeader);
                logger.info("📝 Received userId from Gateway: {}", userId);
            } catch (NumberFormatException e) {
                logger.error("❌ Invalid X-User-Id header: {}", userIdHeader);
            }
        }

        if (userId == null) {
            logger.warn("⚠️ No userId found in X-User-Id header (Gateway authentication failed)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new SubmissionResponse(null, "Authentication required"));
        }

        logger.info("📝 Processing suggestion with userId={}", userId);

        Long id = suggestionService.submitSuggestion(userId, request);

        logger.info("✅ Suggestion submitted successfully: suggestionId={}, userId={}", id, userId);

        return ResponseEntity.ok(new SubmissionResponse(
                id,
                "Thank you for your feedback. Admin will review it."));
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
        
        if (!suggestions.isEmpty()) {
            SuggestionDTO sample = suggestions.get(0);
            logger.info("💡 [PUBLIC] Sample suggestion: suggestionId={}, suggestionText={}, userId={}, username={}, category={}", 
                sample.getSuggestionId(), 
                sample.getSuggestionText(), 
                sample.getUserId(),
                sample.getUsername(),
                sample.getCategory());
        }
        
        return suggestions;
    }
}
