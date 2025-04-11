package com.example.demo.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TrainerUpdateResponseDTO {

    private UserUpdateResponseDTO user;
    private TrainingTypeResponseDTO specialization;
    private List<TraineeResponseDTO> trainees = new ArrayList<>();

}