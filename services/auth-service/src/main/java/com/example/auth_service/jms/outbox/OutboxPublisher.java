package com.example.auth_service.jms.outbox;

import java.time.OffsetDateTime;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.auth_service.jms.JmsPublisher;
import com.example.auth_service.jms.dto.TraineeCreateReqDto;
import com.example.auth_service.jms.dto.TrainerCreateReqDto;
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
                    case EventType.TRAINEE_CREATE_REQUESTED -> {
                        TraineeCreateReqDto dto = objectMapper.treeToValue(e.getPayload(), TraineeCreateReqDto.class);
                        producer.publishCreateTrainee(dto, e.getId(), dto.getUserId());
                    }

                    case EventType.TRAINER_CREATE_REQUESTED -> {
                        TrainerCreateReqDto dto = objectMapper.treeToValue(e.getPayload(), TrainerCreateReqDto.class);
                        producer.publishCreateTrainer(dto, e.getId(), dto.getUserId());
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
