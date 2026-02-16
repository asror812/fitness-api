package com.example.fitness_service.service;

import com.example.fitness_service.dao.TrainerRepository;
import com.example.fitness_service.dao.TrainingTypeRepostiory;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.exception.EntityNotFoundException;
import com.example.fitness_service.jms.TrainerWorkloadJmsProducer;
import com.example.fitness_service.mapper.TrainerMapper;
import com.example.fitness_service.model.Trainer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper mapper;
    private final TrainingTypeRepostiory trainingTypeRepostiory;
    private final TrainerWorkloadJmsProducer consumer;

    private static final String TRAINER_NOT_FOUND_WITH_USERNAME = "Trainer with username %s not found";

    public TrainerResponseDTO findById(UUID id) {
        Trainer trainer = trainerRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND_WITH_USERNAME.formatted(id)));

        return mapper.toResponseDTO(trainer);
    }

/*     @Transactional
    public SignUpResponseDTO register(TrainerSignUpRequestDTO requestDTO) {
        SignUpResponseDTO register = authService.register(requestDTO);

        UUID specialization = requestDTO.getSpecialization();

        TrainingType trainingType = trainingTypeRepostiory.findById(specialization)
                .orElseThrow(
                        () -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_WITH_ID.formatted(specialization)));

        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingType);
        trainer.setUserId(UUID.randomUUID());

        trainerRepository.save(trainer);
        return register;
    } */
}
