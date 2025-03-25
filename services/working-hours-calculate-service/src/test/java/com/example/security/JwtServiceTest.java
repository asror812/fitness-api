package com.example.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private Dotenv dotenv;

    private SecretKey secretKey;
    private String validToken;
    private final String TRANSACTION_ID = "transactionId";

    @BeforeEach
    void setUp() {
        when(dotenv.get("MICROSERVICE_JWT_SIGNING_KEY")).thenReturn("mysecretkeymysecretkeymysecretkey123");
        jwtService.init(); // Initialize secret key manually

        secretKey = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkey123".getBytes(StandardCharsets.UTF_8));

        validToken = Jwts.builder().claim(TRANSACTION_ID, "txn12345").signWith(secretKey).compact();
    }

    @Test
    void testClaims_ValidToken_ShouldReturnClaims() {
        Claims claims = jwtService.claims(validToken);
        assertNotNull(claims);
        assertEquals("txn12345", claims.get(TRANSACTION_ID));
    }

    @Test
    void testExtractTransactionId_ValidToken_ShouldReturnTransactionId() {
        String transactionId = jwtService.extractTransactionId(validToken);
        assertEquals("txn12345", transactionId);
    }

    @Test
    void testClaims_InvalidToken_ShouldThrowException() {
        String invalidToken = "invalid.token.value";
        assertThrows(Exception.class, () -> jwtService.claims(invalidToken));
    }
}
