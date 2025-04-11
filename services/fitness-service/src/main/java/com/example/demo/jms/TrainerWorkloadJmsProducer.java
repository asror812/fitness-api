package com.example.demo.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;


@Component
public class TrainerWorkloadJmsProducer {

    @Value("${jms.workload_queue.update}")
    private String updateTrainerWorkloadQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadJmsProducer.class);
    private static final String transactionId = MDC.get("transactionID");


    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        LOGGER.info("Queue name : {} Request Entity: {}", updateTrainerWorkloadQueue, requestDTO);

        jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, requestDTO, message -> {
            message.setStringProperty("transactionId", transactionId);
            return message;
        });
    }

   
}
