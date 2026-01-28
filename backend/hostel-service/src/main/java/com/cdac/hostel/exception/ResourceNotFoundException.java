package com.cdac.hostel.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Used for hostels, ratings, categories, images, etc.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
    }
}
