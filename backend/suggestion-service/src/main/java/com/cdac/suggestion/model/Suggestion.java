package com.cdac.suggestion.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suggestion")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long suggestionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String suggestionText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SuggestionCategory category;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Suggestion() {
    }

    public Suggestion(Long suggestionId, Long userId, String suggestionText, SuggestionCategory category,
            LocalDateTime createdAt) {
        this.suggestionId = suggestionId;
        this.userId = userId;
        this.suggestionText = suggestionText;
        this.category = category;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getSuggestionId() {
        return suggestionId;
    }

    public void setSuggestionId(Long suggestionId) {
        this.suggestionId = suggestionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public SuggestionCategory getCategory() {
        return category;
    }

    public void setCategory(SuggestionCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
