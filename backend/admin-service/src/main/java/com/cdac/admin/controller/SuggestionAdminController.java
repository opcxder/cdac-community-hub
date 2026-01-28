package com.cdac.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.admin.dto.SuggestionDto;
import com.cdac.admin.service.SuggestionAdminService;



@RestController
@RequestMapping("/api/admin/suggestions")
public class SuggestionAdminController {

    private final SuggestionAdminService suggestionAdminService;

    public SuggestionAdminController(SuggestionAdminService suggestionAdminService) {
        this.suggestionAdminService = suggestionAdminService;
    }

    @GetMapping
    public ResponseEntity<List<SuggestionDto>> getSuggestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(
            suggestionAdminService.getSuggestions(page, size)
        );
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<SuggestionDto>> getByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
            suggestionAdminService.getSuggestionsByCategory(category)
        );
    }
}
