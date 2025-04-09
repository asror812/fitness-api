package com.example.demo.client;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.jms.TrainerWorkloadJmsConsumer;
import com.example.demo.security.JwtService;
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
    private TrainerWorkloadJmsConsumer consumer;

    private final String trainerWorkloadUrl = "http://localhost:8080/workload";

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

        consumer.updateTrainingSession(requestDTO);

        verify(jwtService).generateTokenForMicroservice();
        verify(restTemplate).exchange(
                eq(trainerWorkloadUrl),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Void.class));
    }
}
