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
        // todo
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
