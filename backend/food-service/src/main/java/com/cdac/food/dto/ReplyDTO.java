package com.cdac.food.dto;

import java.time.LocalDateTime;

/**
 * DTO for transferring reply data to clients
 */
public class ReplyDTO {
    
    private Long replyId;
    private Long userId;
    private String username;  // Fetched from Auth Service
    private String replyText;
    private LocalDateTime createdAt;

    // Constructors
    public ReplyDTO() {
    }

    // Getters and Setters
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

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

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
