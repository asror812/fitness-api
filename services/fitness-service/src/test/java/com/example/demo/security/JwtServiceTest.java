package com.example.demo.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        System.setProperty("JWT_DURATION", "3600");
        System.setProperty("JWT_SECRET_KEY", "12345678901234567890123456789012");

        jwtService = new JwtService() {
            @Override
            public void init() {
                this.duration = Long.parseLong(System.getProperty("JWT_DURATION"));
                this.secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                        System.getProperty("JWT_SECRET_KEY").getBytes());
            }
        };

        jwtService.init();
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken("testUser");
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void claims_ShouldReturnCorrectUsernameClaim() {
        String token = jwtService.generateToken("testUser");

        Claims claims = jwtService.claims(token);

        assertEquals("testUser", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}
