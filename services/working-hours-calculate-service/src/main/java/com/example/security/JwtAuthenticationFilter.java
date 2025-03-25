package com.example.security;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.example.dto.ErrorResponseDTO;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";
    private static final String TRANSACTION_ID = "transactionId";
    private final JwtService jwtService;

    private final Gson gson;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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

            String transactionId = (String) claims.get(TRANSACTION_ID);

            if (transactionId == null || transactionId.isBlank()) {
                sendErrorResponse(httpResponse, "Transaction id is missing");
                return;
            }

            long startTime = System.currentTimeMillis();
            LOGGER.info("Transaction id {} | HTTP {} - {}", transactionId, httpRequest.getMethod(),
                    httpRequest.getRequestURI());

            chain.doFilter(request, response);

            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("Transaction id {} | Response Status: {} | Time Taken: {}ms", transactionId,
                    httpResponse.getStatus(), duration);

        } catch (JwtException e) {
            LOGGER.error("JWT error: {}", e.getMessage());
            sendErrorResponse(httpResponse, "Authentication failed: Invalid token");
        } catch (Exception e) {
            LOGGER.error("Authentication error: ", e);
            sendErrorResponse(httpResponse, "Authentication failed");
        }

    }
}
