package com.example.auth_service.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrainerTrainingResponseDTO extends TrainingResponseDTO{
    private String traineeName;

    public TrainerTrainingResponseDTO(String trainingName, Date trainingDate, TrainingTypeResponseDTO trainingType,
            Double duration, String traineeName) {
        super(trainingName, trainingDate, trainingType, duration);
        this.traineeName = traineeName;
    }

    
}
