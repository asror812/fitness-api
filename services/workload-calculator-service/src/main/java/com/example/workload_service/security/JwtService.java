package com.example.workload_service.security;

import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {
    @Value("${token.duration}")
    private Long duration;

    @Value("${token.security-key}")
    private String secret;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(this.duration));

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey()).compact();
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] decode = Base64.getDecoder().decode(secret);

        return Keys.hmacShaKeyFor(decode);
    }
}
