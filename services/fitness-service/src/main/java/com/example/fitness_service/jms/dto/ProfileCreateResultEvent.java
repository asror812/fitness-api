package com.example.fitness_service.jms.dto;

import java.util.UUID;

import com.example.fitness_service.jms.enums.Result;
import com.example.fitness_service.jms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProfileCreateResultEvent {

    private UUID eventId;
    private UUID correlationId;

    private UUID userId;
    private Role role;
    private Result result;

    private boolean created;
    private String message;
}