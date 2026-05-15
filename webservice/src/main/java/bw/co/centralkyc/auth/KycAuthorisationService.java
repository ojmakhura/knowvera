package bw.co.centralkyc.auth;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.TargetEntity;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.individual.IndividualDTO;
import bw.co.centralkyc.individual.IndividualService;
import bw.co.centralkyc.invoice.KycInvoiceDTO;
import bw.co.centralkyc.invoice.KycInvoiceService;
import bw.co.centralkyc.keycloak.KeycloakUserService;
import bw.co.centralkyc.kyc.KycRecordDTO;
import bw.co.centralkyc.kyc.KycRecordService;
import bw.co.centralkyc.organisation.OrganisationDTO;
import bw.co.centralkyc.organisation.OrganisationService;
import bw.co.centralkyc.organisation.client.ClientRequestDTO;
import bw.co.centralkyc.organisation.client.ClientRequestService;
import bw.co.centralkyc.subscription.KycSubscriptionDTO;
import bw.co.centralkyc.subscription.KycSubscriptionService;
import bw.co.centralkyc.user.UserDTO;
import lombok.RequiredArgsConstructor;

@Service("kycAuthService")
@RequiredArgsConstructor
public class KycAuthorisationService {

    private final KycRecordService kycRecordService;
    private final KeycloakUserService keycloakUserService;
    private final IndividualService individualService;
    private final OrganisationService organisationService;
    private final DocumentService documentService;
    private final ClientRequestService clientRequestService;
    private final KycInvoiceService invoiceService;
    private final KycSubscriptionService subscriptionService;

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
