package bw.co.centralkyc.logging;

import java.net.http.HttpRequest;

import org.springframework.stereotype.Service;

import bw.co.centralkyc.audit.AuditLogDTO;
import bw.co.centralkyc.audit.AuditLogService;
import lombok.RequiredArgsConstructor;

@Service("auditService")
@RequiredArgsConstructor
public class AuditService {
    
    private final AuditLogService auditLogService;

    public void logAction(String event, String user, String entityType, HttpRequest request) {
        AuditLogDTO log = new AuditLogDTO();
        log.setEvent(event);
        log.setUser(user);
        log.setEntityType(entityType);
        auditLogService.save(log);
    }

}
