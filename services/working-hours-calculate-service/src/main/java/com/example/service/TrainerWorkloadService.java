package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;
import com.example.model.WorkingMonth;
import com.example.model.WorkingYear;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

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

    public void increaseWorkload(TrainerWorkloadRequestDTO requestDTO) {
        TrainerWorkload workload = trainersWorkload.get(requestDTO.getTrainerUsername());

        if (workload == null) {
            // Trainer doesn't exist, create a new one
            workload = new TrainerWorkload();
            workload.setUsername(requestDTO.getTrainerUsername());
            workload.setFirstName(requestDTO.getTrainerFirstName());
            workload.setLastName(requestDTO.getTrainerLastName());
            workload.setActive(requestDTO.getActive());

            trainersWorkload.put(workload.getUsername(), workload); // Save to in-memory store
        }

        List<WorkingYear> years = workload.getYears();
        if (years == null) {
            years = new ArrayList<>();
            workload.setYears(years);
        }

        // Find or create the correct year
        WorkingYear year = years.stream().filter(y -> y.getYear().equals(requestDTO.getTrainingDate().getYear()))
                .findFirst().orElse(null);

        if (year == null) {
            year = new WorkingYear();
            year.setYear(requestDTO.getTrainingDate().getYear());
            year.setMonthsWorkload(new ArrayList<>()); // Ensure it's initialized
            years.add(year);
        }

        List<WorkingMonth> workingMonths = year.getMonthsWorkload();
        if (workingMonths == null) {
            workingMonths = new ArrayList<>();
            year.setMonthsWorkload(workingMonths);
        }

        // Find or create the correct month
        WorkingMonth month = workingMonths.stream()
                .filter(m -> m.getMonth().equals(requestDTO.getTrainingDate().getMonthValue())).findFirst()
                .orElse(null);

        if (month == null) {
            month = new WorkingMonth();
            month.setMonth(requestDTO.getTrainingDate().getMonthValue());
            month.setTotalHours(requestDTO.getDuration());
            workingMonths.add(month);
        }
        else {
            month.setTotalHours(month.getTotalHours() + requestDTO.getDuration());
        }
    }

    public void decreaseWorkload(TrainerWorkloadRequestDTO requestDTO) {
        TrainerWorkload workload = trainersWorkload.get(requestDTO.getTrainerUsername());
        if (workload == null)
            return; // No trainer found

        List<WorkingYear> years = workload.getYears();
        if (years == null)
            return;

        WorkingYear year = years.stream().filter(y -> y.getYear().equals(requestDTO.getTrainingDate().getYear()))
                .findFirst().orElse(null);

        if (year == null)
            return;

        List<WorkingMonth> months = year.getMonthsWorkload();
        if (months == null)
            return;

        WorkingMonth month = months.stream()
                .filter(m -> m.getMonth().equals(requestDTO.getTrainingDate().getMonthValue())).findFirst()
                .orElse(null);

        if (month == null)
            return;

        month.setTotalHours(month.getTotalHours() - requestDTO.getDuration());

        // Remove the month if hours drop to zero or below
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
}
