package bw.co.knowvera.config;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Per-caller request limit (token bucket, per instance): signed-in users by user name, callers
 * without a token (public endpoints, probing) by IP address with a much lower limit. Runs after
 * token authentication and before the Keycloak policy enforcer. The IP is the connection's remote
 * address: forwarding headers count only from trusted proxies (server.forward-headers-strategy).
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_TRACKED_CALLERS = 100_000;

    private final long userRequestsPerMinute;
    private final long anonymousRequestsPerMinute;

    private final Map<String, Bucket> buckets = Collections.synchronizedMap(
            new LinkedHashMap<String, Bucket>(1024, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<String, Bucket> eldest) {
                    return size() > MAX_TRACKED_CALLERS;
                }
            });

    public RateLimitFilter(long userRequestsPerMinute, long anonymousRequestsPerMinute) {
        this.userRequestsPerMinute = userRequestsPerMinute;
        this.anonymousRequestsPerMinute = anonymousRequestsPerMinute;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean signedIn = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        String caller = signedIn ? "user:" + auth.getName() : "ip:" + request.getRemoteAddr();
        long limit = signedIn ? userRequestsPerMinute : anonymousRequestsPerMinute;

        Bucket bucket = buckets.computeIfAbsent(caller, key -> Bucket.builder()
                .addLimit(l -> l.capacity(limit).refillGreedy(limit, Duration.ofMinutes(1)))
                .build());

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.setHeader("X-RateLimit-Remaining", Long.toString(probe.getRemainingTokens()));
            chain.doFilter(request, response);
            return;
        }

        long retryAfter = Math.max(1, TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", Long.toString(retryAfter));
        response.setContentType("application/problem+json");
        response.getWriter().write("{\"type\":\"too-many-requests\",\"title\":\"TOO_MANY_REQUESTS\",\"status\":429,"
                + "\"detail\":\"Too many requests; retry in " + retryAfter + " seconds\"}");
    }
}
