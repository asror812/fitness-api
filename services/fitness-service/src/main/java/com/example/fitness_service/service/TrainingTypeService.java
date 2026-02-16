package com.example.fitness_service.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.fitness_service.dao.TrainingTypeRepostiory;
import com.example.fitness_service.dto.request.TrainingTypeCreateDTO;
import com.example.fitness_service.dto.response.TrainingTypeResponseDTO;
import com.example.fitness_service.exception.AlreadyExistException;
import com.example.fitness_service.exception.EntityNotFoundException;
import com.example.fitness_service.mapper.TrainingTypeMapper;
import com.example.fitness_service.model.TrainingType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeRepostiory trainingTypeRepostiory;
    private final TrainingTypeMapper mapper;

    private static final String TRAINING_TYPE_NOT_FOUND_WITH_NAME = "Training type with name %s not found";
    private static final String TRAINING_TYPE_ALREADY_EXISTS_WITH_NAME = "Training type with name %s already exists";

    public TrainingType findById(UUID id) {
        return trainingTypeRepostiory.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_WITH_NAME.formatted(id)));
    }

    public List<TrainingTypeResponseDTO> getAll() {
        List<TrainingType> all = trainingTypeRepostiory.findAll();

        return all.stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public TrainingTypeResponseDTO create(TrainingTypeCreateDTO createDTO) {
        String name = createDTO.getName();

        trainingTypeRepostiory
                .findByName(createDTO.getName()).orElseThrow(() -> new AlreadyExistException(
                        TRAINING_TYPE_ALREADY_EXISTS_WITH_NAME.formatted(createDTO.getName())));

        TrainingType trainingType = trainingTypeRepostiory
                .save(new TrainingType(createDTO.getName(), Collections.emptyList(), Collections.emptyList()));

        return new TrainingTypeResponseDTO(trainingType.getId(), name);
    }

}
