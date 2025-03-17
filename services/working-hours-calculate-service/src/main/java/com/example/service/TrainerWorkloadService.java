package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.model.TrainerWorkload;
import com.example.model.WorkingMonth;
import com.example.model.WorkingYear;
import com.example.repository.TrainerWorkloadRepository;
import com.example.repository.WorkingMonthRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

    private final WorkingMonthRepository workingMonthRepository;
    private final TrainerWorkloadRepository repository;

    public void increaseWorkload(TrainerWorkloadRequestDTO requestDTO) {

        Optional<TrainerWorkload> trainerWorkload = repository.findById(requestDTO.getTrainerUsername());
        if (trainerWorkload.isPresent()) {
            TrainerWorkload workload = trainerWorkload.get();
            List<WorkingYear> years = workload.getYears();
            WorkingYear year = years.stream().filter(y -> y.getYear().equals(requestDTO.getTrainingDate().getYear()))
                    .findFirst().orElse(null);
            if (year == null) {
                year = new WorkingYear();
                year.setYear(requestDTO.getTrainingDate().getYear());
                year.setWorkload(workload);
                years.add(year);
            }

            List<WorkingMonth> months = year.getMonths();
            WorkingMonth month = months.stream()
                    .filter(m -> m.getMonth().equals(requestDTO.getTrainingDate().getMonthValue())).findFirst()
                    .orElse(null);

            if (month == null) {
                month = new WorkingMonth();
                month.setMonth(requestDTO.getTrainingDate().getMonthValue());
                month.setTotalHours(requestDTO.getDuration());
                month.setYear(year);
                months.add(month);
            }
            else
                month.setTotalHours(month.getTotalHours() + requestDTO.getDuration());

            repository.save(workload);
        }
        else {
            TrainerWorkload workload = new TrainerWorkload();
            workload.setUsername(requestDTO.getTrainerUsername());
            workload.setFirstName(requestDTO.getTrainerFirstName());
            workload.setLastName(requestDTO.getTrainerLastName());
            workload.setStatus(true);

            WorkingYear year = new WorkingYear();
            year.setYear(requestDTO.getTrainingDate().getYear());
            year.setWorkload(workload);

            WorkingMonth month = new WorkingMonth();
            month.setMonth(requestDTO.getTrainingDate().getMonthValue());
            month.setTotalHours(requestDTO.getDuration());
            month.setYear(year);
            year.setMonths(List.of(month));
            workload.setYears(List.of(year));

            repository.save(workload);
        }
    }

    public void decreaseWorkload(TrainerWorkloadRequestDTO requestDTO) {
        Optional<TrainerWorkload> trainerWorkload = repository.findById(requestDTO.getTrainerUsername());
        if (trainerWorkload.isPresent()) {
            TrainerWorkload workload = trainerWorkload.get();
            List<WorkingYear> years = workload.getYears();
            WorkingYear year = years.stream().filter(y -> y.getYear().equals(requestDTO.getTrainingDate().getYear()))
                    .findFirst().orElse(null);

            if (year == null)
                return;

            List<WorkingMonth> months = year.getMonths();
            WorkingMonth month = months.stream()
                    .filter(m -> m.getMonth().equals(requestDTO.getTrainingDate().getMonthValue())).findFirst()
                    .orElse(null);

            if (month == null)
                return;

            else {
                month.setTotalHours(month.getTotalHours() - requestDTO.getDuration());

                if (month.getTotalHours() <= 0)
                    months.remove(month);
            }
            repository.save(workload);
        }
    }
}
