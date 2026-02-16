package com.example.auth_service.exception;

public class JsonSerializationException extends RuntimeException {

    private final Object payload;

    public JsonSerializationException(String message, Object payload, Throwable cause) {
        super(message, cause);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}
