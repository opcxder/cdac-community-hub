package com.cdac.suggestion.service.impl;

import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.service.AuthServiceClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Mock implementation of AuthServiceClient for development.
 * Returns hardcoded user data without making external HTTP calls.
 * 
 * Active for: dev profile only
 */
@Component
@Profile("dev")
public class MockAuthServiceClient implements AuthServiceClient {

    @Override
    public UserDTO getUserById(Long userId) {
        return new UserDTO(userId, "mock-user-" + userId);
    }
}
