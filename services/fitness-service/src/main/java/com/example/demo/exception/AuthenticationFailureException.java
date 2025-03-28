package com.example.demo.exception;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException(String message) {
        super(message);
    }

    public AuthenticationFailureException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
