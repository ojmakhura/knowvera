package bw.co.knowvera.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class RequestTokensTest {

    @Test
    void newTokensAreRandomAndValidWithinTheirLifetime() {
        String token = RequestTokens.issue(32);

        assertEquals(32, token.substring(0, token.lastIndexOf('.')).length());
        assertNotEquals(token, RequestTokens.issue(32));
        assertFalse(RequestTokens.isExpired(token, Duration.ofHours(24), null));
    }

    @Test
    void tokensExpire() {
        String old = "abc." + Instant.now().minus(Duration.ofHours(25)).getEpochSecond();

        assertTrue(RequestTokens.isExpired(old, Duration.ofHours(24), null));
        assertFalse(RequestTokens.isExpired(old, Duration.ofDays(7), null));
    }

    @Test
    void legacyTokensCountFromTheRequestOrAreExpired() {
        assertFalse(RequestTokens.isExpired("legacy", Duration.ofDays(7), LocalDateTime.now().minusDays(1)));
        assertTrue(RequestTokens.isExpired("legacy", Duration.ofDays(7), LocalDateTime.now().minusDays(8)));
        assertTrue(RequestTokens.isExpired("legacy", Duration.ofDays(7), null));
    }

    @Test
    void theIssueTimeCannotBeChangedWithoutInvalidatingTheToken() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String token = RequestTokens.issue(32);
        String stored = encoder.encode(token);

        String extended = token.substring(0, token.lastIndexOf('.') + 1) + Instant.now().plus(Duration.ofDays(365)).getEpochSecond();
        assertTrue(encoder.matches(token, stored));
        assertFalse(encoder.matches(extended, stored));
    }
}
