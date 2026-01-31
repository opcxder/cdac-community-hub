package com.cdac.food.service.impl;

import com.cdac.food.dto.UserDTO;
import com.cdac.food.exception.UserNotFoundException;
import com.cdac.food.service.AuthServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Mock implementation of AuthServiceClient for development and testing.
 * Returns hardcoded user data without making external HTTP calls.
 * 
 * Active for: dev, test profiles (any profile except prod)
 * 
 * Mock users available:
 * - User ID 1: HappyPanda42
 * - User ID 2: SmartTiger88
 * - User ID 3: BraveLion99
 */
@Component
@Profile("dev")
public class MockAuthServiceClient implements AuthServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MockAuthServiceClient.class);

    // Mock user data matching seed data in requirements
    private static final String[] USERNAMES = { "HappyPanda42", "SmartTiger88", "BraveLion99" };
    private static final String[] EMAILS = { "user1@test.com", "user2@test.com", "user3@test.com" };

    @Override
    public UserDTO getUserById(Long userId) {
        logger.debug("Mock: Getting user by ID: {}", userId);

        // Validate userId range (only users 1, 2, 3 exist in mock)
        if (userId == null || userId < 1 || userId > 3) {
            logger.warn("Mock: User not found: userId={}", userId);
            throw new UserNotFoundException("User not found: " + userId);
        }

        // Return mock user data
        int index = userId.intValue() - 1;
        UserDTO user = new UserDTO(
                userId,
                USERNAMES[index],
                EMAILS[index],
                "APPROVED");

        logger.debug("Mock: Returning user: userId={}, username={}", userId, user.getUsername());
        return user;
    }

    @Override
    public boolean userExists(Long userId) {
        logger.debug("Mock: Checking if user exists: userId={}", userId);

        // Only users 1, 2, 3 exist in mock environment
        boolean exists = userId != null && userId >= 1 && userId <= 3;

        logger.debug("Mock: User exists check result: userId={}, exists={}", userId, exists);
        return exists;
    }
}
