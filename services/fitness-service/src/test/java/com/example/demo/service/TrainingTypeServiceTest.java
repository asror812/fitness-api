package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.mapper.TrainingTypeMapper;
import com.example.demo.model.TrainingType;

@ExtendWith(MockitoExtension.class)
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

            };

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

}
