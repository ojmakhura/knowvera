package bw.co.roguesystems.ai.profile;

import java.time.Instant;
import java.util.List;

/**
 * A named, versioned processing pipeline. Saving a profile creates its next version; jobs record
 * the version they ran with.
 *
 * @param code        stable identifier, referenced by the callers (e.g. a document type)
 * @param version     set by the processor
 * @param name        display name
 * @param description what the profile is for
 * @param steps       the steps, run in this order
 * @param createdAt   when this version was saved (set by the processor)
 * @param createdBy   who saved this version (set by the processor)
 */
public record ProcessingProfileDTO(
        String code,
        Integer version,
        String name,
        String description,
        List<ProcessingStepDTO> steps,
        Instant createdAt,
        String createdBy) {
}
