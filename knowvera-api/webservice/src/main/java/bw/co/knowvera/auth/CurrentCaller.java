package bw.co.knowvera.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

import bw.co.knowvera.PropertySearchOrder;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.individual.IndividualServiceException;
import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.kyc.KycRecordListDTO;
import bw.co.knowvera.kyc.KycRecordSearchCriteria;
import bw.co.knowvera.kyc.KycRecordService;
import bw.co.knowvera.user.UserDTO;

/**
 * The signed-in caller of a "my ..." endpoint and the records they own. A caller without an
 * individual profile or organisation simply owns nothing: these methods return empty values
 * instead of throwing, and callers must never treat "owns nothing" as "no filter".
 */
@Component
public class CurrentCaller {

    private static final Logger logger = LoggerFactory.getLogger(CurrentCaller.class);

    /** A record the caller owns: its type and id. */
    public record Owned(TargetEntity target, String id) {
    }

    private final KeycloakUserService users;
    private final IndividualService individuals;
    private final KycRecordService kycRecords;

    public CurrentCaller(@Lazy KeycloakUserService users, @Lazy IndividualService individuals,
            @Lazy KycRecordService kycRecords) {
        this.users = users;
        this.individuals = individuals;
        this.kycRecords = kycRecords;
    }

    /** The caller's Keycloak user id (token subject). */
    public String userId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not signed in");
        }
        return jwt.getSubject();
    }

    /**
     * Remembers a value for the rest of the current request: an endpoint resolves the caller's
     * profile, organisation and records several times. Outside a request it just computes.
     */
    @SuppressWarnings("unchecked")
    private <T> T perRequest(String key, Supplier<T> compute) {

        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return compute.get();
        }
        String name = CurrentCaller.class.getName() + "." + key;
        Object value = attributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        if (value == null) {
            value = compute.get();
            attributes.setAttribute(name, value, RequestAttributes.SCOPE_REQUEST);
        }
        return (T) value;
    }

    /** The caller's individual profile, if they have one. */
    public Optional<IndividualDTO> individual() {
        return perRequest("individual", this::loadIndividual);
    }

    private Optional<IndividualDTO> loadIndividual() {

        try {
            IndividualDTO individual = individuals.findByUserId(userId());
            return individual == null || StringUtils.isBlank(individual.getId()) ? Optional.empty() : Optional.of(individual);
        } catch (IndividualServiceException e) {
            logger.debug("No individual profile for user {}: {}", userId(), e.getMessage());
            return Optional.empty();
        }
    }

    /** The caller's organisation: from their Keycloak account, else from their individual profile. */
    public Optional<String> organisationId() {
        return perRequest("organisationId", this::loadOrganisationId);
    }

    private Optional<String> loadOrganisationId() {

        UserDTO user = users.findUserById(userId());
        if (user != null && StringUtils.isNotBlank(user.getOrganisationId())) {
            return Optional.of(user.getOrganisationId());
        }
        return individual()
                .map(IndividualDTO::getOrganisation)
                .filter(org -> StringUtils.isNotBlank(org.id()))
                .map(org -> org.id());
    }

    /** Ids of the subjects (individual, organisation) whose KYC records the caller may see as theirs. */
    public List<String> subjectIds() {

        List<String> ids = new ArrayList<>();
        individual().ifPresent(individual -> ids.add(individual.getId()));
        organisationId().filter(id -> !ids.contains(id)).ifPresent(ids::add);
        return ids;
    }

    /** The caller's KYC records (empty when they own no subject). */
    public List<KycRecordListDTO> kycRecords() {
        return perRequest("kycRecords", this::loadKycRecords);
    }

    private List<KycRecordListDTO> loadKycRecords() {

        List<String> subjects = subjectIds();
        if (subjects.isEmpty()) {
            return List.of();  // an empty criteria list would match every record
        }
        KycRecordSearchCriteria criteria = new KycRecordSearchCriteria();
        criteria.setTargetIds(subjects);
        try {
            return kycRecords.search(criteria, Set.<PropertySearchOrder>of());
        } catch (Exception e) {
            throw new IllegalStateException("Could not load the caller's KYC records", e);
        }
    }

    /**
     * Records of the given type the caller owns; all supported types when {@code target} is null.
     * Supported: INDIVIDUAL, ORGANISATION, KYC_RECORD.
     */
    public List<Owned> owned(TargetEntity target) {

        List<Owned> owned = new ArrayList<>();
        if (target == null || target == TargetEntity.INDIVIDUAL) {
            individual().ifPresent(individual -> owned.add(new Owned(TargetEntity.INDIVIDUAL, individual.getId())));
        }
        if (target == null || target == TargetEntity.ORGANISATION) {
            organisationId().ifPresent(id -> owned.add(new Owned(TargetEntity.ORGANISATION, id)));
        }
        if (target == null || target == TargetEntity.KYC_RECORD) {
            kycRecords().forEach(record -> owned.add(new Owned(TargetEntity.KYC_RECORD, record.id())));
        }
        if (target != null && target != TargetEntity.INDIVIDUAL && target != TargetEntity.ORGANISATION
                && target != TargetEntity.KYC_RECORD) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported target " + target);
        }
        return owned;
    }
}
