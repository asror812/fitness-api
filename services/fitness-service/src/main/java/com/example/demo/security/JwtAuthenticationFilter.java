package com.example.demo.security;

import java.io.IOException;
import java.util.Set;

import com.example.demo.dao.UserDAO;
import com.example.demo.exceptions.ErrorResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.model.User;
import com.example.demo.utils.BruteForceProtectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
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
    private final BruteForceProtectorService bruteForceProtectorService;

    private final Gson gson;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    public static final Set<String> EXCLUDED_URLS = Set.of(
            "/auth/trainers/sign-up",
            "/auth/trainees/sign-up",
            "/auth/sign-in");

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (EXCLUDED_URLS.stream().anyMatch(path::contains)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HEADER_NAME);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            handleFailedAttempt(request, response, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtService.claims(token);
            if (claims == null) {
                handleFailedAttempt(request, response, "Invalid or expired token");
                return;
            }

            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                handleFailedAttempt(request, response, "Invalid username");
                return;
            }

            User user = userDAO.findByUsername(username)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("User not found with username %s".formatted(username)));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            LOGGER.error("Authentication error: ", e.getCause());
            handleFailedAttempt(request, response, "Authentication failed");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), message);
        response.getWriter().write(gson.toJson(errorResponse));
    }

    private void handleFailedAttempt(HttpServletRequest request, HttpServletResponse response, String message)
            throws IOException {
        String ip = request.getRemoteAddr();
        bruteForceProtectorService.incrementFailureCount(ip, response);
        sendErrorResponse(response, message);
    }
}
