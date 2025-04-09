package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.model.WorkingMonth;
import com.example.demo.model.WorkingYear;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadServiceImpl.class);
    private static final String NO_WORKLOAD = "No workload found for trainer '{}'";
    private static final String SUCCESSFULLY_TRAINING_SESSION_ADDED = "Added {} hours to trainer: {} for year: {} month: {}";
    private static final String SUCCESSFULLY_TRAINING_SESSION_REMOVED = "Removed {} hours from trainer: {} for year: {} month: {}";
    private static final String NOT_ENOUGH_HOURS = "Trainer: {} does not have enough hours to remove {} hours for year: {} month: {}";

    private final Map<String, TrainerWorkload> trainersWorkload = new HashMap<>();

    public void saveTrainer(TrainerWorkload trainer) {
        trainersWorkload.put(trainer.getUsername(), trainer);
    }

    public TrainerWorkload getTrainer(String username) {
        return trainersWorkload.get(username);
    }

    public List<TrainerWorkload> getAllTrainers() {
        return new ArrayList<>(trainersWorkload.values());
    }

    public void deleteTrainer(String username) {
        trainersWorkload.remove(username);
    }

    public void addTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String username = requestDTO.getTrainerUsername();

        TrainerWorkload workload = trainersWorkload.get(username);
        if (workload == null) {
            workload = new TrainerWorkload();
            workload.setUsername(username);
            workload.setFirstName(requestDTO.getTrainerFirstName());
            workload.setLastName(requestDTO.getTrainerLastName());
            trainersWorkload.put(username, workload);
        }

        LocalDate trainingDate = requestDTO.getTrainingDate();
        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        WorkingYear year = workload.getYears().stream().filter(y -> y.getYear() == yearValue).findFirst().orElse(null);

        if (year == null) {
            year = new WorkingYear(yearValue, new ArrayList<>());
            workload.getYears().add(year);
        }

        WorkingMonth month = year.getMonthlyWorkload().stream().filter(m -> m.getMonth() == monthValue).findFirst()
                .orElse(null);

        if (month == null) {
            month = new WorkingMonth(monthValue, 0.0);
            year.getMonthlyWorkload().add(month);
        }

        month.setTotalHours(month.getTotalHours() + duration);

        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_ADDED, duration, username, yearValue, monthValue);
    }

    public void removeTrainingSession(TrainerWorkloadRequestDTO requestDTO) {

        LocalDate trainingDate = requestDTO.getTrainingDate();

        String username = requestDTO.getTrainerUsername();
        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        TrainerWorkload workload = trainersWorkload.get(username);
        if (workload == null) {
            LOGGER.warn(NO_WORKLOAD, username, yearValue, monthValue);
            return;
        }

        WorkingYear year = workload.getYears().stream().filter(y -> y.getYear().equals(yearValue)).findFirst()
                .orElse(null);

        if (year == null) {
            LOGGER.warn(NO_WORKLOAD, username, yearValue, monthValue);
            return;
        }

        WorkingMonth month = year.getMonthlyWorkload().stream()
                .filter(m -> m.getMonth().equals(trainingDate.getMonthValue())).findFirst().orElse(null);

        if (month == null || month.getTotalHours() < duration) {
            LOGGER.warn(NOT_ENOUGH_HOURS, requestDTO.getTrainerUsername(), duration, yearValue, monthValue);
            return;
        }

        month.setTotalHours(month.getTotalHours() - duration);

        if (month.getTotalHours() <= 0)
            year.getMonthlyWorkload().remove(month);
        if (year.getMonthlyWorkload().isEmpty())
            workload.getYears().remove(year);
        if (workload.getYears().isEmpty())
            trainersWorkload.remove(requestDTO.getTrainerUsername());

        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_REMOVED, duration, requestDTO.getTrainerUsername(), yearValue,
                monthValue);
    }

    @Override
    public void processWorkload(TrainerWorkloadRequestDTO requestDTO) {
        if (requestDTO.getActionType().equals(ActionType.ADD)) {
            addTrainingSession(requestDTO);
        }
        else if (requestDTO.getActionType().equals(ActionType.DELETE)) {
            removeTrainingSession(requestDTO);
        }
        else {
            throw new IllegalArgumentException("Invalid action type");
        }
    }

    @Override
    public TrainerWorkload getTrainerWorkload(String username) {
        TrainerWorkload trainerWorkload = trainersWorkload.get(username);

        LOGGER.warn("{}", trainerWorkload);

        if (trainerWorkload == null) {
            LOGGER.warn(NO_WORKLOAD, username);
            throw new ResourceNotFoundException("No workload found for trainer: " + username);
        }

        return trainerWorkload;
    }

}