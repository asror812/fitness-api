package com.example.fitness_service.service;

import com.example.fitness_service.dao.TrainerRepository;
import com.example.fitness_service.dao.TrainingTypeRepostiory;
import com.example.fitness_service.jms.dto.TrainerRegisterEvent;
import com.example.fitness_service.jms.outbox.OutboxPublisher;
import com.example.fitness_service.model.Trainer;
import com.example.fitness_service.model.TrainingType;

import com.example.fitness_service.exception.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepostiory trainingTypeRepostiory;
    private final OutboxPublisher publisher;

    public boolean createIfNotExists(TrainerRegisterEvent payload) {
        if (trainerRepository.existsByUserId(payload.getUserId())) {
            return false;
        }

        UUID trainingTypeId = payload.getSpecialization();

        TrainingType trainingType = trainingTypeRepostiory.findById(trainingTypeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Training type with id: %s not found".formatted(trainingTypeId)));

        Trainer trainer = Trainer.builder()
                .userId(payload.getUserId())
                .specialization(trainingType)
                .build();

        trainerRepository.save(trainer);

        return true;
    }

    /*
     * public TrainerResponseDTO findById(UUID id) {
     * Trainer trainer = trainerRepository
     * .findById(id)
     * .orElseThrow(() -> new
     * EntityNotFoundException(TRAINER_NOT_FOUND_WITH_USERNAME.formatted(id)));
     * 
     * return mapper.toResponseDTO(trainer);
     * }
     */
    /*
     * @Transactional
     * public SignUpResponseDTO register(TrainerSignUpRequestDTO requestDTO) {
     * SignUpResponseDTO register = authService.register(requestDTO);
     * 
     * UUID specialization = requestDTO.getSpecialization();
     * 
     * TrainingType trainingType = trainingTypeRepostiory.findById(specialization)
     * .orElseThrow(
     * () -> new EntityNotFoundException(TRAINING_TYPE_NOT_FOUND_WITH_ID.formatted(
     * specialization)));
     * 
     * Trainer trainer = new Trainer();
     * trainer.setSpecialization(trainingType);
     * trainer.setUserId(UUID.randomUUID());
     * 
     * trainerRepository.save(trainer);
     * return register;
     * }
     */
}
