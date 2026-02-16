package com.example.fitness_service.indicator;

import org.springframework.stereotype.Component;

@Component
public class MemoryStatsProvider {
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }
}
