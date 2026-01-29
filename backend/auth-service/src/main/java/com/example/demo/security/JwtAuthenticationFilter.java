package com.example.demo.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entity.User;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // spaces
        try {
            if (header != null && header.startsWith("Bearer ")) {

                String token = header.substring(7);

                // validate token (may throw exception)
                if (jwtUtil.isTokenValid(token)) {

                    String username = jwtUtil.extractUsername(token);

                    // Special handling for Admin (who is not in DB)
                    if ("admin".equalsIgnoreCase(username)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        // Regular User
                        User user = userRepository.findByUsername(username).orElse(null);

                        if (user != null) {
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    null,
                                    List.of(new SimpleGrantedAuthority("ROLE_USER")));

                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                }
            }

            // continue filter chain
            filterChain.doFilter(request, response);

        } catch (InvalidTokenException ex) {

            // JWT error → stop filter chain
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"" + ex.getMessage() + "\"}");
        }
    }
}
