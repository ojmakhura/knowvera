package bw.co.knowvera.novu;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bw.co.knowvera.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Operations on the Novu dead-letter queues: notifications that failed every retry. Permissions:
 * keycloak/generate_authz.py (resource "novu").
 */
@RestController
@RequestMapping("/novu")
@Tag(name = "Novu", description = "Endpoints for Novu integration")
public class NovuDeadLetterController {

    private final NovuDeadLetterService deadLetters;

    public NovuDeadLetterController(NovuDeadLetterService deadLetters) {
        this.deadLetters = deadLetters;
    }

    @GetMapping("/dead-letters")
    @PreAuthorize("hasAuthority('SCOPE_novu:view')")
    @Operation(summary = "Dead-letter counts", description = "Failed notifications waiting in each Novu dead-letter queue, by queue")
    public ResponseEntity<Map<String, Long>> getDeadLetterCounts() {
        return ResponseEntity.ok(deadLetters.counts());
    }

    @PostMapping("/dead-letters/{queue}/redrive")
    @PreAuthorize("hasAuthority('SCOPE_novu:edit')")
    @Audit(entity = "NOTIFICATION", eventLabel = "#queue + ' ' + #max", logData = false)
    @Operation(summary = "Re-drive dead letters", description = "Move failed notifications back to their queue (e.g. q.novu-new-user) to be sent again")
    public ResponseEntity<Map<String, Object>> redriveDeadLetters(@PathVariable("queue") String queue,
            @RequestParam(name = "max", defaultValue = "100") int max) {

        int moved = deadLetters.redrive(queue, max);
        return ResponseEntity.ok(Map.of("queue", queue, "redriven", moved,
                "remaining", deadLetters.counts().getOrDefault(queue, 0L)));
    }
}
