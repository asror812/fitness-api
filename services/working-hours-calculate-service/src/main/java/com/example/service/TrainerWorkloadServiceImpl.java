package com.example.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.dto.ActionType;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;
import com.example.model.WorkingMonth;
import com.example.model.WorkingYear;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadServiceImpl implements TrainerWorkloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadServiceImpl.class);
    private static final String NO_WORKLOAD_FOR_TRAINER_IN_YEAR_MONTH = "No workload found for trainer '{}' in year {} and month {}";

    private static final String NO_WORKLOAD_FOR_TRAINER_IN_YEAR = "No workload found for trainer '{}' in year {}";

    private static final String NO_WORKLOAD_FOR_TRAINER = "No workload found for trainer '{}'";

    private static final String INVALID_ACTION_TYPE = "Invalid action type";
    
    private static final String SUCCESSFULLY_TRAINING_SESSION_ADDED = "Added {} hours to trainer: {} for year: {} month: {}";
    private static final String SUCCESSFULLY_TRAINING_SESSION_REMOVED = "Removed {} hours from trainer: {} for year: {} month: {}";

    private static final String NOT_ENOUGH_HOURS_TO_REMOVE = "Trainer: {} does not have enough hours to remove {} hours for year: {} month: {}";

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

        TrainerWorkload workload = trainersWorkload.computeIfAbsent(username, key -> {
            TrainerWorkload newWorkload = new TrainerWorkload();
            newWorkload.setUsername(username);
            newWorkload.setFirstName(requestDTO.getTrainerFirstName());
            newWorkload.setLastName(requestDTO.getTrainerLastName());
            newWorkload.setActive(requestDTO.getActive());
            return newWorkload;
        });

        LocalDate trainingDate = requestDTO.getTrainingDate();
        List<WorkingYear> years = workload.getYears() == null ? new ArrayList<>() : workload.getYears();

        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        WorkingYear year = years.stream().filter(y -> y.getYear().equals(yearValue)).findFirst().orElseGet(() -> {
            WorkingYear newYear = new WorkingYear(yearValue, new ArrayList<>());
            years.add(newYear);
            return newYear;
        });

        List<WorkingMonth> months = year.getMonthsWorkload() == null ? new ArrayList<>() : year.getMonthsWorkload();

        WorkingMonth month = months.stream().filter(m -> m.getMonth().equals(monthValue)).findFirst()
                .orElseGet(() -> {
                    WorkingMonth newMonth = new WorkingMonth(monthValue, 0.0);
                    year.getMonthsWorkload().add(newMonth);
                    return newMonth;
                });

        month.setTotalHours(month.getTotalHours() + duration);
        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_ADDED, duration, username, yearValue, monthValue);
    }

    public void removeTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String username = requestDTO.getTrainerUsername();
        LocalDate trainingDate = requestDTO.getTrainingDate();
        int yearValue = trainingDate.getYear();
        int monthValue = trainingDate.getMonthValue();
        double duration = requestDTO.getDuration();

        TrainerWorkload workload = trainersWorkload.get(username);
        if (workload == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER, username);
            return;
        }

        List<WorkingYear> years = workload.getYears() == null ? new ArrayList<>() : workload.getYears();

        WorkingYear year = years.stream().filter(y -> y.getYear().equals(yearValue)).findFirst().orElse(null);

        if (year == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR, username, yearValue);
            return;
        }

        List<WorkingMonth> months = year.getMonthsWorkload() == null ? new ArrayList<>() : year.getMonthsWorkload();
        WorkingMonth month = months.stream().filter(m -> m.getMonth().equals(trainingDate.getMonthValue())).findFirst()
                .orElse(null);

        if (month == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR_MONTH, username, yearValue, monthValue);
            return;
        }
        
        if (month.getTotalHours() < duration) {
            LOGGER.warn(NOT_ENOUGH_HOURS_TO_REMOVE, username,
                    duration, yearValue, monthValue);
            return;
        }

        LOGGER.info(SUCCESSFULLY_TRAINING_SESSION_REMOVED, duration, username, yearValue,
                monthValue);

        LOGGER.warn(
                NO_WORKLOAD_FOR_TRAINER_IN_YEAR, requestDTO.getTrainerUsername(), trainingDate.getYear(),
                trainingDate.getMonthValue());

        month.setTotalHours(month.getTotalHours() - requestDTO.getDuration());

        // Remove the month if hours drop to zero
        if (month.getTotalHours() <= 0) {
            months.remove(month);
        }

        // Remove the year if there are no months left
        if (months.isEmpty()) {
            years.remove(year);
        }

        // Remove the trainer if they have no workload left
        if (years.isEmpty()) {
            trainersWorkload.remove(requestDTO.getTrainerUsername());
        }
    }

    @Override
    public void processWorkload(TrainerWorkloadRequestDTO requestDTO) {
        if (requestDTO.getType().equals(ActionType.ADD)) {
            addTrainingSession(requestDTO);
        }
        else if (requestDTO.getType().equals(ActionType.DELETE)) {
            removeTrainingSession(requestDTO);
        }
        else {
            throw new IllegalArgumentException(INVALID_ACTION_TYPE);
        }
    }

    @Override
    public List<TrainerWorkload> getTrainerWorkload(String username, int year, int month) {
        TrainerWorkload trainerWorkload = trainersWorkload.get(username);

        if (trainerWorkload == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER, username);
            return Collections.emptyList();
        }

        List<WorkingYear> years = trainerWorkload.getYears();
        if (years == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR, username, year);
            return Collections.emptyList();
        }

        WorkingYear workingYear = years.stream().filter(y -> y.getYear().equals(year)).findFirst().orElse(null);

        if (workingYear == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR, username, year);
            return Collections.emptyList();
        }

        List<WorkingMonth> months = workingYear.getMonthsWorkload();
        if (months == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR_MONTH, username, year, month);
            return Collections.emptyList();
        }

        WorkingMonth workingMonth = months.stream().filter(m -> m.getMonth().equals(month)).findFirst().orElse(null);
        if (workingMonth == null) {
            LOGGER.warn(NO_WORKLOAD_FOR_TRAINER_IN_YEAR_MONTH, username, year, month);
            return Collections.emptyList();
        }

        return Collections.singletonList(trainerWorkload);
    }
}
