package com.example.demo.dto.response;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
