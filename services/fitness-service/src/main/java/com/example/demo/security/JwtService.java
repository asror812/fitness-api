package com.example.demo.security;


import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service

public class JwtService {

    @Value("${security.jwt.signing-key}")
    private String secretKey;

    @Value("${security.jwt.duration}")
    private Long duration;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(this.duration));

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey())
                .compact();
    }

    public SecretKey signingKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
