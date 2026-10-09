package bw.co.knowvera;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import bw.co.knowvera.AuditableDTO;

public class AuditTracker {
    
    public static void auditTrail(AuditableDTO auditable, Authentication authentication) {
        if (auditable != null) {
            String username = "anonymousUser";
            if(authentication != null) {

                username = authentication.getName();
            }

            if(StringUtils.isBlank(auditable.getId())) {

                auditable.setCreatedBy(username);
                auditable.setCreatedAt(LocalDateTime.now());
            } else {

                auditable.setModifiedBy(username);
                auditable.setModifiedAt(LocalDateTime.now());
            }

        }
    }

    /**
     * As {@link #auditTrail(AuditableDTO, Authentication)}, and on an update keeps the stored
     * creation details so the caller cannot rewrite who created the record or when.
     */
    public static void auditTrail(AuditableDTO auditable, AuditableDTO stored, Authentication authentication) {

        auditTrail(auditable, authentication);

        if (auditable != null && stored != null) {
            auditable.setCreatedBy(stored.getCreatedBy());
            auditable.setCreatedAt(stored.getCreatedAt());
        }
    }
}
