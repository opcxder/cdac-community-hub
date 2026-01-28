package com.cdac.suggestion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCache {

    @Id
    private Long userId;

    private String username;

    private String email;

    private LocalDateTime lastSynced;

    // REQUIRED for Day-2
    public UserCache(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        this.lastSynced = LocalDateTime.now();
    }
}
