package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TraineeTrainersUpdateRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TraineeResponseDTO;
import com.example.demo.dto.response.TraineeUpdateResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;

public interface TraineeService extends GenericService<TraineeUpdateRequestDTO, TraineeUpdateResponseDTO>{

    SignUpResponseDTO register(TraineeSignUpRequestDTO requestDTO);

    List<TrainerResponseDTO> getNotAssignedTrainers(String username);

    void setStatus(String username, boolean status);

    void delete(String username);

    TraineeResponseDTO findByUsername(String username);

    List<TrainerResponseDTO> updateTraineeTrainers(String username,
            TraineeTrainersUpdateRequestDTO requestDTO);
}
