package com.example.demo.security;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private UserDAO userDAO;
    private ObjectMapper objectMapper;
    private JwtAuthenticationFilter filter;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        userDAO = mock(UserDAO.class);
        objectMapper = new ObjectMapper();

        filter = new JwtAuthenticationFilter(jwtService, userDAO, objectMapper);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void shouldReturnUnauthorized_WhenNoAuthorizationHeader() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(sw.toString().contains("Missing or invalid Authorization header"));
    }

    @Test
    void shouldReturnUnauthorized_WhenJwtInvalid() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer bad-token");
        when(jwtService.claims("bad-token")).thenThrow(new JwtException("Invalid"));

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(sw.toString().contains("Invalid or expired token"));
    }

    @Test
    void shouldReturnUnauthorized_WhenUsernameMissing() throws Exception {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(null);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.claims("token")).thenReturn(claims);

        StringWriter sw = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(sw));

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        assertTrue(sw.toString().contains("Invalid username"));
    }

    @Test
    void shouldAuthenticateAndContinueFilterChain_WhenValidToken() throws Exception {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn("validUser");

        User user = mock(User.class);
        when(user.getAuthorities()).thenReturn(null); 

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtService.claims("token")).thenReturn(claims);
        when(userDAO.findByUsername("validUser")).thenReturn(Optional.of(user));

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotFilter_forExcludedPaths() throws Exception {
        when(request.getServletPath()).thenReturn("/auth/login");
        assertTrue(filter.shouldNotFilter(request));

        when(request.getServletPath()).thenReturn("/management/info");
        assertTrue(filter.shouldNotFilter(request));

        when(request.getServletPath()).thenReturn("/api/data");
        assertFalse(filter.shouldNotFilter(request));
    }
}
