package com.example.webhooksqlapp.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenGenerator.class);
    // IMPORTANT: In a real application, this key should be loaded securely
    // from environment variables or a key vault, and should be strong (e.g., 256-bit).
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token with specified subject and audience.
     *
     * @param subject   The subject of the token (e.g., user ID or service name).
     * @param audience  The audience of the token (e.g., the service it's intended for).
     * @return A signed JWT token string.
     */
    public String generateToken(String subject, String audience) {
        log.info("Generating JWT token for subject: {} and audience: {}", subject, audience);

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        // Token expires in 1 hour
        Date expiration = Date.from(now.plus(1, ChronoUnit.HOURS));

        // You can add custom claims if needed
        Map<String, Object> claims = new HashMap<>();
        claims.put("scope", "sql_solution_submit"); // Example custom claim

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("JWT token successfully generated.");
        return jwt;
    }
}