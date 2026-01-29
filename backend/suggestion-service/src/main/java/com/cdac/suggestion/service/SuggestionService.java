package com.cdac.suggestion.service;

import com.cdac.suggestion.dto.SuggestionDTO;
import com.cdac.suggestion.dto.SuggestionRequest;
import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.model.Suggestion;
import com.cdac.suggestion.model.SuggestionCategory;
import com.cdac.suggestion.repository.SuggestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionService {

    private static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    @Autowired
    private SuggestionRepository repository;

    @Autowired
    private UserCacheService userCacheService;

    public Long submitSuggestion(Long userId, SuggestionRequest request) {

        Suggestion suggestion = new Suggestion();
        suggestion.setUserId(userId);
        suggestion.setSuggestionText(request.getSuggestionText());
        suggestion.setCategory(request.getCategory());

        Suggestion saved = repository.save(suggestion);

        userCacheService.getUserWithCache(userId);

        log.info("Suggestion submitted: suggestionId={}, userId={}, category={}",
                saved.getSuggestionId(), userId, request.getCategory());

        return saved.getSuggestionId();
    }

    public List<SuggestionDTO> getUserSuggestions(Long userId) {

        UserDTO user = userCacheService.getUserWithCache(userId);

        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(s -> SuggestionDTO.from(s, user.getUsername()))
                .collect(Collectors.toList());
    }

    public Page<SuggestionDTO> getAllSuggestions(Pageable pageable) {

        return repository.findAll(pageable)
                .map(s -> {
                    String username = userCacheService.getUserWithCache(s.getUserId()).getUsername();
                    return SuggestionDTO.from(s, username);
                });
    }

    public List<SuggestionDTO> getSuggestionsByCategory(SuggestionCategory category) {

        return repository.findByCategory(category).stream()
                .map(s -> {
                    String username = userCacheService.getUserWithCache(s.getUserId()).getUsername();
                    return SuggestionDTO.from(s, username);
                })
                .toList();
    }

}
