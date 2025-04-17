package com.example.demo.dto.request;

import java.time.LocalDate;

import com.example.demo.dto.ActionType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TrainerWorkloadRequestDTO {

    @NotNull(message = "Trainer username is required")
    private String trainerUsername;

    @NotNull(message = "Trainer firstname is required")
    private String trainerFirstName;

    @NotNull(message = "Trainer lastname is required")
    private String trainerLastName;

    @NotNull(message = "Training date is required")
    private LocalDate trainingDate;

    @NotNull(message = "Training duration is required")
    private Double duration;

    @NotNull(message = "Invalid action type")
    private ActionType actionType;
}
