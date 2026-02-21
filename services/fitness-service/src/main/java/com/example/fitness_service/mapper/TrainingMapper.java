package com.example.fitness_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.fitness_service.dto.request.TrainingCreateRequestDTO;
import com.example.fitness_service.dto.response.TraineeTrainingResponseDTO;
import com.example.fitness_service.dto.response.TrainerTrainingResponseDTO;
import com.example.fitness_service.dto.response.TrainingResponseDTO;
import com.example.fitness_service.model.Training;

@Mapper(componentModel = "spring")
public interface TrainingMapper {

    @Mapping(target = "datetimeCreated", ignore = true)
    @Mapping(target = "datetimeUpdated", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trainer", ignore = true)
    @Mapping(target = "trainee", ignore = true)
    @Mapping(target = "trainingType", ignore = true)
    public Training toEntity(TrainingCreateRequestDTO createDto);

    public TrainingResponseDTO toResponseDTO(Training training);

    @Mapping(target = "traineeId", ignore = true)
    public TrainerTrainingResponseDTO toTrainerTrainingResponseDTO(Training training);

    @Mapping(target = "trainerId", ignore = true)
    public TraineeTrainingResponseDTO toTraineeTrainingResponseDTO(Training training);

}