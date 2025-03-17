package com.example.demo.dto.response;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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