package com.example.demo.exception;

public final class ErrorMessages {

    public static final String RESOURCE_NOT_FOUND_ERROR = "The requested resource not found";
    public static final String ALREADY_EXISTS_ERROR = "The requested resource already exists";

    public static final String DATA_ACCESS_ERROR = "An error occurred while accessing data";
    public static final String DUPLICATE_ENTRY_ERROR = "Duplicate entry detected";


    public static final String AUTHENTICATION_FAILED = "Authentication failed";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";

    public static final String VALIDATION_ERROR = "Validation failed";

    public static final String TOO_MANY_REQUESTS = "Too many requests, please try again later";

    private ErrorMessages() {}
    

    //TODO: Improve log messages
    //TODO: Enhance test coverage
    //TODO: Imporve exception responses 
    
}