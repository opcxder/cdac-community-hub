package com.example.demo.security;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final SecretKey key;

    @Value("${jwt.expiration:3600000}")
    private long jwtExpiration;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        // Decode Base64 secret to bytes and create key
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(com.example.demo.entity.User user) {
        // Determine role based on userId
        String role = (user.getUserId() == -1) ? "ADMIN" : "USER";

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("user_id", user.getUserId())
                .claim("email", user.getEmail())
                .claim("account_status", user.getAccountStatus().toString())
                .claim("role", role) // Added for admin-service compatibility
                .issuer("auth-service") // Required by admin-service
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .claim("role", "USER") // Default role
                .issuer("auth-service") // Required by admin-service
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Claims validateAndGetClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("Token expired");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token");
        }
    }

}
