package com.example.demo.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private TrainerWorkloadClient trainerWorkloadClient;

    private final String trainerWorkloadUrl = "http://localhost:8080/workload/";

    @BeforeEach
    void setUp() {
        // Inject trainerWorkloadUrl manually because it's set via @Value in the real
        // class
        trainerWorkloadClient = new TrainerWorkloadClient(restTemplate, jwtService);
        // Use reflection to set trainerWorkloadUrl manually
        try {
            var field = TrainerWorkloadClient.class.getDeclaredField("trainerWorkloadUrl");
            field.setAccessible(true);
            field.set(trainerWorkloadClient, trainerWorkloadUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject trainerWorkloadUrl", e);
        }
    }

    @Test
    void testUpdateTrainingSession() {
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();
        String token = "mock-token";

        when(jwtService.generateTokenForMicroservice()).thenReturn(token);
        when(restTemplate.exchange(
                eq(trainerWorkloadUrl + "addOrRemoveWorkload"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)))
                        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        trainerWorkloadClient.updateTrainingSession(requestDTO);

        verify(jwtService).generateTokenForMicroservice();
        verify(restTemplate).exchange(
                eq(trainerWorkloadUrl + "addOrRemoveWorkload"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class));
    }

    @Test
    void testGetTrainerMonthlyWorkloadSummary() {
        String username = "testUser";
        int year = 2023;
        int month = 10;
        String token = "mock-token";
        TrainerWorkloadResponseDTO mockResponse = new TrainerWorkloadResponseDTO();

        when(jwtService.generateTokenForMicroservice()).thenReturn(token);
        when(restTemplate.exchange(
                eq(trainerWorkloadUrl + username + "/" + year + "/" + month),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(TrainerWorkloadResponseDTO.class)))
                        .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        TrainerWorkloadResponseDTO response = trainerWorkloadClient.getTrainerMonthlyWorkloadSummary(username,
                year, month);

        assertNotNull(response);
        assertEquals(mockResponse, response);

        verify(jwtService).generateTokenForMicroservice();
        verify(restTemplate).exchange(
                eq(trainerWorkloadUrl + username + "/" + year + "/" + month),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(TrainerWorkloadResponseDTO.class));
    }

    @Test
    void testWorkloadSummaryCalculateFallback() {
        String username = "testUser";
        int year = 2023;
        int month = 10;
        Throwable throwable = new RuntimeException("Mock exception");

        TrainerWorkloadResponseDTO response = trainerWorkloadClient.workloadSummaryCalculateFallback(username,
                year, month, throwable);

        assertNull(response);
        // No exception should be thrown, and the fallback should log the error
    }
}
