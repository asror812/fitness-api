package com.example.demo.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.request.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.security.JwtService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadClient {

    private  final RestTemplate restTemplate;

    private final JwtService jwtService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainerWorkloadClient.class);

    private static final String ERROR_TRAINER_WORKLOAD_SERVICE = "Trainer workload service returned an error while processing request: {}";

    private static final String FALLBACK_UPDATE_TRAINING_SESSION = "Fallback triggered for updateTrainingSession. Request: {}. Error: {}";

    private static final String FALLBACK_RESPONSE_MESSAGE = "Trainer workload service is currently unavailable, please try again later.";
    private static final String UNKNOWN_ERROR = "Unknown error";

    @Value("${service.trainer-workload.url}") // http://localhost:8082/api/v1/trainer-workload
    private String trainerWorkloadUrl;

  
    @CircuitBreaker(name = "workloadService", fallbackMethod = "updateTrainingSessionFallback")
    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtService.generateTokenForMicroservice());

        HttpMethod httpMethod = requestDTO.getActionType() == ActionType.ADD ? HttpMethod.POST : HttpMethod.DELETE;

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl, httpMethod, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error(ERROR_TRAINER_WORKLOAD_SERVICE, requestDTO);
        }
    }

    public String updateTrainingSessionFallback(TrainerWorkloadRequestDTO requestDTO, Throwable t) {
        LOGGER.error(FALLBACK_UPDATE_TRAINING_SESSION, requestDTO,
                t != null ? t.getMessage() : UNKNOWN_ERROR);
        return FALLBACK_RESPONSE_MESSAGE;
    }

}