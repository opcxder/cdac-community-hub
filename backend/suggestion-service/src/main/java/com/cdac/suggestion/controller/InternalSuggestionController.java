package com.cdac.suggestion.controller;

import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.model.SuggestionCategory;
import com.cdac.suggestion.service.SuggestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/suggestions")
public class InternalSuggestionController {

        private static final Logger log = LoggerFactory.getLogger(InternalSuggestionController.class);

        @Autowired
        private SuggestionService suggestionService;

        @GetMapping
        public ResponseEntity<List<SuggestionDTO>> getAllSuggestions() {

                log.info("💡 [SUGGESTION-SERVICE] Admin fetching all suggestions");
                
                // Get all suggestions sorted by createdAt descending
                List<SuggestionDTO> suggestions = suggestionService.getAllSuggestionsList();
                
                log.info("💡 [SUGGESTION-SERVICE] Returning {} suggestions", suggestions.size());
                if (!suggestions.isEmpty()) {
                        SuggestionDTO sample = suggestions.get(0);
                        log.info("💡 [SUGGESTION-SERVICE] Sample DTO: suggestionId={}, suggestionText={}, userId={}, username={}, category={}, createdAt={}", 
                                sample.getSuggestionId(), 
                                sample.getSuggestionText(), 
                                sample.getUserId(), 
                                sample.getUsername(),
                                sample.getCategory(),
                                sample.getCreatedAt());
                }
                return ResponseEntity.ok(suggestions);
        }

        @GetMapping("/category/{category}")
        public ResponseEntity<List<SuggestionDTO>> getSuggestionsByCategory(
                        @PathVariable SuggestionCategory category) {

                log.info("Admin fetching suggestions by category: {}", category);

                List<SuggestionDTO> suggestions = suggestionService.getSuggestionsByCategory(category);

                return ResponseEntity.ok(suggestions);
        }
}
