package com.example.fitness_service.exception;

public class JsonDeserializationException extends RuntimeException {

    private final Object payload;

    public JsonDeserializationException(String message, Object payload, Throwable cause) {
        super(message, cause);
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}
