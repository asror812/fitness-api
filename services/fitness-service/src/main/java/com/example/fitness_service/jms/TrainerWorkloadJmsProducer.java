package com.example.fitness_service.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.example.fitness_service.dto.request.TrainerWorkloadRequestDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerWorkloadJmsProducer {

    @Value("${jms.workload_queue.update}")
    String updateTrainerWorkloadQueue;

    private final ObjectMapper objectMapper;

    private final JmsTemplate jmsTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadJmsProducer.class);
    private static final String TRANSACTION_ID = MDC.get("transactionID");


    public void updateTrainingSession(String json) throws JsonProcessingException {

        TrainerWorkloadRequestDTO requestDTO = objectMapper.readValue(json, TrainerWorkloadRequestDTO.class);
        LOGGER.info("Queue name : {} Request Entity: {}", updateTrainerWorkloadQueue, requestDTO);

        jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, requestDTO, message -> {
            message.setStringProperty("transactionId", TRANSACTION_ID);
            return message;
        });
    }

}
