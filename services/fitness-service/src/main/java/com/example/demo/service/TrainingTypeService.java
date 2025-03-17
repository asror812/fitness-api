package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.mapper.TrainingTypeMapper;
import com.example.demo.model.TrainingType;

import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainingTypeMapper mapper;

    private static final String TRAINING_TYPE_NOT_FOUND_WITH_NAME = "Training type with name %s not found";

    public TrainingType findByName(String name) {
        return trainingTypeDAO.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(TRAINING_TYPE_NOT_FOUND_WITH_NAME.formatted(name)));
    }

    public List<TrainingTypeResponseDTO> getAll() {
        List<TrainingType> all = trainingTypeDAO.getAll();

        return all.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

}
