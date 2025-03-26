package com.example.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import com.example.dto.ActionType;
import com.example.dto.TrainerWorkloadRequestDTO;
import com.example.dto.TrainerWorkloadResponseDTO;
import com.example.model.TrainerWorkload;
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
    void testAddTrainingSession() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        service.addTrainingSession(request);
        TrainerWorkload trainer = service.getTrainer(testUsername);

        assertNotNull(trainer);
        assertEquals("John", trainer.getFirstName());
        assertEquals(1, trainer.getYears().size());
        assertEquals(2025, trainer.getYears().get(0).getYear());
        assertEquals(1, trainer.getYears().get(0).getMonthsWorkload().size());
        assertEquals(2.0, trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours());
    }

    @Test
    void testAddTrainingSession_ExistingTrainer() {
        TrainerWorkloadRequestDTO request1 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);
        TrainerWorkloadRequestDTO request2 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 1.5, ActionType.ADD);

        service.addTrainingSession(request1);
        service.addTrainingSession(request2);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double totalHours = trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours();

        assertEquals(3.5, totalHours);
    }

    @Test
    void testRemoveTrainingSession() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 3.0, ActionType.ADD);
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.addTrainingSession(addRequest);
        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double remainingHours = trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours();

        assertEquals(1.0, remainingHours);
    }

    @Test
    void testRemoveTrainingSession_NotEnoughHours() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 1.0, ActionType.ADD);
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.addTrainingSession(addRequest);
        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        assertNotNull(trainer);
        double remainingHours = trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours();
        assertEquals(1.0, remainingHours);
    }

    @Test
    void testRemoveTrainingSession_TrainerNotFound() {
        TrainerWorkloadRequestDTO removeRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.removeTrainingSession(removeRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        assertNull(trainer);
    }

    @Test
    void testProcessWorkload_Add() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        service.processWorkload(request);
        TrainerWorkload trainer = service.getTrainer(testUsername);

        assertNotNull(trainer);
        assertEquals(2.0, trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours());
    }

    @Test
    void testProcessWorkload_Delete() {
        TrainerWorkloadRequestDTO addRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 3.0, ActionType.ADD);
        TrainerWorkloadRequestDTO deleteRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        service.processWorkload(addRequest);
        service.processWorkload(deleteRequest);

        TrainerWorkload trainer = service.getTrainer(testUsername);
        double remainingHours = trainer.getYears().get(0).getMonthsWorkload().get(0).getTotalHours();

        assertEquals(1.0, remainingHours);
    }

    @Test
    void testGetTrainerWorkload_NoData() {
        TrainerWorkloadResponseDTO response = service.getTrainerWorkload(testUsername, 2025, 3);

        assertNotNull(response);
        assertEquals(testUsername, response.getUsername());
        assertEquals(2025, response.getYear());
        assertEquals(3, response.getMonth());
        assertEquals(0, response.getTotalHours());
    }

    @Test
    void testGetTrainerWorkload_WithData() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 4.0, ActionType.ADD);
        service.addTrainingSession(request);

        TrainerWorkloadResponseDTO response = service.getTrainerWorkload(testUsername, 2025, 3);

        assertEquals(4.0, response.getTotalHours());
    }
}
