package com.cdac.food.dto;

/**
 * DTO for user data fetched from Auth Service
 */
public class UserDTO {
    
    private Long userId;
    private String username;
    private String email;
    private String accountStatus;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Long userId, String username, String email, String accountStatus) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.accountStatus = accountStatus;
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

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
