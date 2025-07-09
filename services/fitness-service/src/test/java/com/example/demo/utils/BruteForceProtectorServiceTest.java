package com.example.demo.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.exception.TooManyRequestsException;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BruteForceProtectorServiceTest {

    private BruteForceProtectorService bruteForceProtectorService;

    @BeforeEach
    void setUp() {
        bruteForceProtectorService = new BruteForceProtectorService();
        ReflectionTestUtils.setField(bruteForceProtectorService, "maxFailureCounts", 3);
        ReflectionTestUtils.setField(bruteForceProtectorService, "blockTime", 300);
    }

    @Test
    void testIsBlocked_UserNotInFailureCounts_ShouldReturnTrue() {
        assertFalse(bruteForceProtectorService.isBlocked("user1"));
    }

    @Test
    void testIsBlocked_UserBelowMaxFailureCounts_ShouldReturnFalse() {
        BruteForceProtectorService.FAILURE_COUNTS.put("user1", new FailureCount(2, LocalDateTime.now()));
        assertFalse(bruteForceProtectorService.isBlocked("user1"));
    }

    @Test
    void testIsBlocked_UserExceedsMaxFailureCountsAndStillBlocked_ShouldReturnTrue() {
        BruteForceProtectorService.FAILURE_COUNTS.put("user1", new FailureCount(3, LocalDateTime.now()));
        assertTrue(bruteForceProtectorService.isBlocked("user1"));
    }

    @Test
    void testAddFailedAttempt_FirstAttempt_ShouldAddUserToFailureCounts() {
        bruteForceProtectorService.addFailedAttempt("user1");
        assertTrue(BruteForceProtectorService.FAILURE_COUNTS.containsKey("user1"));
        assertEquals(1, BruteForceProtectorService.FAILURE_COUNTS.get("user1").getCount());
    }

    @Test
    void testAddFailedAttempt_SubsequentAttempts_ShouldIncrementFailureCount() {
        BruteForceProtectorService.FAILURE_COUNTS.put("user1", new FailureCount(1, LocalDateTime.now()));
        bruteForceProtectorService.addFailedAttempt("user1");
        assertEquals(2, BruteForceProtectorService.FAILURE_COUNTS.get("user1").getCount());
    }

    @Test
    void testAddFailedAttempt_ExceedsMaxFailureCounts_ShouldThrowTooManyRequestsException() {
        BruteForceProtectorService.FAILURE_COUNTS.put("user1", new FailureCount(3, LocalDateTime.now()));
        TooManyRequestsException exception = assertThrows(TooManyRequestsException.class, () -> {
            bruteForceProtectorService.addFailedAttempt("user1");
        });
        assertEquals("You have exceeded the maximum number of login attempts. Please try again after some time",
                exception.getMessage());
    }
}