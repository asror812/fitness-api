package com.example.demo.security;



import java.util.Date;
import javax.crypto.SecretKey;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    private Long duration;
    private SecretKey secretKey;
    private SecretKey microserviceSecretKey;
    private Long microserviceDuration;

    @PostConstruct
    public void init() {
        Dotenv envFile = Dotenv.load();
        this.duration = Long.parseLong(envFile.get("JWT_DURATION"));
        this.secretKey = Keys.hmacShaKeyFor(envFile.get("JWT_SECRET_KEY").getBytes());

        this.microserviceSecretKey = Keys.hmacShaKeyFor(
                envFile.get("MICROSERVICE_JWT_SIGNING_KEY").getBytes());
        this.microserviceDuration = Long
                .parseLong(envFile.get("MICROSERVICE_JWT_DURATION"));
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(this.duration));

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    public String generateTokenForMicroservice() {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(this.microserviceDuration));

        return Jwts.builder()
                .subject("fitness-microservice")
                .issuedAt(now)
                .expiration(expiration)
                .claim("transactionId", MDC.get("transactionId"))
                .signWith(microserviceSecretKey)
                .compact();
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
