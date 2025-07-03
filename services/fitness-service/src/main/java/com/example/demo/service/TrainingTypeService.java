package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.request.TrainingTypeCreateDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.model.TrainingType;

public interface TrainingTypeService {
    TrainingType findByName(String name);

    TrainingTypeResponseDTO create(TrainingTypeCreateDTO createDTO);

    List<TrainingTypeResponseDTO> getAll();
}