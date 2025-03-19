package com.example.security;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.example.dto.ErrorResponseDTO;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


@Order(2)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private final JwtService jwtService;

    private final Gson gson;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final Set<String> EXCLUDED_URLS = Set.of(
            "/auth/trainers/sign-up",
            "/auth/trainees/sign-up",
            "/auth/sign-in");

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), message);
        response.getWriter().write(gson.toJson(errorResponse));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String path = httpRequest.getRequestURI();

        if (EXCLUDED_URLS.stream().anyMatch(path::contains)) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader(HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendErrorResponse(httpResponse, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.claims(token);
            if (claims == null) {
                sendErrorResponse(httpResponse, "Invalid or expired token");
                return;
            }

            String transactionId = claims.getSubject();
            if (transactionId == null || transactionId.isBlank()) {
                sendErrorResponse(httpResponse, "Transaction id is missing");
                return;
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error("Authentication error: ", e.getCause());
            sendErrorResponse(httpResponse, "Authentication failed");
        }

    }
}
