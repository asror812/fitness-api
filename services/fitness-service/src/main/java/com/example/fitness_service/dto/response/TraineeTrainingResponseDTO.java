package com.example.fitness_service.dto.response;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TraineeTrainingResponseDTO extends TrainingResponseDTO{

    private UUID trainerId;

    public TraineeTrainingResponseDTO(String trainingName, Date trainingDate, TrainingTypeResponseDTO trainingType,
            Double duration, UUID trainerId) {
        super(trainingName, trainingDate, trainingType, duration);
        this.trainerId = trainerId;
    }
    
}