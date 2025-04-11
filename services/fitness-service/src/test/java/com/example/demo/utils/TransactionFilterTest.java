package com.example.demo.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionLoggerFilterTest {

    private TransactionLoggerFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        filter = new TransactionLoggerFilter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void shouldLogAndClearTransactionId() throws ServletException, IOException {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(200);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);

        assertNull(MDC.get("transactionID"));
    }

    @Test
    void shouldAlwaysClearMDC_IfExceptionIsThrown() {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/exception");

        try {
            doThrow(new RuntimeException("Fail")).when(filterChain).doFilter(request, response);
            assertThrows(RuntimeException.class, () -> filter.doFilterInternal(request, response, filterChain));
        } catch (Exception e) {
            fail("Should not throw here");
        }

        assertNull(MDC.get("transactionID"));
    }
}
