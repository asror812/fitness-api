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

    @NotNull
    private String trainerUsername;

    @NotNull
    private String trainerFirstName;

    @NotNull
    private String trainerLastName;

    @NotNull
    private LocalDate trainingDate;

    @NotNull
    private Double duration;

    @NotNull
    private ActionType actionType;
}
