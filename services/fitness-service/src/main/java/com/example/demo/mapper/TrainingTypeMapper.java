package com.example.demo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.model.TrainingType;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class TrainingTypeMapper {
    
    private final ModelMapper mapper;

    public TrainingTypeResponseDTO toResponseDTO(TrainingType trainingType) {
        return mapper.map(trainingType, TrainingTypeResponseDTO.class);
    }

}
