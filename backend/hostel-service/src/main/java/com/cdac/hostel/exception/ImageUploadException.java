package com.cdac.hostel.exception;

/**
 * Exception thrown when image upload or deletion fails.
 * Used for Cloudinary errors, file size issues, etc.
 */
public class ImageUploadException extends RuntimeException {
    
    public ImageUploadException(String message) {
        super(message);
    }
    
    public ImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
