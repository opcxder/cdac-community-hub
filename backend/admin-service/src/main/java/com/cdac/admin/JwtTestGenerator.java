package com.cdac.admin;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
public class JwtTestGenerator {

    public static void main(String[] args) {

        String secret =
            "mySuperSecureJwtSecretKeyThatIsDefinitelyLongerThanSixtyFourCharacters1234567890";

        String token = Jwts.builder()
                .setSubject("admin@cdac.in")
                .setIssuer("auth-service")
                .claim("role", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(
                    Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)),
                    SignatureAlgorithm.HS512
                )
                .compact();

        System.out.println(token);
    }
}
