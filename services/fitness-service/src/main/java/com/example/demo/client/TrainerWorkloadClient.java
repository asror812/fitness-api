package com.example.demo.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.security.JwtService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadClient {

    private final RestTemplate restTemplate;

    private final JwtService jwtService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainerWorkloadClient.class);

    private static final String FALLBACK_UPDATE_TRAINING_SESSION = "Fallback triggered for updateTrainingSession. Message: {} Cause: {}";
    private static final String UNKNOWN_ERROR = "Unknown error";

    @Value("${service.trainer-workload.url}")
    private String trainerWorkloadUrl;

    @CircuitBreaker(name = "workloadService", fallbackMethod = "updateTrainingSessionFallback")
    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtService.generateTokenForMicroservice());

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        LOGGER.info("Url: {} Request Entity: {}", trainerWorkloadUrl, entity);

        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl, HttpMethod.POST,
                entity, Void.class);
        LOGGER.info("{}", response);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Workload service returned : {}", response);
        }
    }

    public ResponseEntity<Void> updateTrainingSessionFallback(TrainerWorkloadRequestDTO requestDTO, Throwable e) {
        LOGGER.error(FALLBACK_UPDATE_TRAINING_SESSION, e.getCause() != null ? e.getCause() : UNKNOWN_ERROR);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

}