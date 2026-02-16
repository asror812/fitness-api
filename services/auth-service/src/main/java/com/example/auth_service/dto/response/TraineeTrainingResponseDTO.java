package com.example.auth_service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TraineeTrainingResponseDTO extends TrainingResponseDTO{

    private String trainerName;

    public TraineeTrainingResponseDTO(String trainingName, Date trainingDate, TrainingTypeResponseDTO trainingType,
            Double duration, String trainerName) {
        super(trainingName, trainingDate, trainingType, duration);
        this.trainerName = trainerName;
    }
    
}