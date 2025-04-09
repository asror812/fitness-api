package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.demo.dto.response.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        LOGGER.warn("Resource not found: {}", e.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ErrorMessages.RESOURCE_NOT_FOUND_ERROR)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(DataAccessException e) {
        LOGGER.error("Data access error", e);

        if (e.getCause() instanceof ConstraintViolationException) {
            ErrorResponseDTO error = ErrorResponseDTO.builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message(ErrorMessages.DUPLICATE_ENTRY_ERROR)
                    .timestamp(Instant.now())
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ErrorMessages.DATA_ACCESS_ERROR)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler({ InvalidCredentialsException.class, AuthenticationFailureException.class })
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationExceptions(RuntimeException ex) {
        LOGGER.warn("Authentication failed: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ErrorMessages.INVALID_CREDENTIALS)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        LOGGER.warn("Validation errors: {}", errors);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ErrorMessages.VALIDATION_ERROR)
                .details(errors)
                .timestamp(Instant.now()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistException(AlreadyExistException ex) {
        LOGGER.warn("Resource already exists: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .message(ErrorMessages.ALREADY_EXISTS_ERROR)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT.value())
                .body(error);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ErrorResponseDTO> handleTooManyRequestsException(TooManyRequestsException ex) {
        LOGGER.warn("REquest rate limit exceeded: {}", ex.getMessage());

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                .message(ErrorMessages.TOO_MANY_REQUESTS)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }
}