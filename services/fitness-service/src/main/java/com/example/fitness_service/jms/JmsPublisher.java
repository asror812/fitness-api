package com.example.fitness_service.jms;

import com.example.fitness_service.jms.dto.ProfileCreateResultEvent;
import com.example.fitness_service.jms.enums.Result;
import com.example.fitness_service.jms.enums.Role;
import com.example.fitness_service.jms.outbox.EventType;
import com.example.fitness_service.exception.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JmsPublisher {

    @Value("${jms.topics.workload_update:workload_update}")
    private String updateTrainerWorkloadQueue;

    @Value("${jms.topics.user_register:user_register}")
    private String registerUser;

    public static final String HDR_EVENT_ID = "eventId";
    public static final String HDR_EVENT_TYPE = "eventType";
    public static final String HDR_CORRELATION_ID = "correlationId";

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void publishUpdateWorkload(Object dto, UUID eventId, UUID correlationId) {
        publish(updateTrainerWorkloadQueue, EventType.WORKLOAD_UPDATE_REQUESTED, dto, eventId, correlationId);
    }

    public void publishUserCreated(ProfileCreateResultEvent dto, UUID eventId, UUID correlationId) {
        publish(registerUser, EventType.TRAINER_CREATE_REQUESTED, dto, eventId, correlationId);
    }

    private void publish(
            String topic,
            EventType eventType,
            Object dto,
            UUID eventId,
            UUID correlationId) {

        final String json = toJson(dto);

        log.info("Publish -> topic={} correlationId={} eventType={} eventId={}", topic, eventType, eventId,
                correlationId);

        jmsTemplate.convertAndSend(topic, json, message -> {
            message.setStringProperty(HDR_EVENT_ID, eventId.toString());
            message.setStringProperty(HDR_EVENT_TYPE, eventType.name());
            message.setStringProperty(HDR_CORRELATION_ID, correlationId.toString());

            return message;
        });
    }

    private String toJson(Object dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Failed to serialize payload", dto, e);
        }
    }

    public void publishProfileCreateResult(ProfileCreateResultEvent resultEvent, UUID eventId) {
        EventType eventType;
        if (resultEvent.getRole() == Role.TRAINEE) {
            eventType = resultEvent.getResult() == Result.OK
                    ? EventType.TRAINEE_CREATE_SUCCESS
                    : EventType.TRAINEE_CREATE_FAILED;
        } else {
            eventType = resultEvent.getResult() == Result.OK
                    ? EventType.TRAINER_CREATED_SUCCESS
                    : EventType.TRAINER_CREATE_FAILED;
        }

        log.info("Profile create event {}", resultEvent);

        publish(registerUser, eventType, resultEvent, eventId, resultEvent.getCorrelationId());
    }
}
