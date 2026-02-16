package com.example.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdateResponseDTO {

    private UserUpdateResponseDTO user;
    private TrainingTypeResponseDTO specialization;
    private List<TraineeResponseDTO> trainees = new ArrayList<>();

}