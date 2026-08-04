package bw.co.kyvera;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

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
}
