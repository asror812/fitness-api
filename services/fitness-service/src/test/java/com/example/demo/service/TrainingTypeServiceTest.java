package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.TrainingTypeCreateDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.exceptions.AlreadyExistException;
import com.example.demo.mapper.TrainingTypeMapper;
import com.example.demo.model.TrainingType;

import io.jsonwebtoken.lang.Collections;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingTypeServiceTest {

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private TrainingTypeMapper mapper;

    @Test
    void findByName_ShouldReturn_TrainingType() {
        when(trainingTypeDAO.findByName("swimming")).thenReturn(Optional.of(new TrainingType()));
        TrainingType byName = trainingTypeService.findByName("swimming");

        assertNotNull(byName);
    }

    @Test
    void getAll_ShouldReturn_TrainingTypes() {
        List<TrainingType> trainingTypes = new ArrayList<>() {
            {
                add(new TrainingType());
                add(new TrainingType());
            }
        };

        List<TrainingTypeResponseDTO> trainings = trainingTypes.stream().map(t -> new TrainingTypeResponseDTO())
                .toList();

        when(trainingTypeDAO.getAll()).thenReturn(trainingTypes);

        for (int i = 0; i < trainingTypes.size(); i++) {
            when(mapper.toResponseDTO(trainingTypes.get(i))).thenReturn(trainings.get(i));
        }

        List<TrainingTypeResponseDTO> all = trainingTypeService.getAll();

        assertEquals(2, all.size());
    }

    @Test
    void create_ShouldReturn_TrainingTypeResponseDTO() {
        TrainingTypeCreateDTO createDTO = new TrainingTypeCreateDTO("yoga");
        TrainingType trainingType = new TrainingType("yoga", Collections.emptyList(), Collections.emptyList());
        trainingType.setId(UUID.randomUUID());

        when(trainingTypeDAO.findByName(createDTO.getName())).thenReturn(Optional.empty());
        when(trainingTypeDAO.create(any(TrainingType.class))) // Fix here
                .thenReturn(trainingType);

        TrainingTypeResponseDTO responseDTO = trainingTypeService.create(createDTO);

        assertNotNull(responseDTO);
        assertEquals("yoga", responseDTO.getTrainingTypeName());
    }

    @Test
    void create_ShouldThrow_AlreadyExistException() {
        TrainingTypeCreateDTO createDTO = new TrainingTypeCreateDTO("yoga");
        TrainingType existingTrainingType = new TrainingType("yoga", Collections.emptyList(), Collections.emptyList());

        when(trainingTypeDAO.findByName("yoga")).thenReturn(Optional.of(existingTrainingType));

        AlreadyExistException exception = org.junit.jupiter.api.Assertions.assertThrows(
                AlreadyExistException.class,
                () -> trainingTypeService.create(createDTO));

        assertEquals("Training type with name yoga already exists", exception.getMessage());
    }
}