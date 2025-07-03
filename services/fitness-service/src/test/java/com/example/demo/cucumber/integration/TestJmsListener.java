package com.example.demo.cucumber.integration;

import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import jakarta.jms.ObjectMessage;
import jakarta.jms.JMSException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class TestJmsListener {

    private final BlockingQueue<TrainerWorkloadRequestDTO> receivedMessages = new LinkedBlockingQueue<>();

    @JmsListener(destination = "${jms.workload_queue.update}")
    public void receiveMessage(ObjectMessage message) throws JMSException {
        Object payload = message.getObject();
        if (payload instanceof TrainerWorkloadRequestDTO dto) {
            receivedMessages.add(dto);
        }
    }

    public TrainerWorkloadRequestDTO awaitMessage(long timeoutMillis) throws InterruptedException {
        return receivedMessages.poll(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
