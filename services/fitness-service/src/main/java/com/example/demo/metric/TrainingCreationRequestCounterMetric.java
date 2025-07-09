package com.example.demo.metric;

import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class TrainingCreationRequestCounterMetric {
    private final Counter trainingCreationRequestCounter;

    public TrainingCreationRequestCounterMetric(MeterRegistry registry) {
        this.trainingCreationRequestCounter = Counter.builder("training-create.request.count")
                .description("Number of training sessions created per day")
                .register(registry);
    }

    public void incrementTrainingCreationRequestCounter() {
        trainingCreationRequestCounter.increment();
    }
}
