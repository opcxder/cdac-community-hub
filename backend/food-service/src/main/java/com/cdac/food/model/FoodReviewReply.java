package com.cdac.food.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_review_replies")
public class FoodReviewReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_id", nullable = false)
    private FoodPlaceRating rating;

  
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "reply_text", nullable = false, columnDefinition = "TEXT")
    private String replyText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors
    public FoodReviewReply() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public FoodPlaceRating getRating() {
        return rating;
    }

    public void setRating(FoodPlaceRating rating) {
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
