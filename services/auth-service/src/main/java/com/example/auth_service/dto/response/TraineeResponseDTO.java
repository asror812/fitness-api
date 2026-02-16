package com.example.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraineeResponseDTO {
    private UserResponseDTO user;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    private String address;

    private List<TrainerResponseDTO> trainers;
}
