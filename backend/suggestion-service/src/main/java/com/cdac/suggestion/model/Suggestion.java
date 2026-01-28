package com.cdac.suggestion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "suggestion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
