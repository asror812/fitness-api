package com.example.fitness_service.jms.outbox;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = """
            select e from OutboxEvent e
            where e.status = 'NEW'
              and e.nextAttemptAt <= :now
            order by e.createdAt asc
            """)
    List<OutboxEvent> pickBatchForSend(@Param("now") OffsetDateTime now, Pageable pageable);
}
