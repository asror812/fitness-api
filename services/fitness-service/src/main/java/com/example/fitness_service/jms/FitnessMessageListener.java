package com.example.fitness_service.jms;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.example.fitness_service.jms.event.IdempotencyService;
import com.example.fitness_service.service.TraineeService;
import com.example.fitness_service.service.TrainerService;
import com.example.fitness_service.jms.dto.ProfileCreateResultEvent;
import com.example.fitness_service.jms.enums.Result;
import com.example.fitness_service.jms.enums.Role;
import com.example.fitness_service.jms.dto.TraineeRegisterEvent;
import com.example.fitness_service.jms.dto.TrainerRegisterEvent;
import com.example.fitness_service.jms.outbox.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class FitnessMessageListener {

    private static final String FAILED_TO_DESERIALIZE_PAYLOAD = "Failed to deserialize payload";
    private final ObjectMapper objectMapper;
    private final IdempotencyService idempotencyService;

    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private final JmsPublisher publisher;

    public static final String HDR_EVENT_ID = "eventId";
    public static final String HDR_EVENT_TYPE = "eventType";
    public static final String HDR_CORRELATION_ID = "correlationId";

    @JmsListener(destination = "${jms.topics.user_register}", containerFactory = "topicListenerContainerFactory")

    public void onMessage(
            String json,
            @Header(name = "eventId", required = true) String eventID,
            @Header(name = "eventType", required = true) String eventType,
            @Header(name = "correlationId", required = true) String correlationID) {

        Role expectedRole = null;

        if (!EventType.TRAINEE_CREATE_REQUESTED.name().equals(eventType)
                && !EventType.TRAINER_CREATE_REQUESTED.name()
                        .equals(eventType)) {
            log.info("Ignored unsupported eventType={} payload={}", eventType, json);
            return;
        }

        else if (EventType.TRAINER_CREATE_REQUESTED.name().equals(eventType))
            expectedRole = Role.TRAINER;

        else if (EventType.TRAINEE_CREATE_REQUESTED.name().equals(eventType))
            expectedRole = Role.TRAINEE;

        log.info("Incoming request body {}", json);

        UUID eventId;
        UUID correlationId;

        try {
            eventId = UUID.fromString(eventID);
            correlationId = UUID.fromString(correlationID);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid event/correlation UUID. eventId={} correlationId={}", eventID, correlationID, ex);
            return;
        }

        try {
            if (!idempotencyService.tryMarkProcessed(eventId, correlationId)) {
                log.info("Duplicate event ignored. eventId={}", eventID);
                return;
            }
        } catch (Exception e) {
            log.info("Duplicate event ignored by DB constraint. eventId={}", eventID);
            return;
        }

        if (json == null || json.isBlank()) {
            publishFailure(eventId, correlationId, expectedRole, "User register object is null");
            return;
        }

        log.info("Json: {}", json);

        switch (expectedRole) {
            case Role.TRAINEE -> processTrainee(json, expectedRole, eventId, correlationId);
            case Role.TRAINER -> processTrainer(json, expectedRole, eventId, correlationId);
            default -> throw new RuntimeException("Role is undefined");
        }
    }

    private void processTrainer(String json, Role expectedRole, UUID eventId, UUID correlationId) {
        try {
            TrainerRegisterEvent dto = objectMapper.readValue(json, TrainerRegisterEvent.class);
            UUID userId = dto.getUserId();

            boolean created = trainerService.createIfNotExists(dto);

            var resultEvent = new ProfileCreateResultEvent(
                    UUID.randomUUID(),
                    correlationId,
                    userId,
                    expectedRole,
                    Result.OK,
                    created,
                    null);

            publisher.publishProfileCreateResult(resultEvent, correlationId);

            idempotencyService.markSuccess(eventId);

        } catch (JsonProcessingException e) {

            log.error("Failed to deserialize payload. eventId={}", eventId, e);
            publishFailure(eventId, correlationId, expectedRole, FAILED_TO_DESERIALIZE_PAYLOAD);

            idempotencyService.markFailed(eventId, correlationId, FAILED_TO_DESERIALIZE_PAYLOAD);
        } catch (Exception e) {
            log.error("Failed to process registration event. eventId={}", eventId, e);
            publishFailure(eventId, correlationId, expectedRole, e.getMessage());
            idempotencyService.markFailed(eventId, correlationId, e.getMessage());
        }
    }

    private void processTrainee(String json, Role expectedRole, UUID eventId, UUID correlationId) {
        try {
            TraineeRegisterEvent dto = objectMapper.readValue(json, TraineeRegisterEvent.class);
            UUID userId = dto.getUserId();

            boolean created = traineeService.createIfNotExists(eventId, dto);

            var resultEvent = new ProfileCreateResultEvent(
                    UUID.randomUUID(),
                    correlationId,
                    userId,
                    expectedRole,
                    Result.OK,
                    created,
                    null);

            publisher.publishProfileCreateResult(resultEvent, correlationId);

            idempotencyService.markSuccess(eventId);

        } catch (JsonProcessingException e) {

            log.error("Failed to deserialize payload. eventId={}", eventId, e);
            publishFailure(eventId, correlationId, expectedRole, FAILED_TO_DESERIALIZE_PAYLOAD);
            idempotencyService.markFailed(eventId, correlationId, FAILED_TO_DESERIALIZE_PAYLOAD);

        } catch (Exception e) {

            log.error("Failed to process registration event. eventId={}", eventId, e);
            publishFailure(eventId, correlationId, expectedRole, e.getMessage());
            idempotencyService.markFailed(eventId, correlationId, e.getMessage());
        }
    }

    private void publishFailure(UUID eventId, UUID correlationId, Role role, String errorMessage) {
        var resultEvent = new ProfileCreateResultEvent(
                eventId,
                correlationId,
                null,
                role,
                Result.ERROR,
                false,
                errorMessage);
        publisher.publishProfileCreateResult(resultEvent, correlationId);
    }
}
