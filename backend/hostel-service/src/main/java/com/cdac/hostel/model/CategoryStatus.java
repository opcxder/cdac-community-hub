package com.cdac.hostel.model;

/**
 * Enumeration representing the approval status of a hostel category.
 * Categories follow an approval workflow similar to hostels.
 */
public enum CategoryStatus {
    /**
     * Category has been submitted but not yet reviewed by admin
     */
    PENDING,
    
    /**
     * Category has been approved by admin and is visible to all users
     */
    APPROVED,
    
    /**
     * Category has been rejected by admin
     */
    REJECTED
}
