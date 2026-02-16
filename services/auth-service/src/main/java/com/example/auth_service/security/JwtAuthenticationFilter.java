package com.example.auth_service.security;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.example.auth_service.dao.UserRepository;
import com.example.auth_service.dto.response.ErrorResponseDTO;
import com.example.auth_service.exception.AuthenticationFailureException;
import com.example.auth_service.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private static final String USER_NOT_FOUND = "User not found with username %s";
    private static final String INVALID_TOKEN = "Invalid or expired token";
    private static final String INVALID_USERNAME = "Invalid username";
    private static final String AUTHENTICATION_FAILED = "Authentication failed";
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().contains("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.claims(token);
            if (claims == null) {
                sendErrorResponse(response, INVALID_TOKEN);
                return;
            }

            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                sendErrorResponse(response, INVALID_USERNAME);
                return;
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(
                            () -> new AuthenticationFailureException(USER_NOT_FOUND.formatted(username)));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            LOGGER.error("Invalid or expired token: {} - {}", token, e.getMessage());
            sendErrorResponse(response, INVALID_TOKEN);
        } catch (Exception e) {
            LOGGER.error("Unexpected authentication error: {}", e.getMessage(), e);
            sendErrorResponse(response, AUTHENTICATION_FAILED);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
