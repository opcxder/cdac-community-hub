package com.cdac.suggestion.service;

import com.cdac.suggestion.dto.UserDTO;

public interface AuthServiceClient {
    UserDTO getUserById(Long userId);
}
