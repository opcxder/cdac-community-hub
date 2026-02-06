package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsernameGeneratorService usernameGeneratorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        // Check email uniqueness BEFORE attempting save
        java.util.Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            
            // Block rejected users from re-registering
            if (user.getAccountStatus() == User.AccountStatus.REJECTED) {
                throw new RuntimeException("This email has been rejected. Please contact admin for assistance.");
            }
            
            // Block if user already exists with PENDING or APPROVED status
            throw new UserAlreadyExistsException("Email already registered");
        }

        User user = new User();

        // Username is ALWAYS auto-generated - user cannot provide it
        String username = usernameGeneratorService.generateUniqueUsername();

        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // user.setRole("USER"); // Role removed from entity

        return userRepository.save(user);
    }
}
