package com.cdac.admin.client.mock;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.cdac.admin.client.SuggestionServiceClient;
import com.cdac.admin.dto.SuggestionDto;
@Service
@Profile("dev")
public class MockSuggestionServiceClient implements SuggestionServiceClient {

    private static final Logger log = LoggerFactory.getLogger(MockSuggestionServiceClient.class);

    @Override
    public List<SuggestionDto> getSuggestions(int page, int size) {
        log.info("Mock Suggestion Service: page={}, size={}", page, size);

        List<SuggestionDto> all = List.of(
            new SuggestionDto(1L, "first suggestion", "alphaking", "2025-05-03"),
            new SuggestionDto(2L, "second suggestion", "alphaqueen", "2025-05-02")
        );

        return all; 
    }

    @Override
    public List<SuggestionDto> getSuggestionsByCategory(String category) {
        log.info("Mock Suggestion Service: category={}", category);
        return List.of(); 
    }
}
