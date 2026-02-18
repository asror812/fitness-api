package com.example.auth_service.jms;

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

    @Value("${jms.queues.workload_update_request_queue}")
    private String updateTrainerWorkloadRequestQueue;

    @Value("${jms.queues.create_trainee_request_queue}")
    private String createTraineeRequestQueue;

    @Value("${jms.queues.create_trainer_request_queue}")
    private String createTrainerRequestQueue;

    public static final String HDR_EVENT_ID = "eventId";
    public static final String HDR_EVENT_TYPE = "eventType";
    public static final String HDR_USER_ID = "userId";

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public void publishUpdateWorkload(Object dto, UUID eventId, UUID userId) {
        publish(updateTrainerWorkloadRequestQueue, EventType.WORKLOAD_UPDATE_REQUESTED, dto, eventId, userId);
    }

    public void publishCreateTrainee(Object dto, UUID eventId, UUID userId) {
        publish(createTraineeRequestQueue, EventType.TRAINEE_CREATE_REQUESTED, dto, eventId, userId);
    }

    public void publishCreateTrainer(Object dto, UUID eventId, UUID userId) {
        publish(createTrainerRequestQueue, EventType.TRAINER_CREATE_REQUESTED, dto, eventId, userId);
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
