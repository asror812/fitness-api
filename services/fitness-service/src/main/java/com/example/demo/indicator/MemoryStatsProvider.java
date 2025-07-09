package com.example.demo.indicator;

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
