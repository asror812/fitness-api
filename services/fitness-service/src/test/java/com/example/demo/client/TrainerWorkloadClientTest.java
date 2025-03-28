package com.example.demo.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    private final String trainerWorkloadUrl = "http://localhost:8080/workload";

    @BeforeEach
    void setUp() {
        // Inject trainerWorkloadUrl manually because it's set via @Value in the real
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
                eq(trainerWorkloadUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class)))
                        .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        trainerWorkloadClient.updateTrainingSession(requestDTO);

        verify(jwtService).generateTokenForMicroservice();
        verify(restTemplate).exchange(
                eq(trainerWorkloadUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class));
    }
}
