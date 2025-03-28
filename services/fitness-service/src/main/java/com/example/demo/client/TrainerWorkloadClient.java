package com.example.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadClient.class);

    private static final String FALLBACK_UPDATE_TRAINING_SESSION = "Fallback triggered for updateTrainingSession. RequestDTO: {} Error: {}";

    @Value("${service.trainer-workload.url}")
    private String trainerWorkloadUrl;

    @CircuitBreaker(name = "workloadService", fallbackMethod = "updateTrainingSessionFallback")
    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String transactionId = MDC.get("transactionID");

        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + jwtService.generateTokenForMicroservice());
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Transaction-ID", transactionId);

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        LOGGER.info("Url: {} Request Entity: {}", trainerWorkloadUrl, entity);

        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl, HttpMethod.POST,
                entity, Void.class);
        LOGGER.info("{}", response);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Proccess workload returned : {}", response);
        }
    }

    public void updateTrainingSessionFallback(TrainerWorkloadRequestDTO requestDTO, Throwable throwable) {
        LOGGER.error(FALLBACK_UPDATE_TRAINING_SESSION, requestDTO, throwable);
    }

}