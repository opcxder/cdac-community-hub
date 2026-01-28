package com.cdac.food.dto;

import com.cdac.food.model.ApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for transferring category data to clients
 */
public class CategoryDTO {
    
    private Long categoryId;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must not exceed 100 characters")
    private String categoryName;
    
    private ApprovalStatus status;
    private Long createdByUserId;

    // Constructors
    public CategoryDTO() {
    }

    public CategoryDTO(Long categoryId, String categoryName, ApprovalStatus status) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.status = status;
    }

    // Getters and Setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
}
