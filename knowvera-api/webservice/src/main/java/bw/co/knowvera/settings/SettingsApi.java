package bw.co.knowvera.settings;

import bw.co.knowvera.TargetEntity;
import jakarta.validation.Valid;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.settings.SettingsApi
 */
@RequestMapping("/settings")
public interface SettingsApi {

    @GetMapping(
        value = "/attach-document-type/{documentTypeId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<SettingsDTO> attachDocumentType(@PathVariable(name = "documentTypeId", required = true) String documentTypeId, @RequestParam(name = "purpose", required = true) DocumentTypePurpose purpose) throws Exception;

    @DeleteMapping(
        value = "/detach-document-type/{documentTypeId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:delete')")
    public ResponseEntity<SettingsDTO> detachDocumentType(@PathVariable(name = "documentTypeId", required = true) String documentTypeId, @RequestParam(name = "purpose", required = true) DocumentTypePurpose purpose) throws Exception;

    @GetMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<SettingsDTO> findById(@PathVariable(name = "id", required = true) String id) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:list')")
    public @Nullable ResponseEntity<List<SettingsDTO>> getAll() throws Exception;

    @GetMapping(
        value = "/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:list')")
    public @Nullable ResponseEntity<Page<SettingsDTO>> getAllPaged(@RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @GetMapping(
        value = "/documents"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<DocumentRequirements> getDocumentRequirements() throws Exception;

    @GetMapping(
        value = "/financials"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<FinancialSettings> getFinancialSettings() throws Exception;

    @GetMapping(
        value = "/operational-metrics"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<OperationalMetrics> getOperationalMetrics() throws Exception;

    @GetMapping(
        value = "/platform-identity"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<PlatformIdentity> getPlatformIdentity() throws Exception;

    @GetMapping(
        value = "/field-groups"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<SettingsFieldGroups> getSettingsFieldGroups() throws Exception;

    @GetMapping(
        value = "/template-mappings"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<TemplateMappings> getTemplateMappings() throws Exception;

    @GetMapping(
        value = "/one"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:view')")
    public ResponseEntity<SettingsDTO> loadSettings() throws Exception;

    @GetMapping(
        value = "/search/paged"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:list')")
    public @Nullable ResponseEntity<Page<SettingsDTO>> pagedSearch(@RequestParam(name = "criteria", required = false) @Nullable String criteria, @RequestParam(name = "pageNumber", required = false) @Nullable Integer pageNumber, @RequestParam(name = "pageSize", required = false) @Nullable Integer pageSize) throws Exception;

    @DeleteMapping(
        value = "/{id}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:delete')")
    public ResponseEntity<Boolean> remove(@PathVariable(name = "id", required = true) String id) throws Exception;

    @DeleteMapping(
        value = "/salary-ranges/{salaryRangeId}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:delete')")
    public ResponseEntity<FinancialSettings> removeSalaryRange(@PathVariable(name = "salaryRangeId", required = false) @Nullable Long salaryRangeId) throws Exception;

    @PostMapping(
        
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<SettingsDTO> save(@RequestBody(required = true) @Valid SettingsDTO setttings) throws Exception;

    @PostMapping(
        value = "/documents"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<DocumentRequirements> saveDocumentRequirements(@RequestBody(required = true) @Valid DocumentRequirements documentRequirements) throws Exception;

    @PostMapping(
        value = "/financials"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<FinancialSettings> saveFinancialSettings(@RequestBody(required = true) @Valid FinancialSettings financialSettings) throws Exception;

    @PostMapping(
        value = "/operational-metrics"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<OperationalMetrics> saveOperationalMetrics(@RequestBody(required = true) @Valid OperationalMetrics operationalMetrics) throws Exception;

    @PostMapping(
        value = "/platform-identity"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<PlatformIdentity> savePlatformIdentity(@RequestBody(required = true) @Valid PlatformIdentity platformIdentity) throws Exception;

    @PostMapping(
        value = "/salary-ranges"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<FinancialSettings> saveSalaryRange(@RequestBody(required = true) @Valid SalaryRangeDTO salaryRange) throws Exception;

    @PostMapping(
        value = "/field-groups"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<SettingsFieldGroups> saveSettingsFieldGroups(@RequestBody(required = true) @Valid SettingsFieldGroups settingsFieldGroups) throws Exception;

    @PostMapping(
        value = "/template-mappings"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:edit')")
    public ResponseEntity<TemplateMappings> saveTemplateMappings(@RequestBody(required = true) @Valid TemplateMappings templateMappings) throws Exception;

    @GetMapping(
        value = "/search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:list')")
    public @Nullable ResponseEntity<List<SettingsDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;

    @PostMapping(
        value = "/upload"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_settings:import')")
    public ResponseEntity<SettingsDTO> uploadTemplate(@RequestBody(required = true) @Valid MultipartFile template, @RequestParam(name = "target", required = true) TargetEntity target) throws Exception;
}