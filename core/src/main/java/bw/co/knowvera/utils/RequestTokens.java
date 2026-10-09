package bw.co.knowvera.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Tokens in links sent to clients (client request confirmation, identity confirmation and
 * registration). A token is {@code <random>.<issued epoch seconds>}; the whole string is stored
 * as a BCrypt hash, so its issue time cannot be changed without invalidating it.
 */
public final class RequestTokens {

    private RequestTokens() {
    }

    /** A new token of {@code length} random letters and digits, stamped with the current time. */
    public static String issue(int length) {
        return RandomStringUtils.secure().next(length, true, true) + "." + Instant.now().getEpochSecond();
    }

    /**
     * Whether the token is older than {@code ttl}. Tokens issued before expiry existed carry no
     * time and count from {@code fallbackIssuedAt}; without one they are expired.
     */
    public static boolean isExpired(String token, Duration ttl, LocalDateTime fallbackIssuedAt) {

        Instant issuedAt = issuedAt(token);
        if (issuedAt == null && fallbackIssuedAt != null) {
            issuedAt = fallbackIssuedAt.atZone(ZoneId.systemDefault()).toInstant();
        }
        return issuedAt == null || issuedAt.plus(ttl).isBefore(Instant.now());
    }

    private static Instant issuedAt(String token) {

        int dot = token == null ? -1 : token.lastIndexOf('.');
        if (dot < 0) {
            return null;
        }
        try {
            return Instant.ofEpochSecond(Long.parseLong(token.substring(dot + 1)));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
