package com.example.demo.mapper;

import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;
import com.example.demo.dto.response.TrainingTypeResponseDTO;
import com.example.demo.dto.response.UserResponseDTO;
import com.example.demo.dto.response.UserUpdateResponseDTO;
import com.example.demo.model.Trainer;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class TrainerMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TrainingTypeDAO trainingTypeDAO;

    @InjectMocks
    private TrainerMapper trainerMapper;

    @Test
    void toResponseDTO() {
        Trainer trainer = new Trainer();
        TrainerResponseDTO expectedDto = new TrainerResponseDTO(
                new UserResponseDTO("asror", "r", true),
                new TrainingTypeResponseDTO());

        when(modelMapper.map(trainer, TrainerResponseDTO.class)).thenReturn(expectedDto);

        TrainerResponseDTO dto = trainerMapper.toResponseDTO(trainer);

        assertNotNull(dto);
        assertEquals("asror", dto.getUser().getFirstName());
    }

    @Test
    void toEntity() {
        TrainerSignUpRequestDTO requestDTO = new TrainerSignUpRequestDTO("asror", "r", UUID.randomUUID());
        Trainer trainer = new Trainer(new User(), new TrainingType(), Collections.emptyList(), Collections.emptySet());
        when(modelMapper.map(requestDTO, Trainer.class)).thenReturn(trainer);

        Trainer trainer2 = trainerMapper.toEntity(requestDTO);

        assertNotNull(trainer2);
        assertEquals(0, trainer2.getTrainees().size());
    }

    @Test
    void toUpdateResponseDTO() {
        Trainer trainer = new Trainer();
        TrainerUpdateResponseDTO expectedDto = new TrainerUpdateResponseDTO(
                new UserUpdateResponseDTO("asror.r", "asror", "r", true),
                new TrainingTypeResponseDTO(),
                Collections.emptyList());

        when(modelMapper.map(trainer, TrainerUpdateResponseDTO.class)).thenReturn(expectedDto);

        TrainerUpdateResponseDTO dto = trainerMapper.toUpdateResponseDTO(trainer);

        assertNotNull(dto);
        assertEquals("asror.r", dto.getUser().getUsername());
    }
}
