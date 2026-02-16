package com.example.fitness_service.indicator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemoryHealthIndicator implements HealthIndicator {

    private final MemoryStatsProvider memoryStatsProvider;

    private static final String FREE_MEMORY = "free memory";
    private static final String TOTAL_MEMORY = "total memory";
    private static final String FREE_MEMORY_PERCENTAGE = "free memory percentage";

    @Override
    public Health health() {
        long freeMemory = memoryStatsProvider.getFreeMemory() / 1024;
        long totalMemory = memoryStatsProvider.getTotalMemory() / 1024;
        double freeMemoryPercentage = (freeMemory * 100.0) / totalMemory;

        if (freeMemoryPercentage < 10) {
            return Health.status(new Status("DOWN", "Low available memory!"))
                    .withDetail(FREE_MEMORY, freeMemory)
                    .withDetail(TOTAL_MEMORY, totalMemory)
                    .withDetail(FREE_MEMORY_PERCENTAGE, freeMemoryPercentage)
                    .build();
        } else if (freeMemoryPercentage < 30) {
            return Health.status(new Status("WARN", "Memory usage is high"))
                    .withDetail(FREE_MEMORY, freeMemory)
                    .withDetail(TOTAL_MEMORY, totalMemory)
                    .withDetail(FREE_MEMORY_PERCENTAGE, freeMemoryPercentage)
                    .build();
        } else {
            return Health.status(new Status("UP", "Memory is healthy"))
                    .withDetail(FREE_MEMORY, freeMemory)
                    .withDetail(TOTAL_MEMORY, totalMemory)
                    .withDetail(FREE_MEMORY_PERCENTAGE, freeMemoryPercentage)
                    .build();
        }
    }
}
