package bw.co.centralkyc.keycloak;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.admin.client.resource.OrganizationResource;
import org.keycloak.admin.client.resource.OrganizationsResource;
import org.keycloak.representations.idm.OrganizationDomainRepresentation;
import org.keycloak.representations.idm.OrganizationRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import bw.co.centralkyc.GeneralStatus;
import bw.co.centralkyc.PhoneNumber;
import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.kyc.KycComplianceStatus;
import bw.co.centralkyc.organisation.OrganisationDTO;
import bw.co.centralkyc.organisation.OrganisationDomain;
import bw.co.centralkyc.organisation.OrganisationListDTO;
import bw.co.centralkyc.organisation.OrganisationSearchCriteria;
import bw.co.centralkyc.subscription.KycSubscriptionRepository;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Component
@RequiredArgsConstructor
public class KeycloakOrganisationService {

    private final KeycloakService keycloakService;
    private final JsonMapper jsonMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss.SSSSSS");

    private final KycSubscriptionRepository subscriptionRepository;

    // --------------------- Core Conversion Methods --------------------- //

    private OrganizationRepresentation fromOrganisationDTO(OrganisationDTO organisation) {
        if (organisation == null) {
            throw new WebApplicationException("Cannot convert null organisation to representation.");
        }

        OrganizationRepresentation rep = new OrganizationRepresentation();
        rep.setId(organisation.getId());
        rep.setName(organisation.getName());
        rep.setAlias(organisation.getCode());
        rep.setDescription(organisation.getDescription());

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("registrationNo", List.of(organisation.getRegistrationNo()));
        attributes.put("status", List.of(organisation.getStatus().toString()));

        if (organisation.getDomains() != null) {
            for (OrganisationDomain dom : organisation.getDomains()) {
                OrganizationDomainRepresentation od = new OrganizationDomainRepresentation(dom.getName());
                od.setVerified(dom.getVerified());
                rep.addDomain(od);
            }
        }

        if (organisation.getPhysicalAddress() != null)
            attributes.put("physicalAddress", List.of(organisation.getPhysicalAddress()));
        if (organisation.getPostalAddress() != null)
            attributes.put("postalAddress", List.of(organisation.getPostalAddress()));
        if (organisation.getContactEmailAddress() != null)
            attributes.put("contactEmailAddress", List.of(organisation.getContactEmailAddress()));
        if (organisation.getCreatedBy() != null)
            attributes.put("createdBy", List.of(organisation.getCreatedBy()));
        if (organisation.getCreatedAt() != null)
            attributes.put("createdAt", List.of(formatter.format(organisation.getCreatedAt())));
        attributes.put("modifiedBy",
                organisation.getModifiedBy() != null ? List.of(organisation.getModifiedBy()) : List.of());
        attributes.put("modifiedAt",
                organisation.getModifiedAt() != null ? List.of(formatter.format(organisation.getModifiedAt()))
                        : List.of());
        if (CollectionUtils.isNotEmpty(organisation.getDocuments()))
            attributes.put("documents", organisation.getDocuments().stream().map(d -> d.getId()).toList());
        if (CollectionUtils.isNotEmpty(organisation.getClientKycDocuments()))
            attributes.put("clientKycDocuments",
                    organisation.getClientKycDocuments().stream().map(d -> d.getId()).toList());
        if (CollectionUtils.isNotEmpty(organisation.getClientRequestsFiles()))
            attributes.put("clientRequestsFiles",
                    organisation.getClientRequestsFiles().stream().map(d -> d.getId()).toList());
        if (organisation.getIsClient() != null)
            attributes.put("isClient", List.of(organisation.getIsClient().toString()));
        if (CollectionUtils.isNotEmpty(organisation.getPhoneNumbers()))
            attributes.put("phoneNumbers", organisation.getPhoneNumbers().stream().map(PhoneNumber::toString).toList());
        if (organisation.getKycStatus() != null)
            attributes.put("kycStatus", List.of(organisation.getKycStatus().toString()));
        if (organisation.getCountryOfRegistration() != null)
            attributes.put("countryOfRegistration", List.of(organisation.getCountryOfRegistration()));

        rep.setAttributes(attributes);
        return rep;
    }

    private OrganisationDTO toOrganisationDTO(OrganizationRepresentation rep) {
        if (rep == null)
            throw new WebApplicationException("Cannot convert null organisation representation.");

        OrganisationDTO org = new OrganisationDTO();
        org.setId(rep.getId());
        org.setCode(rep.getAlias());
        org.setName(rep.getName());
        org.setDescription(rep.getDescription());

        Map<String, List<String>> attrs = rep.getAttributes();
        if (attrs != null) {
            org.setRegistrationNo(getFirst(attrs, "registrationNo"));
            org.setPhysicalAddress(getFirst(attrs, "physicalAddress"));
            org.setPostalAddress(getFirst(attrs, "postalAddress"));
            org.setStatus(GeneralStatus.valueOf(getFirst(attrs, "status")));
            org.setContactEmailAddress(getFirst(attrs, "contactEmailAddress"));
            org.setIsClient(Boolean.parseBoolean(getFirst(attrs, "isClient")));
            org.setCreatedBy(getFirst(attrs, "createdBy"));
            org.setCreatedAt(parseDate(getFirst(attrs, "createdAt")));
            org.setModifiedBy(getFirst(attrs, "modifiedBy"));
            org.setModifiedAt(parseDate(getFirst(attrs, "modifiedAt")));
            if (attrs.containsKey("phoneNumbers")) {
                org.setPhoneNumbers(attrs.get("phoneNumbers").stream()
                        .map(p -> jsonMapper.readValue(p, PhoneNumber.class)).toList());
            }
            if (attrs.containsKey("kycStatus"))
                org.setKycStatus(KycComplianceStatus.valueOf(getFirst(attrs, "kycStatus")));
            if (attrs.containsKey("countryOfRegistration"))
                org.setCountryOfRegistration(getFirst(attrs, "countryOfRegistration"));
        }

        org.setDomains(rep.getDomains().stream().map(d -> new OrganisationDomain(d.getName(), d.isVerified()))
                .collect(Collectors.toSet()));
        return org;
    }

    private String getFirst(Map<String, List<String>> attrs, String key) {
        return (attrs != null && attrs.containsKey(key) && !attrs.get(key).isEmpty()) ? attrs.get(key).get(0) : null;
    }

    private LocalDateTime parseDate(String value) {
        if (StringUtils.isBlank(value))
            return null;
        return LocalDateTime.from(formatter.parse(value));
    }

    public static String getCreatedId(Response response) {
        URI location = response.getLocation();
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            StatusType statusInfo = response.getStatusInfo();
            response.bufferEntity();
            String body = response.readEntity(String.class);
            throw new WebApplicationException("Create method returned status "
                    + statusInfo.getReasonPhrase() + " (Code: " + statusInfo.getStatusCode()
                    + "); expected status: Created (201). Response body: " + body, response);
        }
        if (location == null)
            return null;
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    // --------------------- CRUD Operations --------------------- //

    public OrganisationDTO createOrganisation(OrganisationDTO organisation) {
        return keycloakService.withOrganizations(orgsResource -> {

            OrganisationDTO existing = findByName(organisation.getRegistrationNo());

            if (existing != null) {
                return existing;
            }

            Response res;
            if (StringUtils.isBlank(organisation.getId())) {
                res = orgsResource.create(fromOrganisationDTO(organisation));
                organisation.setId(getCreatedId(res));
            } else {
                OrganizationResource orgResource = orgsResource.get(organisation.getId());
                res = orgResource.update(fromOrganisationDTO(organisation));
                if (res.getStatus() != HttpStatus.NO_CONTENT.value()) {
                    throw new WebApplicationException("Update failed with status " + res.getStatus());
                }
            }
            return organisation;
        });
    }

    public OrganisationDTO findById(String id) {
        return keycloakService.withOrganization(id, orgResource -> toOrganisationDTO(orgResource.toRepresentation()));
    }

    public OrganisationDTO findByName(String name) {
        return keycloakService.withOrganizations(resource -> {
            List<OrganizationRepresentation> t = resource.search("name:" + name);
            if (t == null || t.size() == 0) {
                return null;
            }
            return toOrganisationDTO(t.get(0));
        });
    }

    public List<OrganisationListDTO> getAll() {

        return keycloakService.withOrganizations(
                orgsResource -> orgsResource.getAll().stream().map(this::toOrganisationListDTO).toList());
    }

    public Page<OrganisationListDTO> getAll(Integer pageNumber, Integer pageSize) {

        return keycloakService.withOrganizations(orgsResource -> {
            // Get the paged list of OrganizationRepresentation
            List<OrganizationRepresentation> reps = orgsResource.list(pageNumber * pageSize, pageSize);

            // Count total organizations
            long count = orgsResource.count("");

            // Map to DTOs
            List<OrganisationListDTO> dtos = reps.stream()
                    .map(this::toOrganisationListDTO)
                    .toList();

            return new PageImpl<>(dtos, PageRequest.of(pageNumber, pageSize), count);
        });
    }

    public Boolean remove(String id) {
        // Use runWithOrganization(String orgId, Consumer<OrganizationResource>)
        return keycloakService.withOrganization(id, orgResource -> {
            Response res = orgResource.delete();
            return res.getStatus() == HttpStatus.OK.value();
        });
    }

    public OrganisationDTO findByRegistrationNo(String registrationNo) {
        if (StringUtils.isBlank(registrationNo)) {
            throw new IllegalArgumentException("Registration number must be provided.");
        }

        return keycloakService.withOrganizations(resource -> {

            List<OrganizationRepresentation> t = resource.searchByAttribute("registrationNo:" + registrationNo);

            if (t == null || t.size() == 0) {
                return null;
            }

            return toOrganisationDTO(t.get(0));
        });
    }

    // --------------------- Search Helpers --------------------- //

    public List<OrganisationListDTO> search(OrganisationSearchCriteria criteria) {
        return keycloakService.withOrganizations(orgsResource -> {
            String search = (criteria != null && (criteria.getId() != null || criteria.getName() != null))
                    ? (criteria.getId() != null ? criteria.getId() : criteria.getName())
                    : null;
            return orgsResource.search(search).stream().map(this::toOrganisationListDTO).toList();
        });
    }

    public Page<OrganisationListDTO> search(SearchObject<OrganisationSearchCriteria> criteria) {
        return keycloakService.withOrganizations(orgsResource -> {
            if (criteria.getCriteria() == null)
                criteria.setCriteria(new OrganisationSearchCriteria());
            String search = criteria.getCriteria().getId() != null
                    ? criteria.getCriteria().getId()
                    : criteria.getCriteria().getName();
            Integer pageNumber = criteria.getPageNumber();
            Integer pageSize = criteria.getPageSize();

            List<OrganizationRepresentation> reps = orgsResource.search(search, false, pageNumber * pageSize, pageSize);
            long count = orgsResource.count(search);

            List<OrganisationListDTO> dtos = reps.stream().map(this::toOrganisationListDTO).toList();
            return new PageImpl<>(dtos, PageRequest.of(pageNumber, pageSize), count);
        });
    }

    public Long countOrganisations() {
        return keycloakService.withOrganizations(orgResources -> {

            return orgResources.count(null);
        });
    }

    // --------------------- DTO Mapping --------------------- //
    private OrganisationListDTO toOrganisationListDTO(OrganizationRepresentation rep) {

        String id = rep.getId();
        String code = rep.getAlias();
        String name = rep.getName();
        String registrationNo = null;
        GeneralStatus status = null;
        String contactEmailAddress = null;
        KycComplianceStatus kycStatus = null;
        Boolean isClient = false;
        String keycloakId = rep.getId();

        Map<String, List<String>> attrs = rep.getAttributes();

        if (attrs != null) {
            registrationNo = getFirst(attrs, "registrationNo");
            if (attrs.containsKey("status"))
                status = GeneralStatus.valueOf(getFirst(attrs, "status"));
            contactEmailAddress = getFirst(attrs, "contactEmailAddress");
            if (attrs.containsKey("kycStatus"))
                kycStatus = KycComplianceStatus.valueOf(getFirst(attrs, "kycStatus"));
            if (attrs.containsKey("isClient"))
                isClient = Boolean.parseBoolean(getFirst(attrs, "isClient"));
        }

        return new OrganisationListDTO(id, code, name, registrationNo, status, contactEmailAddress, kycStatus, isClient,
                keycloakId);
    }
}
