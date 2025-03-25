package com.example.demo.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String FALLBACK_GET_TRAINER_MONTHLY_WORKLOAD = "Fallback triggered for getTrainerMonthlyWorkloadSummary. Username: {} Year: {} Month {}. Error: {}";

    @Value("${service.trainer-workload.url}")
    private String trainerWorkloadUrl;

    @CircuitBreaker(name = "workloadService", fallbackMethod = "updateTrainingSessionFallback")
    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + jwtService.generateTokenForMicroservice());
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        LOGGER.info("Url: {} Request Entity: {}", trainerWorkloadUrl, entity);

        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl + "addOrRemoveWorkload", HttpMethod.POST,
                entity, Void.class);
        LOGGER.info("{}", response);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Proccess workload returned : {}", response);
        }

    }

    public void updateTrainingSessionFallback(TrainerWorkloadRequestDTO requestDTO, Throwable throwable) {
        LOGGER.error(FALLBACK_UPDATE_TRAINING_SESSION, requestDTO, throwable);
    }

    @CircuitBreaker(name = "workloadSummary", fallbackMethod = "workloadSummaryCalculateFallback")
    public TrainerWorkloadResponseDTO getTrainerMonthlyWorkloadSummary(String username, int year, int month) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + jwtService.generateTokenForMicroservice());
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers); 

        ResponseEntity<TrainerWorkloadResponseDTO> response = restTemplate.exchange(
                trainerWorkloadUrl + username + "/" + year + "/" + month,
                HttpMethod.GET,
                entity,
                TrainerWorkloadResponseDTO.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Workload summary returned : {}", response.getBody());
        }

        LOGGER.info("Trainer monthly workload for user: {}. Workload: {}", username, response.getBody());

        return response.getBody();
    }

    public TrainerWorkloadResponseDTO workloadSummaryCalculateFallback(String username, int year, int month,
            Throwable throwable) {
        LOGGER.error(FALLBACK_GET_TRAINER_MONTHLY_WORKLOAD, username, year, month, throwable);
        return null;
    }

}