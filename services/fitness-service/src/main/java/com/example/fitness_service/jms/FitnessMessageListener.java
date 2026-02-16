package com.example.fitness_service.jms;

import com.example.fitness_service.exception.JsonSerializationException;
import com.example.fitness_service.jms.outbox.EventType;
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
public class FitnessMessageListener {

    @Value("${jms.workload_queue.update}")
    private String updateTrainerWorkloadQueue;

    @Value("${jms.create_trainee_queue}")
    private String createTraineeQueue;

    @Value("${jms.create_trainer_queue}")
    private String createTrainerQueue;

    public static final String HDR_EVENT_ID = "eventId";
    public static final String HDR_EVENT_TYPE = "eventType";
    public static final String HDR_USER_ID = "userId";

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void publishUpdateWorkload(Object dto, UUID eventId, UUID userId) {
        publish(updateTrainerWorkloadQueue, EventType.WORKLOAD_UPDATE_REQUESTED, dto, eventId, userId);
    }

    public void publishCreateTrainee(Object dto, UUID eventId, UUID userId) {
        publish(createTraineeQueue, EventType.TRAINEE_CREATE_REQUESTED, dto, eventId, userId);
    }

    public void publishCreateTrainer(Object dto, UUID eventId, UUID userId) {
        publish(createTrainerQueue, EventType.TRAINER_CREATE_REQUESTED, dto, eventId, userId);
    }

    private void publish(String queue,
            EventType eventType,
            Object dto,
            UUID eventId,
            UUID userId) {

        final String json = toJson(dto);

        log.info("Publish -> queue={} eventType={} eventId={} userId={}",
                queue, eventType, eventId, userId);

        jmsTemplate.convertAndSend(queue, json, message -> {
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
