package com.example.fitness_service.service;

import com.example.fitness_service.dao.TraineeRepository;
import com.example.fitness_service.dto.response.TraineeResponseDTO;
import com.example.fitness_service.exception.EntityNotFoundException;
import com.example.fitness_service.jms.dto.TraineeRegisterEvent;
import com.example.fitness_service.mapper.TraineeMapper;
import com.example.fitness_service.model.Trainee;
import com.example.fitness_service.model.Training;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TraineeService {

    private static final String TRAINEE_NOT_FOUND_WITH_ID = "Trainee with id %s not found";

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    /*
     * protected TraineeUpdateResponseDTO internalUpdate(TraineeUpdateRequestDTO
     * updateDTO) {
     * UUID id = updateDTO.getId();
     * 
     * Trainee trainee = traineeRepository.findById(id)
     * .orElseThrow(() -> new
     * EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));
     * 
     * traineeMapper.toEntity(updateDTO, trainee);
     * 
     * traineeRepository.save(trainee);
     * return traineeMapper.toUpdateResponseDTO(trainee);
     * }
     */

    public TraineeResponseDTO findById(UUID id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));

        return traineeMapper.toResponseDTO(trainee);
    }

    @Transactional
    public void delete(UUID id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));

        List<Training> trainings = trainee.getTrainings();

        for (Training training : trainings) {
            training.setTrainee(null);
        }

        traineeRepository.delete(trainee);

        for (Training training : trainings) {
            Date trainingDate = training.getTrainingDate();

            if (trainingDate.compareTo(new Date()) > 0) {
            }
        }

    }

    @Transactional
    public boolean createIfNotExists(UUID eventId, TraineeRegisterEvent payload) {
        if (traineeRepository.existsByUserId(payload.getUserId())) {
            return false;
        }

        Trainee trainee = Trainee.builder()
                .address(payload.getAddress())
                .dateOfBirth(payload.getDateOfBirth())
                .userId(payload.getUserId())
                .build();

        traineeRepository.save(trainee);

        return true;
    }

    /*
     * public List<TrainerResponseDTO> getNotAssignedTrainers(UUID id) {
     * Trainee trainee = traineeRepository.findById(id)
     * .orElseThrow(() -> new
     * EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));
     * 
     * Set<Trainer> traineeTrainers = Set.copyOf(trainee.getTrainers());
     * 
     * return trainerRepository.findAll().stream()
     * .filter(trainer -> !traineeTrainers.contains(trainer))
     * .map().toList();
     * }
     */

}
