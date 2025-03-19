package com.example.security;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    private SecretKey microserviceSecretKey;
    private static final String TRANSACTION_ID = "transactionId";

    @PostConstruct
    public void init() {
        this.microserviceSecretKey = Keys.hmacShaKeyFor(Dotenv.load().get("MICROSERVICE_JWT_SIGNING_KEY").getBytes());
    }

    public Claims claims(String token) {
        return Jwts.parser().verifyWith(microserviceSecretKey).build().parseSignedClaims(token).getPayload();
    }

    public String extractTransactionId(String token) {
        return (String) claims(token).get(TRANSACTION_ID);
    }
}
