package com.example.fitness_service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import com.example.fitness_service.dto.request.TrainingCreateRequestDTO;
import com.example.fitness_service.dto.response.TraineeTrainingResponseDTO;
import com.example.fitness_service.dto.response.TrainerTrainingResponseDTO;
import com.example.fitness_service.dto.response.TrainingResponseDTO;
import com.example.fitness_service.model.Training;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainingMapper {

    private final ModelMapper modelMapper;

    public Training toEntity(TrainingCreateRequestDTO createDto) {
        return modelMapper.map(createDto, Training.class);
    }

    public TrainingResponseDTO toResponseDTO(Training training) {
        return modelMapper.map(training, TrainingResponseDTO.class);
    }

    public TrainerTrainingResponseDTO toTrainerTrainingResponseDTO(Training training) {
        return modelMapper.map(training, TrainerTrainingResponseDTO.class);
    }

    public TraineeTrainingResponseDTO toTraineeTrainingResponseDTO(Training training) {
        return modelMapper.map(training, TraineeTrainingResponseDTO.class);
    }

}