package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.demo.dao.TraineeDAO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.request.ActionType;
import com.example.demo.dto.request.TraineeSignUpRequestDTO;
import com.example.demo.dto.request.TraineeTrainersUpdateRequestDTO;
import com.example.demo.dto.request.TraineeUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TraineeResponseDTO;
import com.example.demo.dto.response.TraineeUpdateResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.dto.response.UserUpdateResponseDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.jms.TrainerWorkloadJmsProducer;
import com.example.demo.mapper.TraineeMapper;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.metric.TraineeSignUpRequestCountMetrics;
import com.example.demo.model.Trainee;
import com.example.demo.model.Trainer;
import com.example.demo.model.Training;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;
import io.jsonwebtoken.lang.Collections;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO traineeDAO;

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthService authService;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainerWorkloadJmsProducer producer;

    @InjectMocks
    private TraineeService traineeService;

    @Mock
    private TraineeSignUpRequestCountMetrics signUpRequestCountMetrics;

    private Trainee trainee;

    private TrainingType trainingType;

    private User user;

    private Trainer trainer;

    @BeforeEach
    void initialize() {
        user = new User("asror", "r", "asror.r", "password1234", true);
        trainee = new Trainee();
        trainee.setAddress("T");
        trainee.setDateOfBirth(new Date());
        trainee.setUser(user);

        trainingType = new TrainingType("swimming", new ArrayList<>(), new ArrayList<>());

        trainer = new Trainer();
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);
    }

    @Test
    void register_ShouldSetStatus_ShouldBeSuccessful() {
        TraineeSignUpRequestDTO requestDTO = new TraineeSignUpRequestDTO("asror", "r", new Date(), "T");

        SignUpResponseDTO responseDTO = new SignUpResponseDTO("asror.r", "password", "qwerty");

        when(authService.register(Mockito.any(TraineeSignUpRequestDTO.class))).thenReturn(responseDTO);

        SignUpResponseDTO result = traineeService.register(requestDTO);

        assertEquals("asror.r", result.getUsername());
        assertNotNull(result);
        verify(traineeDAO, times(1)).create(any(Trainee.class));
    }

    @Test
    void findByUsername_ShouldReturnTrainee() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        when(traineeMapper.toResponseDTO(trainee))
                .thenReturn(new TraineeResponseDTO(new UserResponseDTO("asror", "r", true), new Date(), "T",
                        Collections.emptyList()));

        TraineeResponseDTO result = traineeService.findByUsername("asror.r");

        assertNotNull(result);
        verify(traineeDAO, times(1)).findByUsername("asror.r");
    }

    @Test
    void findByUsername_EntityNotFoundException() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.findByUsername("asror.r"));

        assertEquals("Trainee with username asror.r not found", ex.getMessage());
    }

    @Test
    void delete_EntityNotFoundException() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.delete("asror.r"));

        assertEquals("Trainee with username asror.r not found", ex.getMessage());
    }

    @Test
    void setStatus_ShouldBe_Ok() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        traineeService.setStatus("asror.r", false);
        verify(userDAO, times(1)).update(user);
    }

    @Test
    void setStatus_EntityNotFoundException() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.setStatus("asror.r", false));
        assertEquals("Trainee with username asror.r not found", ex.getMessage());
    }

    @Test
    void setStatus_ShouldReturn_IllegalStateException() {
        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> traineeService.setStatus("asror.r", true));

        assertEquals("'asror.r's status already set true", exception.getMessage());

        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void getNotAssignedTrainers_ShouldReturnTrainers() {
        Trainer trainer1 = new Trainer(user, trainingType, new ArrayList<>(), new HashSet<>());
        Trainer trainer2 = new Trainer(user, trainingType, new ArrayList<>(), new HashSet<>());

        Set<Trainer> assignedTrainers = Set.of(trainer1);
        List<Trainer> allTrainers = List.of(trainer1, trainer2);

        trainee.setTrainers(assignedTrainers);

        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        when(trainerDAO.getAll()).thenReturn(allTrainers);

        List<TrainerResponseDTO> result = traineeService.getNotAssignedTrainers("asror.r");

        assertEquals(1, result.size());
        verify(trainerDAO, times(1)).getAll();
    }

    @Test
    void getNotAssignedTrainers_EntityNotFoundException() {

        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> traineeService.getNotAssignedTrainers("asror.r"));

        assertEquals("Trainee with username asror.r not found", ex.getMessage());
    }

    @Test
    void internalUpdate() {
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO("asror.r", "asror", "r", true, new Date(),
                "T");

        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        traineeService.update(requestDTO);
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void updateTraineeTrainers_ShouldUpdateTrainersSuccessfully() {
        String username = "asror.r";
        TraineeTrainersUpdateRequestDTO requestDTO = new TraineeTrainersUpdateRequestDTO(
                List.of(new TraineeTrainersUpdateRequestDTO.TrainerDTO("trainer1"),
                        new TraineeTrainersUpdateRequestDTO.TrainerDTO("trainer2")));

        Trainer trainer1 = new Trainer(new User("trainer1", "last1", "trainer1", "password", true), trainingType,
                new ArrayList<>(), new HashSet<>());
        Trainer trainer2 = new Trainer(new User("trainer2", "last2", "trainer2", "password", true), trainingType,
                new ArrayList<>(), new HashSet<>());

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("trainer1")).thenReturn(Optional.of(trainer1));
        when(trainerDAO.findByUsername("trainer2")).thenReturn(Optional.of(trainer2));
        when(trainerMapper.toResponseDTO(trainer1)).thenReturn(new TrainerResponseDTO());
        when(trainerMapper.toResponseDTO(trainer2)).thenReturn(new TrainerResponseDTO());

        List<TrainerResponseDTO> result = traineeService.updateTraineeTrainers(username, requestDTO);

        assertEquals(2, result.size());
        verify(traineeDAO, times(1)).findByUsername(username);
        verify(trainerDAO, times(1)).findByUsername("trainer1");
        verify(trainerDAO, times(1)).findByUsername("trainer2");
        verify(traineeDAO, times(1)).update(trainee);
    }

    @Test
    void updateTraineeTrainers_ShouldThrowEntityNotFoundException_WhenTraineeNotFound() {
        String username = "nonexistent";
        TraineeTrainersUpdateRequestDTO requestDTO = new TraineeTrainersUpdateRequestDTO(
                List.of(new TraineeTrainersUpdateRequestDTO.TrainerDTO("trainer1")));

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTraineeTrainers(username, requestDTO));

        assertEquals("Trainee with username nonexistent not found", exception.getMessage());
        verify(traineeDAO, times(1)).findByUsername(username);
        verify(trainerDAO, never()).findByUsername(any());
        verify(traineeDAO, never()).update(any());
    }

    @Test
    void updateTraineeTrainers_ShouldThrowEntityNotFoundException() {
        String username = "asror.r";
        TraineeTrainersUpdateRequestDTO requestDTO = new TraineeTrainersUpdateRequestDTO(
                List.of(new TraineeTrainersUpdateRequestDTO.TrainerDTO("nonexistentTrainer")));

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));
        when(trainerDAO.findByUsername("nonexistentTrainer")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTraineeTrainers(username, requestDTO));

        assertEquals("Trainer with username nonexistentTrainer not found", exception.getMessage());
        verify(traineeDAO, times(1)).findByUsername(username);
        verify(trainerDAO, times(1)).findByUsername("nonexistentTrainer");
        verify(traineeDAO, never()).update(any());
    }

    @Test
    void internalUpdate_ShouldUpdateTraineeSuccessfully() {
        TraineeUpdateRequestDTO updateDTO = new TraineeUpdateRequestDTO("asror.r", "asror", "r", true, new Date(), "T");

        TraineeUpdateResponseDTO responseDTO = new TraineeUpdateResponseDTO();
        responseDTO.setUser(new UserUpdateResponseDTO("asror.r", "asror", "r", true));
        responseDTO.setDateOfBirth(new Date());
        responseDTO.setAddress("T");
        responseDTO.setTrainers(List.of());

        when(traineeDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainee));
        when(traineeMapper.toUpdateResponseDTO(trainee))
                .thenReturn(responseDTO);

        TraineeUpdateResponseDTO result = traineeService.update(updateDTO);

        assertNotNull(result);
        assertEquals("asror.r", result.getUser().getUsername());
        verify(traineeDAO, times(1)).findByUsername("asror.r");
        verify(traineeMapper, times(1)).toEntity(updateDTO, trainee);
        verify(traineeDAO, times(1)).update(trainee);
        verify(traineeMapper, times(1)).toUpdateResponseDTO(trainee);
    }

    @Test
    void internalUpdate_ShouldThrowEntityNotFoundException() {
        TraineeUpdateRequestDTO updateDTO = new TraineeUpdateRequestDTO("nonexistent", "asror", "r", true, new Date(),
                "T");

        when(traineeDAO.findByUsername("nonexistent")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> traineeService.update(updateDTO));

        assertEquals("Trainee with username nonexistent not found", exception.getMessage());
        verify(traineeDAO, times(1)).findByUsername("nonexistent");
        verify(traineeMapper, never()).toEntity(any(), any());
        verify(traineeDAO, never()).update(any());
        verify(traineeMapper, never()).toUpdateResponseDTO(any());
    }

    @Test
    void delete_ShouldRemoveTraineeAndNotifyTrainersForFutureTrainings() {
        String username = "asror.r";

        Date now = new Date();
        Date futureDate = new Date(now.getTime() + 86400000); // +1 day
        Date pastDate = new Date(now.getTime() - 86400000); // -1 day

        Training futureTraining = new Training();
        futureTraining.setTrainingDate(futureDate);
        futureTraining.setDuration(1.5);
        futureTraining.setTrainee(trainee);
        futureTraining.setTrainer(trainer);

        Training pastTraining = new Training();
        pastTraining.setTrainingDate(pastDate);
        pastTraining.setDuration(2.0);
        pastTraining.setTrainee(trainee);
        pastTraining.setTrainer(trainer);

        List<Training> trainings = new ArrayList<>();
        trainings.add(futureTraining);
        trainings.add(pastTraining);

        trainee.setTrainings(trainings);

        when(traineeDAO.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.delete(username);

        verify(traineeDAO, times(1)).delete(trainee);
        verify(producer, times(1))
                .updateTrainingSession(Mockito.argThat(req -> req.getTrainerUsername().equals(user.getUsername()) &&
                        req.getActionType() == ActionType.DELETE &&
                        req.getTrainingDate().equals(
                                LocalDate.parse(new java.text.SimpleDateFormat("yyyy-MM-dd").format(futureDate)))));

        verify(producer, times(1)).updateTrainingSession(any());
    }

}