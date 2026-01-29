package com.example.demo.dto;

public class RejectUserDto {
    private String reason;

    public RejectUserDto() {
    }

    public RejectUserDto(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
