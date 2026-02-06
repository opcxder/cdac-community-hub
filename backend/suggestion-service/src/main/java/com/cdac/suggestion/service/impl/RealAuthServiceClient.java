package com.cdac.suggestion.service.impl;

import com.cdac.suggestion.dto.UserDTO;
import com.cdac.suggestion.service.AuthServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Real implementation of AuthServiceClient for production.
 * Makes HTTP calls to the Auth Service to get user data.
 * 
 * Active for: prod profile only
 */
@Service
@Profile("prod")
public class RealAuthServiceClient implements AuthServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(RealAuthServiceClient.class);
    
    private final RestTemplate restTemplate;
    
    @Value("${auth.service.url}")
    private String authServiceUrl;
    
    public RealAuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        logger.info("🔍 [AUTH-CLIENT] Fetching user from auth-service: userId={}", userId);
        
        try {
            String url = authServiceUrl + "/internal/users/" + userId;
            logger.info("🔍 [AUTH-CLIENT] Making HTTP GET request to: {}", url);
            
            UserDTO user = restTemplate.getForObject(url, UserDTO.class);
            
            if (user == null) {
                logger.warn("⚠️ [AUTH-CLIENT] Auth Service returned null for userId={}", userId);
                return new UserDTO(userId, "unknown-user-" + userId);
            }
            
            logger.info("✅ [AUTH-CLIENT] Successfully fetched user: userId={}, username={}", 
                       userId, user.getUsername());
            return user;
            
        } catch (RestClientException e) {
            logger.error("❌ [AUTH-CLIENT] Failed to fetch user from Auth Service: userId={}, error={}", 
                        userId, e.getMessage());
            e.printStackTrace();
            // Return fallback user on error
            return new UserDTO(userId, "error-user-" + userId);
        }
    }
}
