package com.example.demo.service;

import java.util.Date;
import java.util.List;

import com.example.demo.dto.request.TrainingCreateRequestDTO;
import com.example.demo.dto.request.TrainingUpdateRequestDTO;
import com.example.demo.dto.response.TrainingResponseDTO;
import com.example.demo.dto.response.TrainingUpdateResponseDTO;

public interface TrainingService extends GenericService<TrainingUpdateRequestDTO, TrainingUpdateResponseDTO> {
    void create(TrainingCreateRequestDTO createDTO);

    List<TrainingResponseDTO> getTraineeTrainings(String username, Date from, Date to, String trainerName,
            String trainingType);

    List<TrainingResponseDTO> getTrainerTrainings(String username, Date from, Date to, String traineeName);
}
