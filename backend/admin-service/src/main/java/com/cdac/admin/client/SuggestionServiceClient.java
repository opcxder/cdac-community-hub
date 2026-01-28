package com.cdac.admin.client;

import java.util.List;

import com.cdac.admin.dto.SuggestionDto;

public interface SuggestionServiceClient {

	List<SuggestionDto> getSuggestions(int page, int size);
	List<SuggestionDto> getSuggestionsByCategory(String category);

	
	
}
