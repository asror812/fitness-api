package com.example.fitness_service.exception;

public class TooManyRequestsException  extends RuntimeException{

    public TooManyRequestsException(String message) {
        super(message);
    }

}
