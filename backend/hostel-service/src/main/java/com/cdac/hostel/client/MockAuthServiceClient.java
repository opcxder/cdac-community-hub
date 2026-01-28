package com.cdac.hostel.client;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class MockAuthServiceClient implements AuthServiceClient {

    @Override
    public boolean userExists(Long userId) {
        return userId == 1 || userId == 2 || userId == 3;
    }
}
