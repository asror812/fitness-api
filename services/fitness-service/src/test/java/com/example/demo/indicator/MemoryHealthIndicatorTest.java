package com.example.demo.indicator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemoryHealthIndicatorTest {

    @Test
    void healthStatusUp() {
        MemoryStatsProvider mockProvider = mock(MemoryStatsProvider.class);
        when(mockProvider.getFreeMemory()).thenReturn(900L * 1024);
        when(mockProvider.getTotalMemory()).thenReturn(1000L * 1024);

        MemoryHealthIndicator indicator = new MemoryHealthIndicator(mockProvider);
        Health health = indicator.health();

        assertEquals("UP", health.getStatus().getCode());
        assertEquals("Memory is healthy", health.getStatus().getDescription());
    }

    @Test
    void healthStatusWarn() {
        MemoryStatsProvider mockProvider = mock(MemoryStatsProvider.class);
        when(mockProvider.getFreeMemory()).thenReturn(250L * 1024);
        when(mockProvider.getTotalMemory()).thenReturn(1000L * 1024);

        MemoryHealthIndicator indicator = new MemoryHealthIndicator(mockProvider);
        Health health = indicator.health();

        assertEquals("WARN", health.getStatus().getCode());
        assertEquals("Memory usage is high", health.getStatus().getDescription());
    }

    @Test
    void healthStatusDown() {
        MemoryStatsProvider mockProvider = mock(MemoryStatsProvider.class);
        when(mockProvider.getFreeMemory()).thenReturn(90L * 1024);
        when(mockProvider.getTotalMemory()).thenReturn(1000L * 1024);

        MemoryHealthIndicator indicator = new MemoryHealthIndicator(mockProvider);
        Health health = indicator.health();

        assertEquals("DOWN", health.getStatus().getCode());
        assertEquals("Low available memory!", health.getStatus().getDescription());
    }
}
