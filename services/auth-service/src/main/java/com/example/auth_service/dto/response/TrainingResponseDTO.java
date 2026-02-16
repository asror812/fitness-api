package com.example.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainingResponseDTO {

    private String trainingName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date trainingDate;

    private TrainingTypeResponseDTO trainingType;

    private Double duration;
}
