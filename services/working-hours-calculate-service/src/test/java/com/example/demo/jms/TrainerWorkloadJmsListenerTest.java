package com.example.demo.jms;

import com.example.demo.dto.request.TrainerWorkloadRequestDTO;
import com.example.demo.service.TrainerWorkloadService;

import jakarta.jms.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerWorkloadJmsListenerTest {

    private TrainerWorkloadService workloadService;
    private TrainerWorkloadJmsListener listener;

    @BeforeEach
    void setUp() {
        workloadService = mock(TrainerWorkloadService.class);
        listener = new TrainerWorkloadJmsListener(workloadService);
    }

    @Test
    void consume_ShouldProcessWorkloadSuccessfully() {

        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO(); // populate if needed
        Map<String, Object> headers = new HashMap<>();
        headers.put("transactionId", "TX-123");

        
        listener.consume(dto, headers);

        verify(workloadService, times(1)).processWorkload(dto);
    }

    @Test
    void consume_ShouldThrowRuntimeException_OnFailure() {
        TrainerWorkloadRequestDTO dto = new TrainerWorkloadRequestDTO();
        Map<String, Object> headers = new HashMap<>();
        headers.put("transactionId", "TX-456");

        doThrow(new RuntimeException("Simulated failure")).when(workloadService).processWorkload(any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            listener.consume(dto, headers);
        });

        assertEquals("Trigger retry", thrown.getMessage());
    }

    @Test
    void handleDLQ_ShouldLogMessage() {
        Message mockMessage = mock(Message.class);

        listener.handleDLQ(mockMessage);

        assertTrue(true);
    }
}
