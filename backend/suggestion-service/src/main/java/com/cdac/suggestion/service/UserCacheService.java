package com.cdac.suggestion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.model.UserCache;
import com.cdac.suggestion.repository.UserCacheRepository;


@Service
public class UserCacheService {

    @Autowired
    private UserCacheRepository repository;

    @Autowired
    private AuthServiceClient authServiceClient;

    public UserDTO getUserWithCache(Long userId) {

        return repository.findByUserId(userId)
                .map(c -> new UserDTO(c.getUserId(), c.getUsername()))
                .orElseGet(() -> {
                    UserDTO user = authServiceClient.getUserById(userId);
                    repository.save(new UserCache(user.getUserId(), user.getUsername()));
                    return user;
                });
    }
}
