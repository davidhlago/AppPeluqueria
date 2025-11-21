package com.peluqueria.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Injected values from application.properties (or .yml)
    @Value("${peluqueria.jwt.secret}")
    private String jwtSecret;

    @Value("${peluqueria.jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * Gets the HMAC-SHA signing key for the provided secret.
     * Assumes ${peluqueria.jwt.secret} is a secure string.
     */
    private Key getSigningKey() {
        // Uses Keys.hmacShaKeyFor to create a secure key from the secret.
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates a JWT token for a username (email).
     * @param username The user's email.
     * @return The JWT token as a String.
     */
    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Subject: the user's email
                .setIssuedAt(new Date()) // Issue date
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Expiration date
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Sign with key and HS256 algorithm
                .compact();
    }

    /**
     * Extracts the username (Subject) from the JWT token.
     * @param token The JWT token.
     * @return The user's email.
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the integrity and expiration of the JWT token.
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String token) { // <-- RENAMED from 'validarToken'
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error while validating JWT token: {}", e.getMessage());
        }
        return false;
    }
}