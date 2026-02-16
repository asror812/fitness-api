/* package com.example.workload_service.jms;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import com.example.workload_service.dto.request.TrainerWorkloadRequestDTO;
import com.example.workload_service.service.TrainerWorkloadService;

import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkloadMessageListener {

    private final TrainerWorkloadService workloadService;
    private final ActiveMQProperties properties;


    @Value("${jms.workload_queue.update}")
    private String updateTrainerWorkloadQueue;

    @Value("${jms.create_trainee_queue}")
    private String createTraineeQueue;

    @Value("${jms.create_trainer_queue}")
    private String createTrainerQueue;


    @JmsListener(destination = "${jms.workload_update_queue}", containerFactory = "jmsListenerContainerFactory")
    public void consume(TrainerWorkloadRequestDTO dto, @Headers Map<String, Object> headers) {
        String transactionId = (String) headers.get("transactionId");

        try {
            log.info("Processing message with Transaction ID: {}", transactionId);
            workloadService.processWorkload(dto);
            log.info("Successfully processed {}", dto);
        } catch (Exception e) {
            log.error("Message processing failed, will be retried", e.getCause());
        }
    }


    @JmsListener(destination = "${jms.create_trainee_queue}", containerFactory = "jmsListenerContainerFactory")
    public void consume(TrainerWorkloadRequestDTO dto, @Headers Map<String, Object> headers) {
        String transactionId = (String) headers.get("transactionId");

        try {
            log.info("Processing message with Transaction ID: {}", transactionId);
            workloadService.processWorkload(dto);
            log.info("Successfully processed {}", dto);
        } catch (Exception e) {
            log.error("Message processing failed, will be retried", e.getCause());
        }
    }
    
    @JmsListener(destination = "${jms.create_trainer_queue}", containerFactory = "jmsListenerContainerFactory")
    public void consume(TrainerWorkloadRequestDTO dto, @Headers Map<String, Object> headers) {
        String transactionId = (String) headers.get("transactionId");

        try {
            log.info("Processing message with Transaction ID: {}", transactionId);
            workloadService.processWorkload(dto);
            log.info("Successfully processed {}", dto);
        } catch (Exception e) {
            log.error("Message processing failed, will be retried", e.getCause());
        }
    }


    @JmsListener(destination = "ActiveMQ.DLQ")
    public void handleDLQ(Message failedMessage) {
        log.error("Message sent to DLQ: {}", failedMessage);
    }
    
}
 */