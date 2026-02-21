package com.example.fitness_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.fitness_service.dto.request.TraineeSignUpRequestDTO;
import com.example.fitness_service.dto.response.TraineeResponseDTO;
import com.example.fitness_service.dto.response.TraineeUpdateResponseDTO;
import com.example.fitness_service.model.Trainee;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "trainers", ignore = true)
    @Mapping(target = "trainings", ignore = true)
    public Trainee toEntity(TraineeSignUpRequestDTO createDto);

    // public void toEntity(TraineeUpdateRequestDTO updateDto, Trainee trainee);

    public TraineeResponseDTO toResponseDTO(Trainee trainee);

    @Mapping(target = "user", ignore = true)
    public TraineeUpdateResponseDTO toUpdateResponseDTO(Trainee trainee);

}