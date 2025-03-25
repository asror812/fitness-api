package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.TrainingTypeCreateDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.mapper.TrainingTypeMapper;
import com.example.demo.model.TrainingType;
import com.example.demo.exceptions.AlreadyExistException;
import com.example.demo.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainingTypeService {

    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainingTypeMapper mapper;

    private static final String TRAINING_TYPE_NOT_FOUND_WITH_NAME = "Training type with name %s not found";
    private static final String TRAINING_TYPE_ALREADY_EXISTS_WITH_NAME = "Training type with name %s already exists";

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

    public TrainingTypeResponseDTO create(TrainingTypeCreateDTO createDTO) {
        String name = createDTO.getName();

        Optional<TrainingType> existingTrainingType = trainingTypeDAO.findByName(createDTO.getName());

        if (existingTrainingType.isPresent()) {
            throw new AlreadyExistException(TRAINING_TYPE_ALREADY_EXISTS_WITH_NAME.formatted(createDTO.getName()));
        }

        TrainingType trainingType = trainingTypeDAO.create(new TrainingType(createDTO.getName(), Collections.emptyList(), Collections.emptyList()));

        return new TrainingTypeResponseDTO(trainingType.getId(), name);
    }

}
