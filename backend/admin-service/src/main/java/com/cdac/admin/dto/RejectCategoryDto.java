package com.cdac.admin.dto;

/**
 * DTO for rejecting a category with a reason.
 */
public class RejectCategoryDto {
    private String reason;

    public RejectCategoryDto() {
    }

    public RejectCategoryDto(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
