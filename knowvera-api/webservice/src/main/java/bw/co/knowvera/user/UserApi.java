package bw.co.knowvera.user;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.user.UserApi
 */
@RequestMapping("/users")
public interface UserApi {

    @PostMapping(
        value = "add-client-roles"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:manage')")
    public ResponseEntity<UserDTO> addClientRoles(@RequestParam(name = "clientId", required = true) String clientId, @RequestParam(name = "roles", required = false) @Nullable Set<String> roles, @RequestParam(name = "userId", required = true) String userId) throws Exception;

    @PutMapping(
        value = "/role"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:manage', 'SCOPE_users:manage-own')")
    public ResponseEntity<Boolean> addRole(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "role", required = true) String role) throws Exception;

    @PostMapping(
        value = "updatePassword"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:manage')")
    public ResponseEntity<String> changePassword(@RequestParam(name = "userId", required = true) String userId, @RequestParam(name = "newPassword", required = true) String newPassword) throws Exception;

    @GetMapping(
        value = "/branch-id/{branchId}/branchId/{branchId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:list', 'SCOPE_users:list-own')")
    public @Nullable ResponseEntity<List<UserDTO>> findByBranchId(@PathVariable(name = "branchId", required = false) @Nullable String branchId) throws Exception;

    @GetMapping(
        value = "/branch-name/{branch}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> findByBranchName(@PathVariable(name = "branch", required = true) String branch) throws Exception;

    @GetMapping(
        value = "client/roles"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> findByClientRoles(@RequestParam(name = "roles", required = false) @Nullable Set<String> roles, @PathVariable(name = "clientId", required = true) String clientId) throws Exception;

    @GetMapping(
        value = "/by-identity/{identityNo}/identityNo/{identityNo}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:view')")
    public @Nullable ResponseEntity<UserDTO> findByIdentityNo(@PathVariable(name = "identityNo", required = false) @Nullable String identityNo) throws Exception;

    @GetMapping(
        value = "/organisation-id/{organisationId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:list', 'SCOPE_users:list-own')")
    public @Nullable ResponseEntity<List<UserDTO>> findByOrganisationId(@PathVariable(name = "organisationId", required = false) @Nullable String organisationId) throws Exception;

    @GetMapping(
        value = "/department-name/{organisation}"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> findByOrganisationName(@PathVariable(name = "organisation", required = true) String organisation) throws Exception;

    @GetMapping(
        value = "realm/roles"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> findByRealmRoles(@RequestParam(name = "roles", required = false) @Nullable Set<String> roles) throws Exception;

    @GetMapping(
        
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:view', 'SCOPE_users:view-own')")
    public ResponseEntity<UserDTO> findUserById(@RequestParam(name = "userId", required = true) String userId) throws Exception;

    @GetMapping(
        value = "load"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> loadUsers() throws Exception;

    @DeleteMapping(
        value = "/role"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:manage', 'SCOPE_users:manage-own')")
    public ResponseEntity<Boolean> removeRole(@RequestParam(name = "userId", required = false) @Nullable String userId, @RequestParam(name = "role", required = false) @Nullable String role) throws Exception;

    @PostMapping(
        value = "/create"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:edit', 'SCOPE_users:edit-own')")
    public ResponseEntity<UserDTO> saveUser(@RequestBody(required = true) @Valid UserDTO user) throws Exception;

    @GetMapping(
        value = "search"
    )
    @PreAuthorize(value = "hasAuthority('SCOPE_users:list')")
    public @Nullable ResponseEntity<List<UserDTO>> search(@RequestParam(name = "criteria", required = false) @Nullable String criteria) throws Exception;

    @PatchMapping(
        value = "{userId}"
    )
    @PreAuthorize(value = "hasAnyAuthority('SCOPE_users:edit', 'SCOPE_users:edit-own')")
    public ResponseEntity<Boolean> updateUserName(@PathVariable(name = "userId", required = true) String userId, @RequestParam(name = "username", required = true) String username) throws Exception;
}