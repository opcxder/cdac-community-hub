package com.cdac.food.service;

import com.cdac.food.dto.UserDTO;

/**
 * Interface for Auth Service client.
 * Provides methods to interact with the Auth Service for user validation.
 * 
 * Two implementations:
 * - MockAuthServiceClient: For dev/test profiles (returns hardcoded users)
 * - RealAuthServiceClient: For prod profile (makes HTTP calls to Auth Service)
 */
public interface AuthServiceClient {
    
    /**
     * Get user details by user ID.
     * Used to fetch user information for display purposes.
     * 
     * @param userId the ID of the user
     * @return UserDTO containing user details
     * @throws com.cdac.food.exception.UserNotFoundException if user not found
     */
    UserDTO getUserById(Long userId);
    
    /**
     * Check if a user exists in the Auth Service.
     * Used for validation before creating food places, ratings, etc.
     * 
     * @param userId the ID of the user to check
     * @return true if user exists, false otherwise
     */
    boolean userExists(Long userId);
}
