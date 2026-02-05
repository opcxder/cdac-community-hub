package com.cdac.hostel.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Service
@Profile("dev")
public class MockAuthServiceClient implements AuthServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(MockAuthServiceClient.class);

    @Override
    public boolean userExists(Long userId) {
        logger.debug("🔧 [MOCK-AUTH-CLIENT] Checking user existence: userId={}", userId);
        // Accept any positive userId for development
        boolean exists = userId != null && userId > 0;
        logger.debug("🔧 [MOCK-AUTH-CLIENT] User exists: {}", exists);
        return exists;
    }
}
