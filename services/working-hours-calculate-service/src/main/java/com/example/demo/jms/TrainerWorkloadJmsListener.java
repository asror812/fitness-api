package com.example.demo.jms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.service.TrainerWorkloadService;

import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TrainerWorkloadJmsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadJmsListener.class);
    private final TrainerWorkloadService workloadService;

    @Value("${jms.workload_queue.update}")
    public String updateTrainerWorkloadQueue;

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String brokerUser;

    @Value("${spring.activemq.password}")
    private String brokerPassword;

    @JmsListener(destination = "main-a-queue", containerFactory = "jmsListenerContainerFactory")
    public void consume(TrainerWorkloadRequestDTO dto, @Headers Map<String, Object> headers) {
        String transactionId = (String) headers.get("transactionId");
        try {
            LOGGER.info("Processing message with Transaction ID: {}", transactionId);
            workloadService.processWorkload(dto);
            LOGGER.info("Successfully processed {}", dto);
        } catch (Exception e) {
            LOGGER.error("Message processing failed, will be retried", e);
            throw new RuntimeException("Trigger retry");
        }
    }

    @JmsListener(destination = "ActiveMQ.DLQ")
    public void handleDLQ(Message failedMessage) {
        LOGGER.error("Message sent to DLQ: {}", failedMessage);
    }

}
