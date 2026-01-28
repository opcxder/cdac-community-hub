package com.cdac.suggestion.repository;

import com.cdac.suggestion.model.Suggestion;
import com.cdac.suggestion.model.SuggestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    
    List<Suggestion> findByUserIdOrderByCreatedAtDesc(Long userId);

    
    List<Suggestion> findByCategory(SuggestionCategory category);
}
