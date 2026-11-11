package bw.co.knowvera.novu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

class NovuAuthControllerTest {

    @Test
    void notConfiguredIsServiceUnavailable() {

        NovuAuthController controller = new NovuAuthController(service(""));

        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> controller.getNovuConfig(jwt(Map.of("email", "ann@example.com"))));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, e.getStatusCode());
    }

    @Test
    void tokenWithoutEmailIsBadRequest() {

        NovuAuthController controller = new NovuAuthController(service("test-key"));

        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> controller.getNovuConfig(jwt(Map.of("sub", "user-1"))));
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
    }

    @Test
    void returnsTheSubscriberAndItsHash() {

        NovuAuthService service = service("test-key");
        Map<String, String> config = new NovuAuthController(service)
                .getNovuConfig(jwt(Map.of("email", "ann@example.com")));

        assertEquals("ann@example.com", config.get("subscriberId"));
        assertEquals(service.generateSubscriberHash("ann@example.com"), config.get("subscriberHash"));
    }

    private static NovuAuthService service(String apiKey) {
        NovuAuthService service = new NovuAuthService();
        ReflectionTestUtils.setField(service, "novuApiKey", apiKey);
        return service;
    }

    private static Jwt jwt(Map<String, Object> claims) {
        return new Jwt("token", Instant.now(), Instant.now().plusSeconds(60), Map.of("alg", "none"), claims);
    }
}
