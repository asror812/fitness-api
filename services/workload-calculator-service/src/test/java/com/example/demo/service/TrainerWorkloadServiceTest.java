package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.dao.TrainerWorkloadRepository;
import com.example.demo.dto.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.TrainerWorkload;
import com.example.demo.model.WorkingMonth;
import com.example.demo.model.WorkingYear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadServiceImplTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadServiceImpl service;

    private final String testUsername = "trainer1";
    private TrainerWorkload workload;

    @BeforeEach
    void setup() {
        workload = new TrainerWorkload();
        workload.setUsername(testUsername);
        workload.setFirstName("John");
        workload.setLastName("Doe");
        workload.setYears(new ArrayList<>());
    }

    @Test
    void processWorkload_Add_NewMonthAndYear() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        when(repository.findById(testUsername)).thenReturn(Optional.of(workload));
        when(repository.save(any())).thenReturn(workload);

        service.processWorkload(request);

        assertEquals(1, workload.getYears().size());
        WorkingYear year = workload.getYears().get(0);
        assertEquals(2025, year.getYear());
        assertEquals(1, year.getMonthlyWorkload().size());
        assertEquals(2.0, year.getMonthlyWorkload().get(0).getTotalHours());
    }

    @Test
    void processWorkload_Add_ToExistingMonth() {
        TrainerWorkloadRequestDTO request1 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.ADD);

        TrainerWorkloadRequestDTO request2 = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 1.5, ActionType.ADD);

        when(repository.findById(testUsername)).thenReturn(Optional.of(workload));
        when(repository.save(any())).thenReturn(workload);

        service.processWorkload(request1);
        service.processWorkload(request2);

        double totalHours = workload.getYears().get(0).getMonthlyWorkload().get(0).getTotalHours();
        assertEquals(3.5, totalHours);
    }

    @Test
    void processWorkload_Delete_ReducesHours() {
        WorkingMonth march = new WorkingMonth(Month.MARCH, 3.0);
        WorkingYear year = new WorkingYear(2025, List.of(march));
        workload.setYears(List.of(year));

        TrainerWorkloadRequestDTO deleteRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        when(repository.findById(testUsername)).thenReturn(Optional.of(workload));
        when(repository.save(any())).thenReturn(workload);

        service.processWorkload(deleteRequest);

        assertEquals(1.0, march.getTotalHours());
    }

    @Test
    void processWorkload_Delete_NotEnoughHours_ThrowsException() {
        WorkingMonth march = new WorkingMonth(Month.MARCH, 1.0);
        WorkingYear year = new WorkingYear(2025, List.of(march));
        workload.setYears(List.of(year));

        TrainerWorkloadRequestDTO deleteRequest = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        when(repository.findById(testUsername)).thenReturn(Optional.of(workload));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> service.processWorkload(deleteRequest));
        assertEquals("Not enough hours to remove. Trainer has only: 1.0", ex.getMessage());
    }

    @Test
    void processWorkload_TrainerNotFound_ThrowsException() {
        TrainerWorkloadRequestDTO request = new TrainerWorkloadRequestDTO(testUsername, "John", "Doe",
                LocalDate.of(2025, 3, 1), 2.0, ActionType.DELETE);

        when(repository.findById(testUsername)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.processWorkload(request));
    }

    @Test
    void getTrainerWorkload_NoData_ThrowsException() {
        when(repository.findById(testUsername)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.getTrainerWorkload(testUsername));

        assertEquals("No workload found for trainer: trainer1", exception.getMessage());
    }

    @Test
    void getTrainerWorkload_WithData_ReturnsCorrectly() {
        WorkingMonth march = new WorkingMonth(Month.MARCH, 4.0);
        WorkingYear year = new WorkingYear(2025, List.of(march));
        workload.setYears(List.of(year));

        when(repository.findById(testUsername)).thenReturn(Optional.of(workload));

        TrainerWorkload response = service.getTrainerWorkload(testUsername);

        WorkingYear workingYear = response.getYears().stream().filter(t -> t.getYear().equals(2025)).findAny()
                .orElseThrow();

        WorkingMonth month = workingYear.getMonthlyWorkload().stream().filter(t -> t.getMonth().equals(Month.MARCH))
                .findAny().orElseThrow();

        assertEquals(4.0, month.getTotalHours());
    }
}
