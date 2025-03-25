package com.example.demo.service;

import com.example.demo.client.TrainerWorkloadClient;
import com.example.demo.client.TrainerWorkloadResponseDTO;
import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.model.Trainer;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;
import io.jsonwebtoken.lang.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO trainerDAO;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private AuthService authService;

    @Mock
    private TrainerMapper trainerMapper;

    @InjectMocks
    private UsernameGeneratorService usernameGeneratorService;

    @InjectMocks
    private TrainerService trainerService;

    @Mock
    private TrainerWorkloadClient client;

    private Trainer trainer;
    private User user;
    private TrainingType trainingType;

    @BeforeEach
    void initialize() {
        user = new User("asror", "r", "asror.r", "password", true);
        trainingType = new TrainingType("Swimming", Collections.emptyList(), Collections.emptyList());
        trainer = new Trainer(user, trainingType, Collections.emptyList(), Collections.emptySet());
    }

    @Test
    void findByUsername_ShouldBe_Ok() {
        when(trainerDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainer));
        when(trainerMapper.toResponseDTO(trainer)).thenReturn(
                new TrainerResponseDTO(new UserResponseDTO("asror", "r", true),
                        new TrainingTypeResponseDTO(UUID.randomUUID(), "Swimming")));

        TrainerResponseDTO response = trainerService.findByUsername("asror.r").get();

        assertNotNull(response);
        verify(trainerDAO).findByUsername("asror.r");
    }

    @Test
    void setStatus_ShouldBe_Ok() {
        when(trainerDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainer));

        trainerService.setStatus("asror.r", false);

        assertFalse(trainer.getUser().getActive());
        verify(userDAO).update(user);
    }

    @Test
    void setStatus_ShouldReturn_IllegalStateException() {

        when(trainerDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainer));

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> trainerService.setStatus("asror.r", true));

        assertEquals("'asror.r' is already true", exception.getMessage());

        verify(userDAO, never()).update(any(User.class));
    }

    @Test
    void register_200() {
        UUID id = UUID.randomUUID();
        TrainerSignUpRequestDTO requestDTO = new TrainerSignUpRequestDTO("asror", "r", id);
        SignUpResponseDTO signUpResponse = new SignUpResponseDTO("asror.r", "password", "qwerty123456");

        when(authService.register(Mockito.any(TrainerSignUpRequestDTO.class))).thenReturn(signUpResponse);
        when(trainingTypeDAO.findById(id)).thenReturn(Optional.of(trainingType));

        SignUpResponseDTO response = trainerService.register(requestDTO);
        assertNotNull(response);

        verify(trainerDAO, times(1)).create(any(Trainer.class));
    }

    @Test
    void internalUpdate_ShouldBe_Ok() {
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO("asror.r", "asror", "r", true,
                UUID.randomUUID());

        when(trainerDAO.findByUsername("asror.r")).thenReturn(Optional.of(trainer));

        trainerService.update(requestDTO);
        verify(trainerDAO, times(1)).update(trainer);
    }

    @Test
    void getTrainerMonthlyWorkloadSummary_ShouldReturnResponse() {
        String username = "asror.r";
        int year = 2023;
        int month = 10;
        TrainerWorkloadResponseDTO workloadResponse = new TrainerWorkloadResponseDTO();

        when(client.getTrainerMonthlyWorkloadSummary(username, year, month)).thenReturn(workloadResponse);

        TrainerWorkloadResponseDTO response = trainerService.getTrainerMonthlyWorkloadSummary(username, year, month);

        assertNotNull(response);
        verify(client, times(1)).getTrainerMonthlyWorkloadSummary(username, year, month);
    }

}
