package com.cdac.suggestion.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_cache")
public class UserCache {

    @Id
    private Long userId;

    private String username;

    private String email;

    private LocalDateTime lastSynced;

    public UserCache() {
    }

    public UserCache(Long userId, String username, String email, LocalDateTime lastSynced) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.lastSynced = lastSynced;
    }

    // REQUIRED for Day-2
    public UserCache(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        this.lastSynced = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLastSynced() {
        return lastSynced;
    }

    public void setLastSynced(LocalDateTime lastSynced) {
        this.lastSynced = lastSynced;
    }
}
