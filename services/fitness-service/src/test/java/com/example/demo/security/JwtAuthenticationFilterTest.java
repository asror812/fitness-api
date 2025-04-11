package com.example.demo.security;

import com.example.demo.dao.UserDAO;
import com.example.demo.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
/* 
    @Test
    void testDoFilterInternal_ExcludedUrl() throws Exception {
        when(request.getRequestURI()).thenReturn("/auth/sign-in");

        when(response.getWriter()).thenReturn(pr)

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(jwtService, userDAO);
    }
    */

    @Test
    void testDoFilterInternal_ValidToken_Success() throws Exception {
        when(request.getRequestURI()).thenReturn("/some/other/url");
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");

        Claims claims = mock(Claims.class);
        when(jwtService.claims("validToken")).thenReturn(claims);
        when(claims.getSubject()).thenReturn("username");

        User user = mock(User.class);
        when(user.getAuthorities()).thenReturn(null);
        when(userDAO.findByUsername("username")).thenReturn(Optional.of(user));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}