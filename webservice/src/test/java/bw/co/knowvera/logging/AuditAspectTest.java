package bw.co.knowvera.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import bw.co.knowvera.audit.AuditLogDTO;
import bw.co.knowvera.audit.AuditLogService;
import tools.jackson.databind.json.JsonMapper;

class AuditAspectTest {

    static class Controller {

        @Audit(entity = "TEST", eventLabel = "#id", logData = false)
        public ResponseEntity<String> load(String id) {
            return ResponseEntity.ok("loaded " + id);
        }

        @Audit(entity = "TEST", eventLabel = "#id", logData = true)
        public ResponseEntity<String> fail(String id) {
            throw new IllegalStateException("boom");
        }
    }

    private final AuditLogService auditLogService = mock(AuditLogService.class);
    private Controller controller;

    @BeforeEach
    void setUp() {
        AspectJProxyFactory factory = new AspectJProxyFactory(new Controller());
        factory.setProxyTargetClass(true);
        factory.addAspect(new AuditAspect(JsonMapper.builder().build(), auditLogService, new AuditContextProvider()));
        controller = factory.getProxy();

        // Public endpoints run as the anonymous user, whose principal is not a Jwt
        SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken(
                "key", "anonymousUser", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))));
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private AuditLogDTO savedLog() {
        ArgumentCaptor<AuditLogDTO> captor = ArgumentCaptor.forClass(AuditLogDTO.class);
        verify(auditLogService).save(captor.capture());
        return captor.getValue();
    }

    @Test
    void auditsAnonymousCalls() {
        assertEquals("loaded 42", controller.load("42").getBody());

        AuditLogDTO log = savedLog();
        assertEquals("load", log.getEvent());
        assertEquals("42", log.getEventLabel());
        assertEquals("anonymousUser", log.getUsername());
        assertNull(log.getUserId());
    }

    @Test
    void failuresKeepTheirExceptionAndAreAudited() {
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> controller.fail("7"));
        assertEquals("boom", thrown.getMessage());

        AuditLogDTO log = savedLog();
        assertEquals("7", log.getEventLabel());
        assertEquals(Map.of("outcome", "FAILURE", "error", "IllegalStateException"), log.getLogData());
    }

    @Test
    void auditStoreFailureDoesNotBreakTheCall() {
        doThrow(new RuntimeException("database down")).when(auditLogService).save(any());

        assertEquals("loaded 1", controller.load("1").getBody());
    }
}
