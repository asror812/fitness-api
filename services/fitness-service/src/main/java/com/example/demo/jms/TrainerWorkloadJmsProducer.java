package com.example.demo.jms;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.example.demo.dto.request.ActionType;
import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.model.Training;

@Component
public class TrainerWorkloadJmsProducer {

    @Value("${jms.workload_queue.update}")
    private String updateTrainerWorkloadQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerWorkloadJmsProducer.class);

    public void updateTrainingSession(TrainerWorkloadRequestDTO requestDTO) {
        String transactionId = MDC.get("transactionID");
        // headers.add("Authorization", "Bearer " +
        // jwtService.generateTokenForMicroservice());

        LOGGER.info("Queue name : {} Request Entity: {}", updateTrainerWorkloadQueue, requestDTO);

        jmsTemplate.convertAndSend(updateTrainerWorkloadQueue, requestDTO, message -> {
            message.setStringProperty("transactionId", transactionId);
            return message;
        });
    }

    public void notifyTrainerDeletion(Training training) {
        TrainerWorkloadRequestDTO requestDTO = TrainerWorkloadRequestDTO.builder()
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .trainerFirstName(training.getTrainer().getUser().getFirstName())
                .trainerLastName(training.getTrainer().getUser().getLastName())
                .duration(training.getDuration())
                .trainingDate(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(training.getTrainingDate())))
                .actionType(ActionType.DELETE)
                .build();

        updateTrainingSession(requestDTO);
    }
}
