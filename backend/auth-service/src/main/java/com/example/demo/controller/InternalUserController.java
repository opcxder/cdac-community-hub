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

    /**
     * Get all pending users (for Admin Service)
     */
    @GetMapping("/pending")
    public ResponseEntity<List<PendingUserDto>> getPendingUsers() {
        List<User> pendingUsers = userRepository.findByAccountStatus(User.AccountStatus.PENDING);

        List<PendingUserDto> dtos = pendingUsers.stream()
                .map(user -> new PendingUserDto(
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail(),
                        "PENDING"))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * Approve a user (for Admin Service)
     */
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
}
