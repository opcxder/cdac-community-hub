package com.cdac.suggestion.service.impl;

import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.service.AuthServiceClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class MockAuthServiceClient implements AuthServiceClient {

    @Override
    public UserDTO getUserById(Long userId) {
        return new UserDTO(userId, "mock-user-" + userId);
    }
}
