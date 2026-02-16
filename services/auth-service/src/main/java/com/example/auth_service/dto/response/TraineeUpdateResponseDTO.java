package com.example.auth_service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class TraineeUpdateResponseDTO  {
    private UserUpdateResponseDTO user;

    private Date dateOfBirth;

    private String address;

    private List<TrainerResponseDTO> trainers = new ArrayList<>();

}
