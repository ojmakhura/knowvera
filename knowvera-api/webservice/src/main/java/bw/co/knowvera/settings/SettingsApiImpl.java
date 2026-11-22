package bw.co.knowvera.settings;

import bw.co.knowvera.AuditTracker;
import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.auth.Permissions;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.individual.IndividualDTO;
import bw.co.knowvera.organisation.OrganisationDTO;
import bw.co.knowvera.organisation.OrganisationService;
import bw.co.knowvera.organisation.client.ClientRequestDTO;
import bw.co.knowvera.organisation.client.ClientRequestService;
import bw.co.knowvera.organisation.client.ClientRequestStatus;
import bw.co.knowvera.document.DocumentApi;
import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Settings", description = "Operations related to application settings.")
public class SettingsApiImpl implements SettingsApi {

    private final static Logger LOGGER = LoggerFactory.getLogger(SettingsApiImpl.class);

    private final DocumentApi documentApi;
    private final SettingsService settingsService;

    private final Logger logger = LoggerFactory.getLogger(SettingsApiImpl.class);
    private final CurrentCaller currentCaller;
    private final ClientRequestService clientRequestService;
    private final OrganisationService organisationService;

    public SettingsApiImpl(SettingsService settingsService, DocumentApi documentApi, CurrentCaller currentCaller,
            @Lazy ClientRequestService clientRequestService, @Lazy OrganisationService organisationService) {

        this.documentApi = documentApi;
        this.settingsService = settingsService;
        this.currentCaller = currentCaller;
        this.clientRequestService = clientRequestService;
        this.organisationService = organisationService;
    }

    @Override
    @Operation(summary = "Find Settings by ID", description = "Find settings by their ID")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<SettingsDTO> findById(String id) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Operation(summary = "Get All Settings", description = "Retrieve all settings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<List<SettingsDTO>> getAll() {

        return ResponseEntity.ok(settingsService.getAll());
    }

    @Override
    @Operation(summary = "Get All Settings Paged", description = "Retrieve all settings with pagination")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<Page<SettingsDTO>> getAllPaged(Integer pageNumber, Integer pageSize) {

        return ResponseEntity.ok(settingsService.getAll(pageNumber, pageSize));
    }

    @Override
    @Operation(summary = "Paged Search Settings", description = "Search settings with pagination")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<Page<SettingsDTO>> pagedSearch(String criteria, Integer pageNumber,
            Integer pageSize) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Operation(summary = "Remove Settings", description = "Remove settings by their ID")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<Boolean> remove(String id) {

        return ResponseEntity.ok(false);
    }

    @Override
    @Operation(summary = "Save Settings", description = "Save settings")
    @Audit(entity = "SETTINGS", logData = true)
    public ResponseEntity<SettingsDTO> save(SettingsDTO setttings) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditTracker.auditTrail(setttings, authentication);

        return ResponseEntity.ok(
                settingsService.save(setttings));
    }

    @Override
    @Operation(summary = "Search Settings", description = "Search settings based on criteria")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<List<SettingsDTO>> search(String criteria) {

        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Operation(summary = "Upload Template", description = "Upload a template for a specific target entity")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<SettingsDTO> uploadTemplate(MultipartFile template, TargetEntity target)
            throws Exception {

        if (target != TargetEntity.INVOICE && target != TargetEntity.QUOTATION) {
            throw new IllegalArgumentException("Invalid target entity for template upload: " + target);
        }

        SettingsDTO settings = settingsService.getAll().stream().findFirst()
                .orElseThrow(() -> new Exception("Settings not found"));

        DocumentDTO doc = documentApi
                .upload(
                        target,
                        settings.getId(),
                        target == TargetEntity.INVOICE ? settings.getInvoiceDocumentType().getId()
                                : settings.getQuotationDocumentType().getId(),
                        "settings",
                        template)
                .getBody();

        if (target == TargetEntity.INVOICE) {

            settings.setInvoiceTemplate(doc);
        } else if (target == TargetEntity.QUOTATION) {

            settings.setQuotationTemplate(doc);
        }

        return this.save(settings);
    }

    @Override
    @Operation(summary = "Attach Document Type", description = "Attach a document type to settings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<SettingsDTO> attachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {

        return ResponseEntity.ok(settingsService.attachDocumentType(documentTypeId, purpose));
    }

    @Override
    @Operation(summary = "Detach Document Type", description = "Detach a document type from settings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<SettingsDTO> detachDocumentType(String documentTypeId, DocumentTypePurpose purpose)
            throws Exception {

        return ResponseEntity.ok(settingsService.detachDocumentType(documentTypeId, purpose));
    }

    @Override
    @Operation(summary = "Load Settings", description = "Load the current application settings")
    public ResponseEntity<SettingsDTO> loadSettings() throws Exception {

        return ResponseEntity.ok(settingsService.loadSettings());
    }

    @Override
    @Operation(summary = "Get Document Requirements", description = "Retrieve document requirements")
    public ResponseEntity<DocumentRequirements> getDocumentRequirements() throws Exception {

        return ResponseEntity.ok(withRequestersDocuments(settingsService.getDocumentRequirements()));
    }

    /**
     * The KYC documents asked of the caller: the platform's, plus those required by each
     * organisation that asked for the caller's KYC (a client request for them as an individual, or
     * for their organisation). Callers who maintain the platform's lists get those lists alone,
     * since what they save back replaces them.
     */
    private DocumentRequirements withRequestersDocuments(DocumentRequirements platform) {

        if (platform == null || Permissions.has("settings:edit")) {
            return platform;
        }

        // A copy: the service's result may be shared (cached)
        DocumentRequirements requirements = new DocumentRequirements(platform);
        try {
            requirements.setIndKycDocuments(withRequested(platform.getIndKycDocuments(), TargetEntity.INDIVIDUAL,
                    currentCaller.individual().map(IndividualDTO::getId).orElse(null)));
            requirements.setOrgKycDocuments(withRequested(platform.getOrgKycDocuments(), TargetEntity.ORGANISATION,
                    currentCaller.organisationId().orElse(null)));
        } catch (Exception e) {
            logger.warn("Could not add the requesting organisations' document requirements", e);
            return platform;
        }
        return requirements;
    }

    private List<DocumentTypeDTO> withRequested(List<DocumentTypeDTO> platform, TargetEntity target, String subjectId) {

        Map<String, DocumentTypeDTO> types = new LinkedHashMap<>();
        if (platform != null) {
            platform.forEach(type -> types.put(type.getId(), type));
        }
        if (StringUtils.isBlank(subjectId)) {
            return new ArrayList<>(types.values());
        }

        for (ClientRequestDTO request : clientRequestService.findByTarget(target, subjectId)) {
            if (request.getStatus() == ClientRequestStatus.REJECTED || StringUtils.isBlank(request.getOrganisationId())) {
                continue;
            }
            OrganisationDTO requester = organisationService.findById(request.getOrganisationId());
            List<DocumentTypeDTO> required = requester == null ? null
                    : target == TargetEntity.INDIVIDUAL ? requester.getIndividualKycDocuments()
                            : requester.getOrganisationKycDocuments();
            if (required != null) {
                required.forEach(type -> types.putIfAbsent(type.getId(), type));
            }
        }
        return new ArrayList<>(types.values());
    }

    @Override
    @Operation(summary = "Get Financial Settings", description = "Retrieve financial settings")
    public ResponseEntity<FinancialSettings> getFinancialSettings() throws Exception {

        return ResponseEntity.ok(settingsService.getFinancialSettings());
    }

    @Override
    @Operation(summary = "Get Operational Metrics", description = "Retrieve operational metrics")
    public ResponseEntity<OperationalMetrics> getOperationalMetrics() throws Exception {

        return ResponseEntity.ok(settingsService.getOperationalMetrics());
    }

    @Override
    @Operation(summary = "Get Platform Identity", description = "Retrieve platform identity settings")
    public ResponseEntity<PlatformIdentity> getPlatformIdentity() throws Exception {

        return ResponseEntity.ok(settingsService.getPlatformIdentity());
    }

    @Override
    @Operation(summary = "Get Settings Field Groups", description = "Retrieve settings field groups")
    public ResponseEntity<SettingsFieldGroups> getSettingsFieldGroups() throws Exception {

        return ResponseEntity.ok(settingsService.getSettingsFieldGroups());
    }

    @Override
    @Operation(summary = "Get Template Mappings", description = "Retrieve template mappings")
    public ResponseEntity<TemplateMappings> getTemplateMappings() throws Exception {

        return ResponseEntity.ok(settingsService.getTemplateMappings());
    }

    @Override
    @Operation(summary = "Save Document Requirements", description = "Save document requirements")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<DocumentRequirements> saveDocumentRequirements(
            @Valid DocumentRequirements documentRequirements) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {

            username = authentication.getName();
        }

        documentRequirements.setUser(username);

        return ResponseEntity.ok(settingsService.saveDocumentRequirements(documentRequirements));
    }

    @Override
    @Operation(summary = "Save Financial Settings", description = "Save financial settings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<FinancialSettings> saveFinancialSettings(@Valid FinancialSettings financialSettings)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {

            username = authentication.getName();
        }

        financialSettings.setUser(username);

        return ResponseEntity.ok(settingsService.saveFinancialSettings(financialSettings));
    }

    @Override
    @Operation(summary = "Save Operational Metrics", description = "Save operational metrics")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<OperationalMetrics> saveOperationalMetrics(@Valid OperationalMetrics operationalMetrics)
            throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {

            username = authentication.getName();
        }

        operationalMetrics.setUser(username);

        return ResponseEntity.ok(settingsService.saveOperationalMetrics(operationalMetrics));
    }

    @Override
    @Operation(summary = "Save Platform Identity", description = "Save platform identity settings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<PlatformIdentity> savePlatformIdentity(@Valid PlatformIdentity platformIdentity)
            throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {

            username = authentication.getName();
        }

        platformIdentity.setUser(username);
        
        return ResponseEntity.ok(settingsService.savePlatformIdentity(platformIdentity));
    }

    @Override
    @Operation(summary = "Save Settings Field Groups", description = "Save settings field groups")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<SettingsFieldGroups> saveSettingsFieldGroups(@Valid SettingsFieldGroups settingsFieldGroups)
            throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {

            username = authentication.getName();
        }

        settingsFieldGroups.setUser(username);
        
        return ResponseEntity.ok(settingsService.saveSettingsFieldGroups(settingsFieldGroups));
    }

    @Override
    @Operation(summary = "Save Template Mappings", description = "Save template mappings")
    @Audit(entity = "SETTINGS", logData = false)
    public ResponseEntity<TemplateMappings> saveTemplateMappings(@Valid TemplateMappings templateMappings)
            throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {
            username = authentication.getName();
        }

        templateMappings.setUser(username);

        return ResponseEntity.ok(settingsService.saveTemplateMappings(templateMappings));
    }

    @Override
    @Operation(summary = "Remove Salary Range", description = "Remove a salary range by its ID")
    @Audit(entity = "SETTINGS", eventLabel = "#salaryRangeId", logData = false)
    public ResponseEntity<FinancialSettings> removeSalaryRange(Long salaryRangeId) throws Exception {

        LOGGER.debug("Removing salary range with ID: {}", salaryRangeId);
        System.out.println("Removing salary range with ID: " + salaryRangeId);
    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {
            username = authentication.getName();
        }

        return ResponseEntity.ok(settingsService.removeSalaryRange(salaryRangeId, username));
    }

    @Override
    @Operation(summary = "Save Salary Range", description = "Save a salary range")
    @Audit(entity = "SETTINGS", eventLabel = "#result?.body?.id", logData = false)
    public ResponseEntity<FinancialSettings> saveSalaryRange(@Valid SalaryRangeDTO salaryRange) throws Exception {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = "anonymousUser";
        if (authentication != null) {
            username = authentication.getName();
        }

        return ResponseEntity.ok(settingsService.saveSalaryRange(salaryRange, username));
    }
}