package com.example.gateway_server.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenProperties tokenProperties;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiration = Date.from(now.toInstant().plusSeconds(tokenProperties.getDuration()));

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey())
                .compact();
    }

    public Claims claims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        byte[] decode = Base64.getDecoder().decode(tokenProperties.getToken());
        return Keys.hmacShaKeyFor(decode);
    }

    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> ((List<?>) claims.getOrDefault("roles", List.of()))
                .stream()
                .map(String::valueOf)
                .toList());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = claims(token);

        return claimsResolver.apply(claims);
    }

}
