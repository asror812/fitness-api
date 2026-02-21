package com.example.fitness_service.jms.event;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.fitness_service.jms.event.ProcessedEvent.ProcessingStatus;

import org.springframework.dao.DataIntegrityViolationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyService {
    private final ProcessedEventRepository repository;

    public boolean tryMarkProcessed(UUID eventId, UUID correlationId) {
        log.info("Idempotency tryMarkProcessed start eventId={} correlationId={}", eventId, correlationId);
        try {
            repository.saveAndFlush(ProcessedEvent.builder()
                    .eventId(eventId)
                    .correlationId(correlationId)
                    .processedAt(OffsetDateTime.now())
                    .status(ProcessingStatus.PROCESSING)
                    .build());
            log.info("Idempotency tryMarkProcessed success eventId={} status={}", eventId, ProcessingStatus.PROCESSING);
            return true;

        } catch (DataIntegrityViolationException | org.hibernate.exception.ConstraintViolationException e) {
            log.info("Idempotency tryMarkProcessed duplicate eventId={} reason={}", eventId, e.getClass().getSimpleName());
            return false;
        }
    }

    @Transactional
    public void markSuccess(UUID eventId) {
        log.info("Idempotency markSuccess start eventId={}", eventId);
        repository.findByEventId(eventId).ifPresent(pe -> {
            pe.setStatus(ProcessedEvent.ProcessingStatus.PROCESSED);
            pe.setErrorMessage(null);
            pe.setProcessedAt(OffsetDateTime.now());
            log.info("Idempotency markSuccess done eventId={} status={}", eventId, pe.getStatus());
        });
    }

    @Transactional
    public void markFailed(UUID eventId, UUID correlationId, String error) {
        log.warn("Idempotency markFailed start eventId={} correlationId={} error={}", eventId, correlationId, error);
        ProcessedEvent processedEvent = repository.findByEventId(eventId)
                .orElseGet(() -> ProcessedEvent.builder()
                        .eventId(eventId)
                        .correlationId(correlationId)
                        .status(ProcessedEvent.ProcessingStatus.PROCESSING)
                        .processedAt(OffsetDateTime.now())
                        .build());

        if (processedEvent.getCorrelationId() == null) {
            processedEvent.setCorrelationId(correlationId);
        }

        processedEvent.setStatus(ProcessedEvent.ProcessingStatus.FAILED);
        processedEvent.setErrorMessage(error);
        processedEvent.setProcessedAt(OffsetDateTime.now());
        repository.save(processedEvent);
        log.warn("Idempotency markFailed done eventId={} status={}", eventId, processedEvent.getStatus());
    }
}
