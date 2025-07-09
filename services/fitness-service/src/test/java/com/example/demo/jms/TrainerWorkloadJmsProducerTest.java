package com.example.demo.jms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import com.example.demo.dto.request.TrainerWorkloadRequestDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerWorkloadJmsProducerTest {

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private TrainerWorkloadJmsProducer producer;

    @BeforeEach
    void setUp() {
        producer.updateTrainerWorkloadQueue = "testQueue";
    }

    @Test
    void updateTrainingSession() throws Exception {
        TrainerWorkloadRequestDTO requestDTO = new TrainerWorkloadRequestDTO();

        doNothing().when(jmsTemplate).convertAndSend(eq("testQueue"), eq(requestDTO), any());

        producer.updateTrainingSession(requestDTO);

        verify(jmsTemplate, times(1)).convertAndSend(eq("testQueue"), eq(requestDTO), any());
    }
}
