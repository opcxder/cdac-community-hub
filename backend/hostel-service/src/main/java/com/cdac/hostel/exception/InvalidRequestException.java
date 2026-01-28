package com.cdac.hostel.exception;

/**
 * Exception thrown when request data is invalid.
 * Used for validation failures, invalid parameters, etc.
 */
public class InvalidRequestException extends RuntimeException {
    
    public InvalidRequestException(String message) {
        super(message);
    }
}
