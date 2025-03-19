package com.example.security;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Order(1)
@Component
@RequiredArgsConstructor
public class TransactionLoggerFilter implements Filter {
    private static final String TRANSACTION_ID = "transactionId";
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionLoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String transactionID = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionID);

        long startTime = System.currentTimeMillis();
        LOGGER.info("Transaction id {} | HTTP {} - {}", transactionID, httpRequest.getMethod(),
                httpRequest.getRequestURI());

        try {
            chain.doFilter(request, response);

            long duration = System.currentTimeMillis() - startTime;
            LOGGER.info("Transaction id {} | Response Status: {} | Time Taken: {}ms", transactionID,
                    httpResponse.getStatus(), duration);

        } finally {
            MDC.remove(TRANSACTION_ID);
        }
    }
}