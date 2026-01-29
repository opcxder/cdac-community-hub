package com.cdac.admin.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("🔍 Admin JWT Filter - Path: " + path);

        // Skip public endpoints
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/api/admin/health")) {
            System.out.println("✅ Public endpoint - skipping JWT validation");
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");
        System.out.println("🔑 Authorization header: " + (header != null ? "Present (Bearer...)" : "MISSING"));

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("❌ Missing or invalid Authorization header - returning 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = header.substring(7);
        System.out.println(
                "🎫 Token extracted (first 20 chars): " + token.substring(0, Math.min(20, token.length())) + "...");

        try {
            Claims claims = jwtUtil.validateAndGetClaims(token);
            System.out.println("✅ Token validated successfully");
            System.out.println("   Subject: " + claims.getSubject());
            System.out.println("   User ID: " + claims.get("user_id"));
            System.out.println("   Email: " + claims.get("email"));
            System.out.println("   Role: " + claims.get("role"));

            String role = claims.get("role", String.class);

            if (role == null) {
                System.out.println("⚠️  Role claim is NULL - defaulting to USER");
                role = "USER";
            }

            // Assign role dynamically in Spring Security
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)) // dynamically ROLE_ADMIN, ROLE_USER, etc.
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("✅ Authentication set - Role: ROLE_" + role);

        } catch (JwtException ex) {
            System.out.println("❌ JWT Validation FAILED: " + ex.getClass().getSimpleName());
            System.out.println("   Error message: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (Exception ex) {
            System.out.println("❌ Unexpected error during JWT validation: " + ex.getClass().getSimpleName());
            System.out.println("   Error message: " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
