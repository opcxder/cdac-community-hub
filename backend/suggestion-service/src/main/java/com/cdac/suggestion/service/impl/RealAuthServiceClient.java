package com.cdac.suggestion.service.impl;

import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.service.AuthServiceClient;
import org.springframework.stereotype.Component;

@Component
public class RealAuthServiceClient implements AuthServiceClient {

    @Override
    public UserDTO getUserById(Long userId) {
        // Will call AUTH SERVICE later
        return new UserDTO(userId, "real-user-" + userId);
    }
}
