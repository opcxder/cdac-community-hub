package com.example.demo.controller;

import com.example.demo.dto.PendingUserDto;
import com.example.demo.dto.RejectUserDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

        @Autowired
        private UserRepository userRepository;

        
        @GetMapping("/pending")
        public ResponseEntity<List<PendingUserDto>> getPendingUsers() {
                List<User> pendingUsers = userRepository.findByAccountStatus(User.AccountStatus.PENDING);

                List<PendingUserDto> dtos = pendingUsers.stream()
                                .map(user -> new PendingUserDto(
                                                user.getUserId(),
                                                user.getUsername(),
                                                user.getEmail(),
                                                user.getPhone(),
                                                "PENDING",
                                                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null))
                                .collect(Collectors.toList());

                System.out.println("📊 [AUTH-SERVICE] Returning " + dtos.size() + " pending users");
                if (!dtos.isEmpty()) {
                        System.out.println("📊 [AUTH-SERVICE] Sample user (ID 1): " + 
                                "userId=" + dtos.get(0).getUserId() + 
                                ", username=" + dtos.get(0).getUsername() + 
                                ", email=" + dtos.get(0).getEmail() + 
                                ", phone=" + dtos.get(0).getPhone() + 
                                ", accountStatus=" + dtos.get(0).getAccountStatus() + 
                                ", createdAt=" + dtos.get(0).getCreatedAt());
                        
                        // Log user ID 4 if it exists (has phone number)
                        if (dtos.size() >= 3) {
                                System.out.println("📊 [AUTH-SERVICE] User with phone (ID 4): " + 
                                        "userId=" + dtos.get(2).getUserId() + 
                                        ", username=" + dtos.get(2).getUsername() + 
                                        ", email=" + dtos.get(2).getEmail() + 
                                        ", phone=" + dtos.get(2).getPhone() + 
                                        ", accountStatus=" + dtos.get(2).getAccountStatus() + 
                                        ", createdAt=" + dtos.get(2).getCreatedAt());
                        }
                }
                
                System.out.println("📊 [AUTH-SERVICE] About to return ResponseEntity with " + dtos.size() + " DTOs");
                return ResponseEntity.ok(dtos);
        }

               @PostMapping("/{id}/approve")
        public ResponseEntity<Void> approveUser(@PathVariable("id") Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                user.setAccountStatus(User.AccountStatus.APPROVED);
                user.setApprovedAt(LocalDateTime.now());
                userRepository.save(user);

                return ResponseEntity.ok().build();
        }

        /**
         * Reject a user (for Admin Service)
         */
        @PostMapping("/{id}/reject")
        public ResponseEntity<Void> rejectUser(
                        @PathVariable("id") Long userId,
                        @RequestBody RejectUserDto rejectDto) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                user.setAccountStatus(User.AccountStatus.REJECTED);
                user.setRejectionReason(rejectDto.getReason());
                userRepository.save(user);

                return ResponseEntity.ok().build();
        }

       
        @GetMapping("/exists/{userId}")
        public ResponseEntity<Boolean> userExists(@PathVariable Long userId) {
                boolean exists = userRepository.existsById(userId);
                return ResponseEntity.ok(exists);
        }

       
        @GetMapping("/{userId}")
        public ResponseEntity<User> getUserById(@PathVariable Long userId) {
                System.out.println("📊 [AUTH-SERVICE] GET /internal/users/" + userId + " - Fetching user");
                
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                
                System.out.println("✅ [AUTH-SERVICE] Returning user: userId=" + user.getUserId() + 
                        ", username=" + user.getUsername() + 
                        ", email=" + user.getEmail());
                
                return ResponseEntity.ok(user);
        }
}
