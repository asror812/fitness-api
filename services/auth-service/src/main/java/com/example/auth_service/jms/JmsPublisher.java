package com.example.auth_service.jms;

import com.example.auth_service.dto.event.TraineeRegisterEvent;
import com.example.auth_service.dto.event.TrainerRegisterEvent;
import com.example.auth_service.exception.JsonSerializationException;
import com.example.auth_service.jms.outbox.EventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JmsPublisher {

    @Value("${jms.topics.workload_update}")
    private String updateTrainerWorkload;

    @Value("${jms.topics.user_register}")
    private String createTrainee;

    public static final String HDR_EVENT_ID = "eventId";
    public static final String HDR_EVENT_TYPE = "eventType";
    public static final String HDR_USER_ID = "userId";

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void publishUpdateWorkload(Object dto, UUID eventId, UUID correlationId, UUID userId) {
        publish(updateTrainerWorkload, EventType.WORKLOAD_UPDATE_REQUESTED, dto, eventId, correlationId, userId);
    }

    public void publishUserRegistered(TraineeRegisterEvent dto, UUID eventId, UUID correlationId, UUID userId) {
        publish(createTrainee, EventType.TRAINEE_REGISTER_REQUESTED, dto, eventId, correlationId, userId);
    }

    public void publishUserRegistered(TrainerRegisterEvent dto, UUID eventId, UUID correlationId, UUID userId) {
        publish(createTrainee, EventType.TRAINER_REGISTER_REQUESTED, dto, eventId, correlationId, userId);
    }

    private void publish(String topic,
            EventType eventType,
            Object dto,
            UUID eventId,
            UUID correlationId,
            UUID userId) {

        final String json = toJson(dto);

        log.info("Publish -> topic={} eventType={} eventId={}  correlationId={} userId={}",
                topic, eventType, eventId, correlationId, userId);

        jmsTemplate.convertAndSend(topic, json, message -> {
            message.setStringProperty(HDR_EVENT_ID, eventId.toString());
            message.setStringProperty(HDR_EVENT_TYPE, eventType.name());

            if (userId != null) {
                message.setStringProperty(HDR_USER_ID, userId.toString());
            }

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
}
