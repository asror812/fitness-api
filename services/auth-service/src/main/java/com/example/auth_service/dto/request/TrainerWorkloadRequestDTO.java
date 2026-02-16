package com.example.auth_service.dto.request;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TrainerWorkloadRequestDTO {
    private String trainerUsername;

    private String trainerFirstName;

    private String trainerLastName;

    private LocalDate trainingDate;

    private Double duration;

    private ActionType actionType;
}
