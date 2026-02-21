package com.example.auth_service.jms.event;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final ProcessedEventRepository repository;

    @Transactional
    public boolean tryMarkProcessed(UUID eventId) {
        try {
            repository.save(ProcessedEvent.builder()
                    .eventId(eventId)
                    .processedAt(OffsetDateTime.now())
                    .build());
            return true;

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return false;
        }
    }
}
