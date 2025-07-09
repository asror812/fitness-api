package com.example.demo.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dao.TrainerWorkloadRepository;
import com.example.demo.dto.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.model.WorkingMonth;
import com.example.demo.model.WorkingYear;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadServiceImpl.class);
    private final TrainerWorkloadRepository repository;

    private static final String NO_WORKLOAD = "No workload found for trainer '{}' year: {} month: {}";
    private static final String SUCCESSFULLY_TRAINING_SESSION_ADDED = "Added {} hours to trainer: {} for year: {} month: {}";
    private static final String SUCCESSFULLY_TRAINING_SESSION_REMOVED = "Removed {} hours from trainer: {} for year: {} month: {}";
    private static final String NOT_ENOUGH_HOURS = "Trainer: {} does not have enough hours to remove {} hours for year: {} month: {}";
    private static final String NO_WORKLOAD_EXCEPTION_MESSAGE = "No workload found for trainer '%s' year: %s month: %s";

    private void addTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String username = requestDTO.getTrainerUsername();
        LocalDate trainingDate = requestDTO.getTrainingDate();
        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        TrainerWorkload workload = repository.findById(username).orElseGet(() -> {
            TrainerWorkload newWorkload = new TrainerWorkload();
            newWorkload.setUsername(username);
            newWorkload.setFirstName(requestDTO.getTrainerFirstName());
            newWorkload.setLastName(requestDTO.getTrainerLastName());
            return newWorkload;
        });

        WorkingYear year = workload.getYears().stream().filter(y -> y.getYear() == yearValue).findFirst()
                .orElseGet(() -> {
                    WorkingYear newYear = new WorkingYear(yearValue, new ArrayList<>());
                    workload.getYears().add(newYear);
                    return newYear;
                });

        WorkingMonth month = year.getMonthlyWorkload().stream().filter(m -> m.getMonth().getValue() == monthValue)
                .findFirst().orElseGet(() -> {
                    WorkingMonth newMonth = WorkingMonth.builder().month(Month.of(monthValue)).build();
                    year.getMonthlyWorkload().add(newMonth);
                    return newMonth;
                });

        month.setTotalHours(month.getTotalHours() + duration);
        repository.save(workload);

        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_ADDED, duration, username, yearValue, monthValue);
    }

    private void removeTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String username = requestDTO.getTrainerUsername();
        LocalDate trainingDate = requestDTO.getTrainingDate();
        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        TrainerWorkload workload = repository.findById(username).orElseThrow(() -> {
            LOGGER.error(NO_WORKLOAD, username, yearValue, monthValue);
            return new EntityNotFoundException("Trainer workload not found for username: " + username);
        });

        WorkingYear year = workload.getYears().stream().filter(y -> y.getYear() == yearValue).findFirst()
                .orElseThrow(() -> {
                    LOGGER.warn(NO_WORKLOAD, username, yearValue, monthValue);
                    return new EntityNotFoundException(
                            String.format(NO_WORKLOAD_EXCEPTION_MESSAGE, username, yearValue, monthValue));
                });

        WorkingMonth month = year.getMonthlyWorkload().stream().filter(m -> m.getMonth().getValue() == monthValue)
                .findFirst().orElseThrow(() -> {
                    LOGGER.warn(NO_WORKLOAD, username, yearValue, monthValue);
                    return new EntityNotFoundException(
                            String.format(NO_WORKLOAD_EXCEPTION_MESSAGE, username, yearValue, monthValue));
                });

        double currentHours = month.getTotalHours();
        if (currentHours < duration) {
            LOGGER.warn(NOT_ENOUGH_HOURS, username, duration, yearValue, monthValue);
            throw new IllegalArgumentException("Not enough hours to remove. Trainer has only: " + currentHours);
        }

        month.setTotalHours(currentHours - duration);

        if (month.getTotalHours() == 0) {
            year.getMonthlyWorkload().remove(month);
        }

        if (year.getMonthlyWorkload().isEmpty()) {
            workload.getYears().remove(year);
        }

        repository.save(workload);

        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_REMOVED, duration, username, yearValue, monthValue);
    }

    @Override
    public void processWorkload(TrainerWorkloadRequestDTO requestDTO) {
        ActionType action = requestDTO.getActionType();

        switch (action) {
            case ADD -> addTrainingSession(requestDTO);
            case DELETE -> removeTrainingSession(requestDTO);
            default -> throw new IllegalArgumentException("Unsupported action type: " + action);
        }
    }

    @Override
    public TrainerWorkload getTrainerWorkload(String username) {
        return repository.findById(username).orElseThrow(() -> {
            LOGGER.error("No workload found for trainer '{}'", username);
            return new EntityNotFoundException("No workload found for trainer: " + username);
        });
    }
}
