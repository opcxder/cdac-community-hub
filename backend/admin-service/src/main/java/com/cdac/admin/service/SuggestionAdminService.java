package com.cdac.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cdac.admin.client.SuggestionServiceClient;
import com.cdac.admin.dto.SuggestionDto;

@Service
public class SuggestionAdminService {

	private final SuggestionServiceClient suggestionServiceClient;

	public SuggestionAdminService(SuggestionServiceClient suggestionServiceClient) {
	    this.suggestionServiceClient = suggestionServiceClient;
	}

	public List<SuggestionDto> getSuggestions(int page, int size) {
	    return suggestionServiceClient.getSuggestions(page, size);
	}

	public List<SuggestionDto> getSuggestionsByCategory(String category) {
	    return suggestionServiceClient.getSuggestionsByCategory(category);
	}



}
