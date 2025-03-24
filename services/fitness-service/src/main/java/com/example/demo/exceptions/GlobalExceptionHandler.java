package com.example.demo.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.response.ErrorResponseDTO;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
                ErrorMessages.RESOURCE_NOT_FOUND_ERROR);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //TODO: Optimize DataAccessException
    //TODO: Correct test coverage
    //write excption handler for Authenticate Failure Exception
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataAccessException(DataAccessException e) {

        if (e.getCause() instanceof ConstraintViolationException) {
            ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
                    ErrorMessages.DUPLICATE_ENTRY_ERROR);

            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        else {
            ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
                    ErrorMessages.INVALID_CREDENTIALS);

            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
                ErrorMessages.INVALID_CREDENTIALS);

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistException(AlreadyExistException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(HttpStatus.CONFLICT.value(), ErrorMessages.ALREADY_EXISTS_ERROR);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<ErrorResponseDTO> handleTooManyRequestsException(TooManyRequestsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(429, ErrorMessages.TOO_MANY_REQUESTS);
        return new ResponseEntity<>(error, HttpStatus.TOO_MANY_REQUESTS);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}