package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidCredentialsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @org.springframework.beans.factory.annotation.Value("${admin.email:admin@cdac.in}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${admin.password:$2a$10$dummyHashedPasswordForDevelopment}")
    private String adminPasswordHash; // Expects BCrypt hash in properties or handled via encoder

    public String login(LoginRequest request) {

        System.out.println("🔐 LOGIN ATTEMPT: " + request);

        // 1. Check for Admin Login
        // Admin password must be properly hashed in properties
        if (adminEmail.equalsIgnoreCase(request.getEmail())) {
            System.out.println("✅ Admin login detected for: " + request.getEmail());
            if (passwordEncoder.matches(request.getPassword(), adminPasswordHash)) {
                System.out.println("✅ Admin password matched!");
                User adminUser = new User();
                adminUser.setUserId(-1L);
                adminUser.setUsername("admin");
                adminUser.setEmail(adminEmail);
                adminUser.setAccountStatus(User.AccountStatus.APPROVED);
                String token = jwtUtil.generateToken(adminUser);
                System.out.println("✅ Admin JWT token generated");
                return token;
            }
            // Admin password mismatch - throw same error as regular users
            System.out.println("❌ Admin password mismatch!");
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 2. Regular User Login (Login by Email as per requirement)
        System.out.println("🔍 Looking up user by email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("❌ User not found with email: " + request.getEmail());
                    return new InvalidCredentialsException("Invalid username or password");
                });

        System.out.println("✅ User found: " + user.getEmail() + " (Status: " + user.getAccountStatus() + ")");

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("❌ Password mismatch for user: " + user.getEmail());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3. Status Checks
        if (user.getAccountStatus() == User.AccountStatus.REJECTED) {
            System.out.println("❌ Account rejected: " + user.getEmail());
            throw new RuntimeException("Account rejected: " + user.getRejectionReason());
        }
        // PENDING is ALLOWED per User Instruction (to see Dashboard)
        // APPROVED is ALLOWED

        String token = jwtUtil.generateToken(user);
        System.out.println("✅ JWT token generated for user: " + user.getEmail());
        return token;
    }

}
