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
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.security.JwtService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkload {

    private  final RestTemplate restTemplate;

    private final JwtService jwtService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainerWorkload.class);


    @Value("${service.trainer-workload.url}") // http://localhost:8082/api/v1/trainer-workload
    private String trainerWorkloadUrl;

    @CircuitBreaker(name = "workloadService", fallbackMethod = "increaseTrainerWorkloadFallback")
    public void increaseTrainerWorkload(TrainerWorkloadRequestDTO requestDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtService.generateTokenForMicroservice());

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl + "/increase", HttpMethod.POST, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Trainer workload service returned an error while processing request: {}", requestDTO);
        }
    }

    public String increaseTrainerWorkloadFallback(TrainerWorkloadRequestDTO requestDTO, Throwable t) {
        LOGGER.error("Fallback triggered for increaseTrainerWorkload. Request: {}. Error: {}", requestDTO,
                t != null ? t.getMessage() : "Unknown error");
        return "Service trainer workload is currently unavailable, please try again later";
    }

    @CircuitBreaker(name = "workloadService", fallbackMethod = "decreaseTrainerWorkloadFallback")
    public void decreaseTrainerWorkload(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwtService.generateTokenForMicroservice());

        HttpEntity<TrainerWorkloadRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                trainerWorkloadUrl + "/decrease", HttpMethod.DELETE, entity, Void.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LOGGER.error("Trainer workload service returned an error while processing request: {}", requestDTO);
        }
    }

    public String decreaseTrainerWorkloadFallback(TrainerWorkloadRequestDTO requestDTO, Throwable t) {
        LOGGER.error("Fallback triggered for decreaseTrainerWorkload. Request: {}. Error: {}", requestDTO,
                t != null ? t.getMessage() : "Unknown error");
        return "Service trainer workload is currently unavailable, please try again later";
    }

}