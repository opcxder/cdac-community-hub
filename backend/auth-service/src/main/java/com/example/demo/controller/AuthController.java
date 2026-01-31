package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.demo.service.UserService;
import com.example.demo.service.UsernameGeneratorService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UsernameGeneratorService usernameGeneratorService;

    @Autowired
    private com.example.demo.repository.UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("📥 [AUTH-CONTROLLER] Login request received for: " + request.getEmail());

        String token = authService.login(request);

        // Get user details for response
        com.example.demo.entity.User userEntity;
        if ("admin@cdac.in".equalsIgnoreCase(request.getEmail())) {
            // Admin user
            userEntity = new com.example.demo.entity.User();
            userEntity.setUserId(-1L);
            userEntity.setUsername("admin");
            userEntity.setEmail(request.getEmail());
            userEntity.setAccountStatus(com.example.demo.entity.User.AccountStatus.APPROVED);
        } else {
            // Regular user - use repository directly
            userEntity = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Map<String, Object> response = new HashMap<>();

        // User object
        Map<String, Object> user = new HashMap<>();
        user.put("userId", userEntity.getUserId());
        user.put("username", userEntity.getUsername());
        user.put("email", userEntity.getEmail());
        user.put("phone", userEntity.getPhone());
        user.put("accountStatus", userEntity.getAccountStatus().toString());
        user.put("createdAt", userEntity.getCreatedAt() != null ? userEntity.getCreatedAt().toString() : null);

        response.put("user", user);
        response.put("accessToken", token);
        response.put("refreshToken", token); // Using same token for now (should be separate in production)

        System.out.println("📤 [AUTH-CONTROLLER] Login successful for: " + request.getEmail());
        System.out.println("📦 [AUTH-CONTROLLER] Response structure:");
        System.out.println("   - user: " + user);
        System.out.println("   - accessToken: " + (token != null ? "Present (length=" + token.length() + ")" : "NULL"));
        System.out
                .println("   - refreshToken: " + (token != null ? "Present (length=" + token.length() + ")" : "NULL"));
        System.out.println("📤 [AUTH-CONTROLLER] Full response: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate-username")
    public ResponseEntity<Map<String, String>> generateUsername() {
        System.out.println("🔔 [AUTH-SERVICE] /generate-username endpoint called");
        String username = usernameGeneratorService.generateUniqueUsername();
        System.out.println("✅ [AUTH-SERVICE] Generated username: " + username);
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        System.out.println("📤 [AUTH-SERVICE] Sending response: " + response);
        return ResponseEntity.ok(response);
    }

}
