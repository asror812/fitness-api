package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.dao.TraineeDAO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.TrainingDAO;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.dto.request.TrainingCreateRequestDTO;
import com.example.demo.dto.response.TrainingResponseDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mapper.TrainingMapper;
import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.Training;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;
import com.example.demo.client.*;
import io.jsonwebtoken.lang.Collections;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainingService trainingService;

    @Mock
    private TrainingDAO trainingDAO;

    @Mock
    private TrainingMapper mapper;

    @Mock
    private TrainerWorkloadClient workloadClient;

    private Trainer trainer;
    private Trainee trainee;
    private User user;
    private TrainingType trainingType;

    @BeforeEach
    void initialize() {
        user = new User("asror", "r", "asror.r", "password1234", true);
        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress("T");
        trainee.setDateOfBirth(new Date());

        trainingType = new TrainingType("Swimming", Collections.emptyList(), Collections.emptyList());
        user.setUsername("abror.r");
        trainer = new Trainer(user, trainingType, new ArrayList<>(), new HashSet<>());
    }

    @Test
    void create() {
        TrainingCreateRequestDTO createDTO = new TrainingCreateRequestDTO(
                "asror.r", "abror.r", "Swimming", new Date(),
                1.5);

        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("abror.r")).thenReturn(Optional.of(trainer));

        trainingService.create(createDTO);

        verify(trainingDAO, times(1)).create(any(Training.class));
    }

    @Test
    void getTraineeTrainings_ShouldReturn_Trainings() {
        List<Training> trainings = getTrainingList();
        when(trainingDAO.findTraineeTrainings("asror.r", null, null, null, null)).thenReturn(trainings);

        List<TrainingResponseDTO> expectedList = trainings.stream()
                .map(t -> new TrainingResponseDTO())
                .toList();

        for (int i = 0; i < trainings.size(); i++) {
            when(mapper.toResponseDTO(trainings.get(i))).thenReturn(expectedList.get(i));
        }

        List<TrainingResponseDTO> result = trainingService.getTraineeTrainings("asror.r", null, null, null, null);

        assertEquals(expectedList.size(), result.size());
        verify(trainingDAO).findTraineeTrainings("asror.r", null, null, null, null);
        verify(mapper, times(result.size())).toResponseDTO(any(Training.class));
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainings() {
        List<Training> trainings = getTrainingList();
        when(trainingDAO.findTrainerTrainings("asror.r", null, null, null)).thenReturn(trainings);

        List<TrainingResponseDTO> expectedList = trainings.stream()
                .map(t -> new TrainingResponseDTO())
                .toList();

        for (int i = 0; i < trainings.size(); i++) {
            when(mapper.toResponseDTO(trainings.get(i))).thenReturn(expectedList.get(i));
        }

        List<TrainingResponseDTO> result = trainingService.getTrainerTrainings("asror.r", null, null, null);

        assertEquals(expectedList.size(), result.size());
        verify(trainingDAO).findTrainerTrainings("asror.r", null, null, null);
        verify(mapper, times(result.size())).toResponseDTO(any(Training.class));
    }

    private List<Training> getTrainingList() {
        List<Training> trainings = new ArrayList<>();

        trainings.add(new Training(trainee, trainer, "swimming", trainingType, new Date(), 1.5));
        trainings.add(new Training(trainee, trainer, "swimming", trainingType, new Date(), 1.5));

        return trainings;
    }

    @Test
    void delete_ShouldDeleteTraining() {
        UUID trainingId = UUID.randomUUID();
        Training training = new Training(trainee, trainer, "swimming", trainingType, new Date(), 1.5);

        when(trainingDAO.findById(trainingId)).thenReturn(Optional.of(training));

        trainingService.delete(trainingId);

        verify(trainingDAO).findById(trainingId);
        verify(workloadClient).updateTrainingSession(any(TrainerWorkloadRequestDTO.class));
        verify(trainingDAO).delete(trainingId);
    }

    @Test
    void delete_ShouldThrowException(){
        UUID trainingId = UUID.randomUUID();

        when(trainingDAO.findById(trainingId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> trainingService.delete(trainingId));

        assertEquals("Training with ID %s not found".formatted(trainingId), exception.getMessage());
        verify(trainingDAO).findById(trainingId);
        verify(workloadClient, times(0)).updateTrainingSession(any(TrainerWorkloadRequestDTO.class));
        verify(trainingDAO, times(0)).delete(trainingId);
    }
}