package com.cdac.hostel.exception;

/**
 * Exception thrown when attempting to create a duplicate resource.
 * Used for duplicate ratings, categories, replies, etc.
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
}
