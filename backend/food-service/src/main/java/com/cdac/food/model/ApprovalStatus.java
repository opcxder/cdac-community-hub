package com.cdac.food.model;

/**
 * Enum representing the approval status of categories and food places.
 * Used for admin approval workflow.
 */
public enum ApprovalStatus {
    /**
     * Item is pending admin approval
     */
    PENDING,
    
    /**
     * Item has been approved by admin and is visible to public
     */
    APPROVED,
    
    /**
     * Item has been rejected by admin
     */
    REJECTED
}
