package bw.co.centralkyc.logging;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import bw.co.centralkyc.audit.AuditContext;
import bw.co.centralkyc.audit.AuditLogDTO;
import bw.co.centralkyc.audit.AuditLogService;
import bw.co.centralkyc.contact.ContactDTO;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.type.DocumentTypeDTO;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualDTO;
import bw.co.centralkyc.invoice.KycInvoiceDTO;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.organisation.OrganisationDTO;
import bw.co.centralkyc.organisation.branch.BranchDTO;
import bw.co.centralkyc.settings.SettingsDTO;
import bw.co.centralkyc.settings.kyc.KycFieldGroupDTO;
import bw.co.centralkyc.subscription.KycSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final JsonMapper jsonMapper;
    private final AuditLogService auditLogService;
    private final AuditContextProvider contextProvider;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(audit)")
    public Object audit(
            ProceedingJoinPoint joinPoint,
            Audit audit) throws Throwable {

        Object result = null;

        try {

            result = joinPoint.proceed();

            return result;

        } finally {

            AuditContext ctx = contextProvider.getContext();

            AuditLogDTO log = new AuditLogDTO();

            log.setTimestamp(Instant.now());

            if(StringUtils.isNotBlank(audit.event())) {
                log.setEvent(audit.event());
            } else {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                log.setEvent(signature.getMethod().getName());
            }

            log.setEntityType(audit.entity());

            String eventLabel = resolveEventLabel(
                    joinPoint,
                    audit,
                    result);

            if(StringUtils.isBlank(eventLabel)) {
                eventLabel = extractEventLabel(result);
            }

            log.setEventLabel(eventLabel);

            log.setUsername(
                    ctx.getUsername());

            log.setUserId(ctx.getUserId());

            log.setIpAddress(
                    ctx.getIpAddress());

            log.setAgent(
                    ctx.getUserAgent());

            log.setTraceId(
                    ctx.getTraceId());

            log.setSpanId(
                    ctx.getSpanId());

            if(audit.logData()) {

                try {
                    ResponseEntity res = (ResponseEntity) result;
                    Map<String, Object> data = jsonMapper.convertValue(res.getBody(), Map.class);

                    log.setLogData(data);

                } catch (Exception e) {
                    
                    log.setLogData(Map.of(
                            "error", "Failed to serialize log data",
                            "message", e.getMessage()));
                }
            }

            auditLogService.save(log);
        }
    }

    private String extractEventLabel(Object result) {

        ResponseEntity res = (ResponseEntity) result;

        if(res.getBody() instanceof IndividualDTO) {
            
            IndividualDTO dto = (IndividualDTO) res.getBody();
            return "Individual: " + dto.getId();

        } else if(res.getBody() instanceof KycRecordDTO) {

            KycRecordDTO dto = (KycRecordDTO) res.getBody();

            if(dto.getRef() != null) {
                return "KycRecord: " + dto.getRef();
            } else {

                return "KycRecord: " + dto.getId();
            }

        } else if (res.getBody() instanceof OrganisationDTO) {
            
            OrganisationDTO dto = (OrganisationDTO) res.getBody();
            return "Organisation: " + dto.getId();

        } else if (res.getBody() instanceof DocumentDTO) {
            
            DocumentDTO dto = (DocumentDTO) res.getBody();
            return "Document: " + dto.getId();
        
        } else if (res.getBody() instanceof DocumentTypeDTO) {
            
            DocumentTypeDTO dto = (DocumentTypeDTO) res.getBody();
            return "DocumentType: " + dto.getId();

        } else if (res.getBody() instanceof KycInvoiceDTO) {
            
            KycInvoiceDTO dto = (KycInvoiceDTO) res.getBody();

            if(dto.getRef() != null) {
                return "KycInvoice: " + dto.getRef();
            }

            return "KycInvoice: " + dto.getId();

        } else if (res.getBody() instanceof BranchDTO) {
            
            BranchDTO dto = (BranchDTO) res.getBody();

            return "Branch: " + dto.getId();

        } else if (res.getBody() instanceof ContactDTO) {
            
            ContactDTO dto = (ContactDTO) res.getBody();

            if(dto.getRef() != null) {
                return "Contact: " + dto.getRef();
            }

            return "Contact: " + dto.getId();
        } else if (res.getBody() instanceof KycSubscriptionDTO) {

            KycSubscriptionDTO dto = (KycSubscriptionDTO) res.getBody();

            if(dto.getRef() != null) {
                return "KycSubscription: " + dto.getRef();
            }

            return "KycSubscription: " + dto.getId();

        } else if (res.getBody() instanceof SettingsDTO) {

            SettingsDTO dto = (SettingsDTO) res.getBody();

            return "Settings: " + dto.getId();

        } else if (res.getBody() instanceof KycFieldGroupDTO) {

            KycFieldGroupDTO dto = (KycFieldGroupDTO) res.getBody();

            return "KycFieldGroup: " + dto.getId();

        } else if (res.getBody() instanceof Page) {

            StringBuilder builder = new StringBuilder();
            Page page = (Page) res.getBody();

            if(!page.isEmpty()) {

                Object first = page.getContent().get(0);

                

                if(first instanceof IndividualDTO) {
                    builder.append("IndividualPage: ");
                } else if(first instanceof KycRecordDTO) {
                    
                    builder.append("KycRecordPage: ");

                } else if (first instanceof OrganisationDTO) {

                    builder.append("OrganisationPage: ");

                } else if (first instanceof DocumentDTO) {

                    builder.append("DocumentPage: ");

                } else if (first instanceof DocumentTypeDTO) {

                    builder.append("DocumentTypePage: ");

                } else if (first instanceof KycInvoiceDTO) {

                    builder.append("KycInvoicePage: ");

                } else if (first instanceof BranchDTO) {

                    builder.append("BranchPage: ");

                } else if (first instanceof ContactDTO) {

                    builder.append("ContactPage: ");

                } else if (first instanceof KycSubscriptionDTO) {

                    builder.append("KycSubscriptionPage: ");


                } else if (first instanceof SettingsDTO) {

                    builder.append("SettingsPage: ");
                } else {
                    builder.append("Page: ");
                }

                builder.append(page.getNumber() + 1)
                        .append(" of ")
                        .append(page.getTotalPages())
                        .append(" with ")
                        .append(page.getNumberOfElements())
                        .append(" items ")
                        .append("(total elements: ")
                        .append(page.getTotalElements())
                        .append(")");
            } else {
                builder.append("EmptyPage");
            }

            return builder.toString();
        } else if(res.getBody() instanceof List) {

            List list = (List) res.getBody();

            int size = list.size();

            if(!list.isEmpty()) {

                Object first = list.get(0);

                if(first instanceof IndividualDTO) {

                    return "IndividualList: " + size;

                } else if(first instanceof KycRecordDTO) {

                    return "KycRecordList: " + size;

                } else if (first instanceof OrganisationDTO) {

                    return "OrganisationList: " + size;

                } else if (first instanceof DocumentDTO) {

                    return "DocumentList: " + size;

                } else if (first instanceof DocumentTypeDTO) {

                    return "DocumentTypeList: " + size;

                } else if (first instanceof KycInvoiceDTO) {

                    return "KycInvoiceList: " + size;

                } else if (first instanceof BranchDTO) {

                    return "BranchList: " + size;

                } else if (first instanceof ContactDTO) {

                    return "ContactList: " + size;

                } else if (first instanceof KycSubscriptionDTO) {

                    return "KycSubscriptionList: " + size;

                } else {
                    return "List: " + size;
                }
            } 
        }

        return null;
    }

    private String resolveEventLabel(
            ProceedingJoinPoint joinPoint,
            Audit audit,
            Object result) {

        String expression = audit.eventLabel();

        if (expression == null || expression.isBlank()) {
            return null;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String[] parameterNames = signature.getParameterNames();

        Object[] args = joinPoint.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        context.setVariable("result", result);

        Object value = parser.parseExpression(expression)
                .getValue(context);

        return value != null
                ? value.toString()
                : null;
    }
}
