package com.example.fitness_service.jms.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TraineeRegisterEvent {

    private UUID userId;

    private LocalDate dateOfBirth;

    private String address;

}
