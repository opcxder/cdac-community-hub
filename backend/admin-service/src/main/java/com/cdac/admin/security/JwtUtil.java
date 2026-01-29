package com.cdac.admin.security;

import java.util.Base64;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final Key signingKey;
	private final String issuer;

	public JwtUtil(
			@Value("${jwt.secret}") String secret,
			@Value("${jwt.issuer}") String issuer) {

		// Use Base64 decoding to match auth-service
		byte[] keyBytes = Base64.getDecoder().decode(secret);
		this.signingKey = Keys.hmacShaKeyFor(keyBytes);
		this.issuer = issuer;

	}

	public Claims validateAndGetClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.requireIssuer(issuer)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

}
