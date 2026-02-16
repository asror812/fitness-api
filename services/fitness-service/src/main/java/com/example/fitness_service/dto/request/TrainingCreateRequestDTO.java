package com.example.fitness_service.dto.request;

import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingCreateRequestDTO {

    @NotBlank(message = "Trainee username must not be empty")
    private String traineeUsername;

    @NotBlank(message = "Trainer username must not be empty")
    private String trainerUsername;

    @NotBlank(message = "Training name must not be empty")
    private String trainingName;

    @NotNull(message = "Training date must not be null")
    private Date trainingDate;

    @NotNull(message = "Duration must not be null")
    @Positive(message = "Duration must be a positive number")
    private Double duration;

}
