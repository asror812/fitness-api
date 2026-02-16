package com.example.gateway_server.exception;

public class TooManyRequestsException  extends RuntimeException{

    public TooManyRequestsException(String message) {
        super(message);
    }

}
