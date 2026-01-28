package com.cdac.hostel.dto;

import lombok.Data;

/**
 * DTO for creating a reply to a hostel review/rating.
 */
@Data
public class ReplyRequest {
    private String replyText;
}
