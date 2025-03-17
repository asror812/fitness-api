package com.example.demo.indicator;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;


@Component
public class DatabaseHealthIndicator implements HealthIndicator{
   
    @Autowired
    private DataSource dataSource;

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            return Health.up().withDetail("database", "Available").build();
        } catch (Exception e) {
            return Health.down().withDetail("database", "Not Available").build();
        }
    }
}
