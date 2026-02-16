package com.example.fitness_service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.fitness_service.dto.response.TrainingTypeResponseDTO;
import com.example.fitness_service.model.TrainingType;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class TrainingTypeMapper {
    
    private final ModelMapper mapper;

    public TrainingTypeResponseDTO toResponseDTO(TrainingType trainingType) {
        return mapper.map(trainingType, TrainingTypeResponseDTO.class);
    }

}
