package com.example.fitness_service.mapper;

import org.mapstruct.Mapper;

import com.example.fitness_service.dto.response.TrainingTypeResponseDTO;
import com.example.fitness_service.model.TrainingType;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    public TrainingTypeResponseDTO toResponseDTO(TrainingType trainingType);

}
