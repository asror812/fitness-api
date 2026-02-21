package com.example.auth_service.jms.outbox;

import java.time.OffsetDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.event.TraineeRegisterEvent;
import com.example.auth_service.dto.event.TrainerRegisterEvent;
import com.example.auth_service.jms.JmsPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;

    private final ObjectMapper objectMapper;
    private final JmsPublisher producer;

    @Scheduled(fixedDelayString = "${outbox.publisher.delay-ms:2000}")
    @Transactional
    public void publish() {
        OffsetDateTime now = OffsetDateTime.now();

        var events = outboxEventRepository.pickBatchForSend(
                Status.NEW,
                now,
                PageRequest.of(0, 50));

        for (OutboxEvent e : events) {
            try {
                switch (e.getEventType()) {
                    case EventType.TRAINEE_REGISTER_REQUESTED -> {
                        TraineeRegisterEvent dto = objectMapper.treeToValue(e.getPayload(), TraineeRegisterEvent.class);
                        producer.publishUserRegistered(dto, e.getId(), e.getCorrelationId(), dto.getUserId());
                    }

                    case EventType.TRAINER_REGISTER_REQUESTED -> {
                        TrainerRegisterEvent dto = objectMapper.treeToValue(e.getPayload(), TrainerRegisterEvent.class);
                        producer.publishUserRegistered(dto, e.getId(), e.getCorrelationId(), dto.getUserId());
                    }

                    case EventType.WORKLOAD_UPDATE_REQUESTED -> {
                        // objectMapper.readValue(e.getPayload(), Object.class);
                        // TO DO
                    }

                    default -> throw new IllegalStateException("Unknown event: " + e.getEventType());
                }

                e.setStatus(Status.NEW);
                e.setSentAt(OffsetDateTime.now());

            } catch (Exception ex) {
                int nextAttempts = e.getAttempts() + 1;
                e.setAttempts(nextAttempts);
                e.setStatus(Status.NEW); // оставляем NEW чтобы ретраить
                e.setNextAttemptAt(OffsetDateTime.now().plusSeconds(backoffSeconds(nextAttempts)));
            }
        }
        // JPA сам сохранит изменения в конце транзакции
    }

    private long backoffSeconds(int attempts) {
        // простой backoff: 2, 4, 8, 16, max 300
        return Math.min(300, (long) Math.pow(2, Math.min(attempts, 8)));
    }
}
