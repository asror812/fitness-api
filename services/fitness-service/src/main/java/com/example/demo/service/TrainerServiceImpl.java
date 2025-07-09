package com.example.demo.service;

import com.example.demo.dao.TrainerDAO;
import com.example.demo.dao.TrainingTypeDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.request.TrainerSignUpRequestDTO;
import com.example.demo.dto.request.TrainerUpdateRequestDTO;
import com.example.demo.dto.response.SignUpResponseDTO;
import com.example.demo.dto.response.TrainerResponseDTO;
import com.example.demo.dto.response.TrainerUpdateResponseDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.jms.TrainerWorkloadJmsProducer;
import com.example.demo.mapper.TrainerMapper;
import com.example.demo.model.Trainer;
import com.example.demo.model.TrainingType;
import com.example.demo.model.User;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class TrainerServiceImpl extends
        AbstractGenericService<Trainer, TrainerSignUpRequestDTO, TrainerUpdateRequestDTO, TrainerResponseDTO, TrainerUpdateResponseDTO>
        implements TrainerService {

    private final UsernameGeneratorService usernameGeneratorService;
    private final AuthService authService;
    private final TrainerDAO dao;
    private final Class<Trainer> entityClass = Trainer.class;
    private final TrainerMapper mapper;
    private final UserDAO userDAO;
    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainerWorkloadJmsProducer consumer;

    private static final String TRAINER_NOT_FOUND_WITH_USERNAME = "Trainer with username %s not found";
    private static final String TRAINING_TYPE_NOT_FOUND_WITH_ID = "Training type with id %s not found";
    private final UserService userService;

    @Override
    public TrainerResponseDTO findByUsername(String username) {
        Optional<Trainer> existingTrainer = dao.findByUsername(username);

        if (existingTrainer.isEmpty()) {
            throw new EntityNotFoundException(TRAINER_NOT_FOUND_WITH_USERNAME.formatted(username));
        }

        return mapper.toResponseDTO(existingTrainer.get());
    }

    @Override
    @Transactional
    public void setStatus(String username, boolean status) {
        userService.setStatus(username, status, "trainer");
    }

    @Override
    @Transactional
    public SignUpResponseDTO register(TrainerSignUpRequestDTO requestDTO) {
        SignUpResponseDTO register = authService.register(requestDTO);

        User user = new User(requestDTO.getFirstName(), requestDTO.getLastName(), register.getUsername(),
                register.getPassword(), true);

        UUID specialization = requestDTO.getSpecialization();

        TrainingType trainingType = trainingTypeDAO.findById(specialization)
                .orElseThrow(
                        () -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_WITH_ID.formatted(specialization)));

        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);

        dao.create(trainer);
        return register;
    }

    @Override
    @Transactional
    protected TrainerUpdateResponseDTO internalUpdate(TrainerUpdateRequestDTO updateDTO) {
        String username = updateDTO.getUsername();
        Trainer trainer = dao.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_WITH_USERNAME.formatted(username)));
        mapper.toEntity(updateDTO, trainer);
        dao.update(trainer);

        return mapper.toUpdateResponseDTO(trainer);
    }
}
