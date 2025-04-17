package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(Exception e) {
        ErrorResponse error = ErrorResponse.builder().message(ErrorMessages.RESOURCE_NOT_FOUND_ERROR)
                .status(HttpStatus.NOT_FOUND.value()).build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    //handler for illegal argumen exception
}
