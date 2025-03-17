package com.example.demo.client;

import java.lang.reflect.Parameter;

import org.apache.hc.core5.http.HttpStatus;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import com.example.demo.controller.AuthController;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.security.JwtService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrainerWorkload {

    private final AuthController authController;

    @Value("${interception.trainer-worload.url}")
    public String trainerWorkloadUrl;

    private final RestTemplate restTemplate;

    private final JwtService jwtService;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainerWorkload.class);

    @CircuitBreaker(name = "trainerWorkload", fallbackMethod = "increaseTrainerWorkloadFallback")
    public void increaseTrainerWorkload(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TrainerWorkloadRequestDTO> requetDTO = new HttpEntity<>(requestDTO, headers);
        restTemplate.exchange(trainerWorkloadUrl, HttpMethod.POST, requetDTO, Void.class);
    }

    public void increaseTrainerWorkloadFallback(TrainerWorkloadRequestDTO requestDTO, Throwable t) {
        LOGGER.error("Fallback triggered for increaseTrainerWorkload. Request: {}. Error: {}", requestDTO,
                t.toString());
    }

    @CircuitBreaker(name = "trainerWorkload", fallbackMethod = "decreaseTrainerWorkloadFallback")
    public void decreaseTrainerWorkload(TrainerWorkloadRequestDTO requestDTO) {
        HttpHeaders headers = new HttpHeaders();

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<TrainerWorkloadRequestDTO> requetEntity = new HttpEntity<>(requestDTO, headers);

        restTemplate.exchange(trainerWorkloadUrl, HttpMethod.DELETE, requetEntity, Void.class);
    }

    public static void decreaseTrainerWorkloadFallback(TrainerWorkloadRequestDTO requestDTO, Throwable t) {
        LOGGER.error("Error while decreasing trainer workload", t);
    }

}