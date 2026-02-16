package com.example.fitness_service.dto.response;

import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrainerTrainingResponseDTO extends TrainingResponseDTO{
    private UUID traineeId;

    public TrainerTrainingResponseDTO(String trainingName, Date trainingDate, TrainingTypeResponseDTO trainingType,
            Double duration, UUID traineeId) {
        super(trainingName, trainingDate, trainingType, duration);
        this.traineeId = traineeId;
    }

    
}
