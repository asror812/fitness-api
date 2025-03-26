package com.example.demo.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.exceptions.TooManyRequestsException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class BruteForceProtectorService {

    @Value("${security.failure.count:5}")
    private Integer maxFailureCounts;

    @Value("${security.failure.duration:300}")
    private Integer blockTime;

    private static final Logger LOGGER = LoggerFactory.getLogger(BruteForceProtectorService.class);
    protected static final Map<String, FailureCount> FAILURE_COUNTS = new ConcurrentHashMap<>();
    private static final String TOO_MANY_REQUESTS = "You have exceeded the maximum number of login attempts. Please try again after some time";

    public boolean isBlocked(String username) {
        FailureCount info = FAILURE_COUNTS.get(username);

        if (info == null) {
            return false;
        }

        if (info.getCount() < maxFailureCounts) {
            return false;
        }

        if (info.getCount() >= maxFailureCounts
                && LocalDateTime.now().isAfter(info.getDateTime().plusSeconds(blockTime))) {

            LOGGER.info("User '{}' is now unblocked after timeout.", username);
            FAILURE_COUNTS.remove(username);
            return false;
        }

        LOGGER.warn("User '{}' has reached max login attempts. Account is blocked until {}.", username,
                info.getDateTime().plusSeconds(blockTime));
        return true;
    }

    public void resetAttempts(String username) {
        FAILURE_COUNTS.remove(username);
    }

    public void addFailedAttempt(String username) {
        FailureCount info = FAILURE_COUNTS.get(username);

        if (info == null) {
            FAILURE_COUNTS.put(username, new FailureCount(1, LocalDateTime.now()));
            LOGGER.info("First failed login attempt for user: {}", username);
        }

        else if (info.getCount() < maxFailureCounts) {
            info.setCount(info.getCount() + 1);
            info.setDateTime(LocalDateTime.now());
            LOGGER.info("Failed login attempt {} for user: {}", info.getCount(), username);
        }

        else {
            LOGGER.warn("User '{}' has reached max login attempts. Account is blocked until {}.", username,
                    info.getDateTime().plusSeconds(blockTime));
            throw new TooManyRequestsException(TOO_MANY_REQUESTS);
        }
    }
}
