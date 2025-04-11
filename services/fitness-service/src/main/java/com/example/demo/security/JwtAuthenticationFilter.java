package com.example.demo.security;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import com.example.demo.dao.UserDAO;
import com.example.demo.dto.response.ErrorResponseDTO;
import com.example.demo.exception.AuthenticationFailureException;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
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
    private final UserDAO userDAO;
    private static final String USER_NOT_FOUND = "User not found with username %s";
    private static final String INVALID_TOKEN = "Invalid or expired token";
    private static final String INVALID_USERNAME = "Invalid username";
    private static final String INVALID_OR_MISSING_AUTH_HEAD = "Missing or invalid Authorization header";
    private static final String AUTHENTICATION_FAILED = "Authentication failed";
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, INVALID_OR_MISSING_AUTH_HEAD);
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.claims(token);
            if (claims == null) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, INVALID_TOKEN);
                return;
            }

            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, INVALID_USERNAME);
                return;
            }

            User user = userDAO.findByUsername(username)
                    .orElseThrow(
                            () -> new AuthenticationFailureException(USER_NOT_FOUND.formatted(username)));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            LOGGER.error("Invalid or expired token: {} - {}", token, e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, INVALID_TOKEN);
        } catch (Exception e) {
            LOGGER.error("Unexpected authentication error: {}", e.getMessage(), e);
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, AUTHENTICATION_FAILED);
        }
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/") || path.startsWith("/management/");
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message)
            throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
