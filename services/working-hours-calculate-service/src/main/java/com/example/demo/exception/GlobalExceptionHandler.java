package com.example.demo.exception;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e) {
        ErrorResponse error = ErrorResponse.builder().message(ErrorMessages.RESOURCE_NOT_FOUND_ERROR)
                .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now())).build();

        LOGGER.error("{}", error);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse error = ErrorResponse.builder().message(ErrorMessages.INVALID_ACTION_TYPE_ERROR)
                .timestamp(DateTimeFormatter.ISO_INSTANT.format(Instant.now())).build();

        LOGGER.error("{}", error);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
