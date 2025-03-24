package com.example.demo.dto.request;

import java.util.Date;

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
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private Date trainingDate;
    private Double duration;
    private ActionType actionType;
}
