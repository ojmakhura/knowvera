package bw.co.knowvera.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class RateLimitFilterTest {

    private final RateLimitFilter filter = new RateLimitFilter(100, 2);

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private MockHttpServletResponse call(String ip) throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/individuals/request/x");
        request.setRemoteAddr(ip);
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, new MockFilterChain());
        return response;
    }

    @Test
    void anonymousCallersAreLimitedPerAddress() throws Exception {
        assertEquals(200, call("203.0.113.5").getStatus());
        assertEquals(200, call("203.0.113.5").getStatus());

        MockHttpServletResponse limited = call("203.0.113.5");
        assertEquals(429, limited.getStatus());
        assertNotNull(limited.getHeader("Retry-After"));

        // Another address has its own allowance
        assertEquals(200, call("198.51.100.7").getStatus());
    }
}
