package com.cdac.suggestion.dto;

public class UserDTO {

    private Long userId;
    private String username;

    public UserDTO(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UserDTO() {
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
}
