package bw.co.knowvera.auth;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authorization.method.AuthorizationInterceptorsOrder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.config.KeycloakPermissionConverter;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.DocumentService;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.invoice.KycInvoiceDTO;
import bw.co.knowvera.invoice.KycInvoiceService;
import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.kyc.KycRecordDTO;
import bw.co.knowvera.kyc.KycRecordService;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.subscription.KycSubscriptionDTO;
import bw.co.knowvera.subscription.KycSubscriptionService;
import bw.co.knowvera.user.UserDTO;
import lombok.RequiredArgsConstructor;

/**
 * Record ownership checks, applied as an aspect to methods annotated with
 * {@link RequiresOwnership}. Runs right after @PreAuthorize (Keycloak permission check):
 * callers holding the type-level permission ({@code SCOPE_<resource>:<scope>}) pass, callers
 * holding only {@code SCOPE_<resource>:<scope>-own} must own the record.
 *
 * Dependencies are injected lazily so that creating this aspect does not instantiate the
 * services before the auto-proxy creator can wrap them (e.g. for @Transactional).
 */
@Aspect
@Component("kycAuthService")
public class KycAuthorisationService implements Ordered {

    /** Directly inside @PreAuthorize, outside @Audit. */
    public static final int ORDER = AuthorizationInterceptorsOrder.PRE_AUTHORIZE.getOrder() + 1;

    /** Suffix of the Keycloak scope that limits a permission to the caller's own records (OWN in generate_authz.py). */
    public static final String OWN_SUFFIX = "-own";

    private final SpelExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer parameterNames = new DefaultParameterNameDiscoverer();

    private final KycRecordService kycRecordService;
    private final KeycloakUserService keycloakUserService;
    private final IndividualService individualService;
    private final OrganisationService organisationService;
    private final DocumentService documentService;
    private final ClientRequestService clientRequestService;
    private final KycInvoiceService invoiceService;
    private final KycSubscriptionService subscriptionService;

    public KycAuthorisationService(@Lazy KycRecordService kycRecordService, @Lazy KeycloakUserService keycloakUserService,
            @Lazy IndividualService individualService, @Lazy OrganisationService organisationService,
            @Lazy DocumentService documentService, @Lazy ClientRequestService clientRequestService,
            @Lazy KycInvoiceService invoiceService, @Lazy KycSubscriptionService subscriptionService) {
        this.kycRecordService = kycRecordService;
        this.keycloakUserService = keycloakUserService;
        this.individualService = individualService;
        this.organisationService = organisationService;
        this.documentService = documentService;
        this.clientRequestService = clientRequestService;
        this.invoiceService = invoiceService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Around("@annotation(requiresOwnership)")
    public Object checkOwnership(ProceedingJoinPoint joinPoint, RequiresOwnership requiresOwnership) throws Throwable {

        String permission = KeycloakPermissionConverter.AUTHORITY_PREFIX + requiresOwnership.scope();
        Set<String> authorities = currentAuthorities();

        if (authorities.contains(permission)) {
            // Type-level permission (staff): not limited to own records
            return joinPoint.proceed();
        }

        if (!authorities.contains(permission + OWN_SUFFIX)) {
            throw new AuthorizationDeniedException("Access denied: missing permission " + requiresOwnership.scope());
        }

        Method method = AopUtils.getMostSpecificMethod(
                ((MethodSignature) joinPoint.getSignature()).getMethod(), joinPoint.getTarget().getClass());
        EvaluationContext context = new MethodBasedEvaluationContext(
                joinPoint.getTarget(), method, joinPoint.getArgs(), parameterNames);

        TargetEntity target = resolveTarget(requiresOwnership.target(), context);
        Object id = parser.parseExpression(requiresOwnership.id()).getValue(context);

        if (target == null || id == null || !isTargetRecordOwner(target, id.toString())) {
            throw new AuthorizationDeniedException("Access denied: caller does not own the "
                    + (target != null ? target : "requested") + " record");
        }

        // An update must also be of a record the caller already owns (the body names the new owner)
        if (StringUtils.isNotBlank(requiresOwnership.recordId())) {
            Object recordId = parser.parseExpression(requiresOwnership.recordId()).getValue(context);
            if (recordId != null && StringUtils.isNotBlank(recordId.toString())
                    && !ownsStoredRecord(resolveTarget(requiresOwnership.record(), context), recordId.toString())) {
                throw new AuthorizationDeniedException("Access denied: caller does not own the stored "
                        + requiresOwnership.record() + " record");
            }
        }

        return joinPoint.proceed();
    }

    private boolean ownsStoredRecord(TargetEntity record, String recordId) {
        try {
            return record != null && isTargetRecordOwner(record, recordId);
        } catch (Exception e) {
            // Unknown id or lookup failure: not proven to be the caller's record
            return false;
        }
    }

    private TargetEntity resolveTarget(String target, EvaluationContext context) {

        if (!target.startsWith("#")) {
            return TargetEntity.valueOf(target);
        }

        Object value = parser.parseExpression(target).getValue(context);
        if (value instanceof TargetEntity entity) {
            return entity;
        }
        return value != null ? TargetEntity.valueOf(value.toString()) : null;
    }

    private Set<String> currentAuthorities() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return Set.of();
        }

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    public boolean canViewRequest(UUID requestId, Authentication auth) {
        // 1. Extract orgId from JWT

        // 2. Load request
        // 3. Verify ownership + status
        return true;
    }

    private UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getSubject(); // usually Keycloak userId (sub)

        return keycloakUserService.findUserById(userId);
    }

    public Boolean isIndividualMatch(String individualId) {

        if(StringUtils.isBlank(individualId)) {

            return false;
        }

        UserDTO user = getCurrentUser();

        if (user == null || StringUtils.isBlank(user.getUserId())) {
            return false;
        }

        IndividualDTO individual = individualService.findByUserId(user.getUserId());

        if (individual == null || StringUtils.isBlank(individual.getId())) {
            return false;
        }

        IndividualDTO individualOwner = individualService.findById(individualId);

        if (individualOwner == null || StringUtils.isBlank(individualOwner.getId())) {
            return false;
        }

        return individualOwner.getId().equals(individual.getId());
    }
    

    public Boolean isOrganisationUserMatch(String organisationId) {

        if(StringUtils.isBlank(organisationId)) {
            return false;
        }

        UserDTO user = getCurrentUser();

        if(user == null || StringUtils.isBlank(user.getUserId()) || StringUtils.isBlank(user.getOrganisationId())) {
            return false;
        }

        OrganisationDTO targetOrg = organisationService.findById(organisationId);

        if(targetOrg == null) {
            return false;
        }

        return user.getOrganisationId().equals(targetOrg.getId());
    }

    public Boolean isOrganisationUserMatchByRegistration(String registrationId) {

        if(StringUtils.isBlank(registrationId)) {

            return false;
        }

        UserDTO user = getCurrentUser();

        if(user == null || StringUtils.isBlank(user.getUserId()) || StringUtils.isBlank(user.getOrganisationId())) {
            return false;
        }

        OrganisationDTO targetOrg = organisationService.findByRegistrationNo(registrationId);

        if(targetOrg == null) {
            return false;
        }

        return user.getOrganisationId().equals(targetOrg.getId());
    }

    private Boolean isKycRecordOwnershipMatch(String kycRecordId) {

        if(StringUtils.isBlank(kycRecordId)) {
            return false;
        }
        KycRecordDTO kycRecord = kycRecordService.findById(kycRecordId);
        if (kycRecord == null || StringUtils.isBlank(kycRecord.getId())) {
            return false;
        }

        Boolean isOwner = switch (kycRecord.getTarget()) {
            case INDIVIDUAL -> isIndividualMatch(kycRecord.getTargetId());
            case ORGANISATION -> isOrganisationUserMatch(kycRecord.getTargetId());
            default -> false;
        };

        return isOwner;
    }

    private Boolean isClientRequestOwnershipMatch(String clientRequestId) {

        if(StringUtils.isBlank(clientRequestId)) {
            return false;
        }

        ClientRequestDTO request = clientRequestService.findById(clientRequestId);

        if(request == null || StringUtils.isBlank(request.getId())) {
            return false;
        }
        
        return isOrganisationUserMatch(request.getOrganisationId());
    }

    private Boolean isInvoiceOwnerMatch(String invoiceId) {

        if(StringUtils.isBlank(invoiceId)) {
            return false;
        }
        
        KycInvoiceDTO invoice = invoiceService.findById(invoiceId);
        if (invoice == null || StringUtils.isBlank(invoice.getId())) {
            return false;
        }

        return isOrganisationUserMatch(invoice.getOrganisationId());
    }

    private Boolean isSubscriptionOwnerMatch(String subscriptionId) {
        if(StringUtils.isBlank(subscriptionId)) {
            return false;
        }

        KycSubscriptionDTO subscription = subscriptionService.findById(subscriptionId);
        if (subscription == null || StringUtils.isBlank(subscription.getId())) {
            return false;
        }

        return isOrganisationUserMatch(subscription.getOrganisationId());
    }

    private Boolean isDocumentOwnershipMatch(String documentId) {
        
        UserDTO user = getCurrentUser();
        if (user == null || StringUtils.isBlank(user.getUserId())) {
            return false;
        }

        DocumentDTO document = documentService.findById(documentId);
        if (document == null || StringUtils.isBlank(document.getId())) {
            return false;
        }
        
        Boolean isOwner = switch (document.getTarget()) {
            case INDIVIDUAL -> isIndividualMatch(document.getTargetId());
            case ORGANISATION -> isOrganisationUserMatch(document.getTargetId());
            case KYC_RECORD -> isKycRecordOwnershipMatch(document.getTargetId());
            case CLIENT_REQUEST -> isClientRequestOwnershipMatch(document.getTargetId());
            case INVOICE -> isInvoiceOwnerMatch(document.getTargetId());
            case SUBSCRIPTION -> isSubscriptionOwnerMatch(document.getTargetId());
            default -> false;
        };

        return isOwner;
    }

    public Boolean isKycRecordOwner(String kycRecordId) {
        
        return isKycRecordOwnershipMatch(kycRecordId);
    }


    public Boolean isDocumentOwner(String documentId) {

        UserDTO user = getCurrentUser();

        if(user == null || StringUtils.isBlank(user.getUserId())) {
            return false;
        }

        DocumentDTO targetDoc = documentService.findById(documentId);

        if (targetDoc == null || StringUtils.isBlank(targetDoc.getId())) {
            return false;
        }

        if(targetDoc.getTarget() == TargetEntity.ORGANISATION) {
            // If the document is linked to an organisation, check if the user belongs to that organisation
            return isOrganisationUserMatch(targetDoc.getTargetId());
        }

        if(targetDoc.getTarget() == TargetEntity.KYC_RECORD) {

            return isKycRecordOwnershipMatch(targetDoc.getTargetId());
        }

        IndividualDTO individual = individualService.findByUserId(user.getUserId());

        if (individual == null || StringUtils.isBlank(individual.getId())) {
            return false;
        }

        // Similar logic to isKycRecordOwner but for documents
        return targetDoc.getTarget() == TargetEntity.INDIVIDUAL
                && targetDoc.getTargetId().equals(individual.getId());
    }

    public Boolean isTargetRecordOwner(TargetEntity target, String targetId) {

        Boolean isOwner = switch (target) {
            case KYC_RECORD -> isKycRecordOwnershipMatch(targetId);
            case INDIVIDUAL -> isIndividualMatch(targetId);
            case ORGANISATION -> isOrganisationUserMatch(targetId);
            case DOCUMENT -> isDocumentOwnershipMatch(targetId);
            case CLIENT_REQUEST -> isClientRequestOwnershipMatch(targetId);
            case INVOICE -> isInvoiceOwnerMatch(targetId);
            case SUBSCRIPTION -> isSubscriptionOwnerMatch(targetId);
            default -> false;
        };

        return isOwner;
    }
}
