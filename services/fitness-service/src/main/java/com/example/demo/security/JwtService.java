package com.example.demo.security;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    protected Long duration;
    protected SecretKey secretKey;

    @PostConstruct void init() {
        Dotenv envFile = Dotenv.load();
        this.duration = Long.parseLong(envFile.get("JWT_DURATION"));
        this.secretKey = Keys.hmacShaKeyFor(envFile.get("JWT_SECRET_KEY").getBytes());
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

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
