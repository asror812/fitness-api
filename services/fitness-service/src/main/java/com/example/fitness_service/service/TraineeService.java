package com.example.fitness_service.service;

import com.example.fitness_service.dao.TraineeRepository;
import com.example.fitness_service.dao.TrainerRepository;
import com.example.fitness_service.dto.request.*;
import com.example.fitness_service.dto.response.TraineeResponseDTO;
import com.example.fitness_service.dto.response.TraineeUpdateResponseDTO;
import com.example.fitness_service.dto.response.TrainerResponseDTO;
import com.example.fitness_service.exception.EntityNotFoundException;
import com.example.fitness_service.jms.TrainerWorkloadJmsProducer;
import com.example.fitness_service.mapper.TraineeMapper;
import com.example.fitness_service.mapper.TrainerMapper;
import com.example.fitness_service.metric.TraineeSignUpRequestCountMetrics;
import com.example.fitness_service.model.Trainee;
import com.example.fitness_service.model.Trainer;
import com.example.fitness_service.model.Training;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Getter
public class TraineeService {

    private static final String TRAINEE_NOT_FOUND_WITH_ID = "Trainee with id %s not found";
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper mapper;
    private final TrainerMapper trainerMapper;
    private final TrainerWorkloadJmsProducer producer;
    private final ObjectMapper objectMapper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final TraineeSignUpRequestCountMetrics signUpRequestCountMetrics;

    protected TraineeUpdateResponseDTO internalUpdate(TraineeUpdateRequestDTO updateDTO) {
        UUID id = updateDTO.getId();

        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));

        mapper.toEntity(updateDTO, trainee);

        traineeRepository.save(trainee);
        return mapper.toUpdateResponseDTO(trainee);
    }

    public TraineeResponseDTO findById(UUID id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));

        return mapper.toResponseDTO(trainee);
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

    public List<TrainerResponseDTO> getNotAssignedTrainers(UUID id) {
        Trainee trainee = traineeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRAINEE_NOT_FOUND_WITH_ID.formatted(id)));

        Set<Trainer> traineeTrainers = Set.copyOf(trainee.getTrainers());

        return trainerRepository.findAll().stream()
                .filter(trainer -> !traineeTrainers.contains(trainer))
                .map(trainerMapper::toResponseDTO).toList();
    }

}
