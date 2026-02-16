package com.example.workload_service.dto.request;

import java.time.LocalDate;

import com.example.workload_service.dto.ActionType;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Trainer username is required")
    private String trainerUsername;

    @NotBlank(message = "Trainer firstname is required")
    private String trainerFirstName;

    @NotBlank(message = "Trainer lastname is required")
    private String trainerLastName;

    @NotBlank(message = "Training date is required")
    private LocalDate trainingDate;

    @NotBlank(message = "Training duration is required")
    private Double duration;

    @NotBlank(message = "Invalid action type")
    private ActionType actionType;
}
