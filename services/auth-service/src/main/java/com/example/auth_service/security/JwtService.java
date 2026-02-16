package com.example.auth_service.security;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.auth_service.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    @Value("${token.duration}")
    private Long duration;

    @Value("${token.security-key}")
    private String secret;

    public String generateToken(User user) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(this.duration));

        Map<String, Object> claims = new HashMap<>();

        claims.put("roles", user.getRoles());
        claims.put("username", user.getUsername());
        claims.put("deleted", user.isDeleted());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> ((List<?>) claims.getOrDefault("roles", List.of()))
                .stream()
                .map(String::valueOf)
                .toList());
    }

    public boolean isTokenValid(String token, UUID id) {
        final UUID userId = extractUserId(token);
        return (userId.equals(id) && !isTokenExpired(token));
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(claims(token).getSubject());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = claims(token);

        return claimsResolver.apply(claims);
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private SecretKey signingKey() {
        byte[] decode = Base64.getDecoder().decode(secret);

        return Keys.hmacShaKeyFor(decode);
    }

}
