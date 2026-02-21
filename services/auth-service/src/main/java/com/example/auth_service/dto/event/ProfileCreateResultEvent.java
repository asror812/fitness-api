package com.example.auth_service.dto.event;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileCreateResultEvent {

    private UUID eventId;
    private UUID correlationId;

    private Role role;
    private Result result;

    private boolean created;
    private String message;
}

enum Result {
    OK,
    ERROR
}