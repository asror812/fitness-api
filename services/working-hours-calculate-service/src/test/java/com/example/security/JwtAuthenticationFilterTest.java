package com.example.security;

import static org.mockito.Mockito.*;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Mock
    private Gson gson;

    @Test
    void doFilter_MissingAuthorizationHeader() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    void doFilter_InvalidToken() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtService.claims("invalid_token")).thenThrow(new JwtException("Invalid token"));
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    void doFilter_ValidToken_MissingTransactionId() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        Claims claims = mock(Claims.class);
        when(jwtService.claims("valid_token")).thenReturn(claims);
        when(claims.get("transactionId")).thenReturn(null);
        when(response.getWriter()).thenReturn(mock(java.io.PrintWriter.class));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
    }

    @Test
    void doFilter_SuccessfulAuthentication() throws IOException, ServletException {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        when(response.getStatus()).thenReturn(HttpServletResponse.SC_OK);

        Claims claims = mock(Claims.class);
        when(jwtService.claims("valid_token")).thenReturn(claims);
        when(claims.get("transactionId")).thenReturn("123456");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}