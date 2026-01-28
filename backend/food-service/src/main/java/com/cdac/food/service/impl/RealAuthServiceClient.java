package com.cdac.food.service.impl;

import com.cdac.food.dto.UserDTO;
import com.cdac.food.exception.UserNotFoundException;
import com.cdac.food.service.AuthServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * Real implementation of AuthServiceClient for production.
 * Makes HTTP calls to the Auth Service to validate users.
 * 
 * Active for: prod profile only
 * 
 * Requires:
 * - Auth Service running and accessible
 * - auth.service.url configured in application-prod.properties
 * - RestTemplate bean configured
 */
@Service
@Profile("prod")
public class RealAuthServiceClient implements AuthServiceClient {
    
    private static final Logger logger = LoggerFactory.getLogger(RealAuthServiceClient.class);
    
    private final RestTemplate restTemplate;
    
    @Value("${auth.service.url}")
    private String authServiceUrl;
    
    /**
     * Constructor injection of RestTemplate.
     * RestTemplate is configured in RestTemplateConfig.
     */
    public RealAuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public UserDTO getUserById(Long userId) {
        logger.debug("Calling Auth Service: getUserById userId={}", userId);
        
        try {
            // Call Auth Service internal API
            String url = authServiceUrl + "/internal/users/" + userId;
            logger.debug("Making HTTP GET request to: {}", url);
            
            UserDTO user = restTemplate.getForObject(url, UserDTO.class);
            
            if (user == null) {
                logger.error("Auth Service returned null for userId={}", userId);
                throw new UserNotFoundException("User not found: " + userId);
            }
            
            logger.info("Successfully fetched user from Auth Service: userId={}, username={}", 
                       userId, user.getUsername());
            return user;
            
        } catch (RestClientException e) {
            logger.error("Failed to fetch user from Auth Service: userId={}, error={}", 
                        userId, e.getMessage());
            throw new UserNotFoundException("Failed to fetch user from Auth Service: " + userId);
        }
    }
    
    @Override
    public boolean userExists(Long userId) {
        logger.debug("Calling Auth Service: userExists userId={}", userId);
        
        try {
            // Call Auth Service internal API to check user existence
            String url = authServiceUrl + "/internal/users/exists/" + userId;
            logger.debug("Making HTTP GET request to: {}", url);
            
            Boolean exists = restTemplate.getForObject(url, Boolean.class);
            
            boolean result = exists != null && exists;
            logger.debug("User exists check result: userId={}, exists={}", userId, result);
            
            return result;
            
        } catch (RestClientException e) {
            logger.error("Failed to check user existence: userId={}, error={}", 
                        userId, e.getMessage());
            // Return false on error rather than throwing exception
            // This allows graceful degradation
            return false;
        }
    }
}
