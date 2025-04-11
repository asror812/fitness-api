package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.example.demo.dto.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.model.WorkingMonth;
import com.example.demo.model.WorkingYear;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceImplTest {

    @InjectMocks
    private TrainerWorkloadServiceImpl service;
    private final String testUsername = "trainer1";

    @Test
    void addTrainingSession() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        service.addTrainingSession(request);
        TrainerWorkload trainer = service.getTrainer(testUsername);

        assertNotNull(trainer);
        assertEquals("John", trainer.getFirstName());
        assertEquals(1, trainer.getYears().size());
        assertEquals(2025, trainer.getYears().get(0).getYear());
        assertEquals(1, trainer.getYears().get(0).getMonthlyWorkload().size());
        assertEquals(2.0, trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours());
    }

    @Test
    void addTrainingSession_ExistingTrainer() {
        TrainerWorkloadRequestDTO request1 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);
        TrainerWorkloadRequestDTO request2 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 1.5, ActionType.ADD);

        service.addTrainingSession(request1);
        service.addTrainingSession(request2);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double totalHours = trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours();

        assertEquals(3.5, totalHours);
    }

    @Test
    void removeTrainingSession() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 3.0, ActionType.ADD);
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.addTrainingSession(addRequest);
        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double remainingHours = trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours();

        assertEquals(1.0, remainingHours);
    }

    @Test
    void removeTrainingSession_NotEnoughHours() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 1.0, ActionType.ADD);
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.addTrainingSession(addRequest);
        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        assertNotNull(trainer);
        double remainingHours = trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours();
        assertEquals(1.0, remainingHours);
    }

    @Test
    void removeTrainingSession_TrainerNotFound() {
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        assertNull(trainer);
    }

    @Test
    void processWorkload_Add() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        service.processWorkload(request);
        TrainerWorkload trainer = service.getTrainer(testUsername);

        assertNotNull(trainer);
        assertEquals(2.0, trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours());
    }

    @Test
    void processWorkload_Delete() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 3.0, ActionType.ADD);
        TrainerWorkloadRequestDTO deleteRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.processWorkload(addRequest);
        service.processWorkload(deleteRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double remainingHours = trainer.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours();

        assertEquals(1.0, remainingHours);
    }

    @Test
    void getTrainerWorkload_NoData() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.getTrainerWorkload(testUsername));

        assertEquals("No workload found for trainer: trainer1", exception.getMessage());
    }

    @Test
    void getTrainerWorkload_WithData() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 4.0, ActionType.ADD);
        service.addTrainingSession(request);

        TrainerWorkload response = service.getTrainerWorkload(testUsername);

        List<WorkingYear> years = response.getYears();
        WorkingYear workingYear = years.stream().filter(t -> t.getYear().equals(2025)).findAny().get();

        WorkingMonth month = workingYear.getMonthlyWorkload().stream().filter(t -> t.getMonth().equals(Month.MARCH)).findAny()
                .get();

        assertEquals(4.0, month.getTotalHours());
    }
}
