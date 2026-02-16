package com.example.fitness_service.dto.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraineeUpdateResponseDTO  {
    private UserUpdateResponseDTO user;

    private Date dateOfBirth;

    private String address;

    private List<TrainerResponseDTO> trainers = new ArrayList<>();

}
