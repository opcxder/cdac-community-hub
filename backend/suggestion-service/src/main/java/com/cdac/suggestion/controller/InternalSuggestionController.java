package com.cdac.suggestion.controller;

import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.model.SuggestionCategory;
import com.cdac.suggestion.service.SuggestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/suggestions")
@Slf4j
public class InternalSuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping
    public ResponseEntity<Page<SuggestionDTO>> getAllSuggestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        log.info("Admin fetching all suggestions: page={}, size={}", page, size);

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<SuggestionDTO> suggestions =
                suggestionService.getAllSuggestions(pageable);

        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SuggestionDTO>> getSuggestionsByCategory(
            @PathVariable SuggestionCategory category) {

        log.info("Admin fetching suggestions by category: {}", category);

        List<SuggestionDTO> suggestions =
                suggestionService.getSuggestionsByCategory(category);

        return ResponseEntity.ok(suggestions);
    }
}
