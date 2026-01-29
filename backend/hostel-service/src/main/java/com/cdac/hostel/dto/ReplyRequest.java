package com.cdac.hostel.dto;

/**
 * DTO for creating a reply to a hostel review/rating.
 */
public class ReplyRequest {
    private String replyText;

    // Getters and Setters
    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }
}
