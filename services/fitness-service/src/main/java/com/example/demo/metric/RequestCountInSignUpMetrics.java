package com.example.demo.metric;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class RequestCountInSignUpMetrics {
    private final Counter requestCounter;

    public RequestCountInSignUpMetrics(MeterRegistry registry) {
        this.requestCounter = Counter.builder("http_sign_up_requests_total")
                .description("Total HTTP sign up requests received")
                .register(registry);
    }

    public void increment() {
        requestCounter.increment();
    }
}
