package bw.co.knowvera.auth;

import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import bw.co.knowvera.keycloak.KeycloakUserService;
import bw.co.knowvera.user.UserDTO;

/**
 * Rules for user administration on top of the endpoints' Keycloak permissions
 * (rules generated into {@link RoleAssignmentRules} by keycloak/generate_authz.py):
 * <ul>
 * <li>only application roles may be assigned;</li>
 * <li>privileged roles are assigned, and their holders managed, only by holders of the privileged grantor role;</li>
 * <li>callers with only the "-own" user permissions (organisation administrators) manage users of their own
 * organisation and assign only organisation roles.</li>
 * </ul>
 * Violations throw {@link AuthorizationDeniedException} (403).
 */
@Component
public class UserAdministrationGuard {

    private final KeycloakUserService users;

    public UserAdministrationGuard(@Lazy KeycloakUserService users) {
        this.users = users;
    }

    /** The caller may administer the existing user with {@code permission} (users:manage or users:edit). */
    public void checkCanManageUser(String userId, String permission) {

        if (StringUtils.isBlank(userId)) {
            throw new AuthorizationDeniedException("Access denied: no user given");
        }

        if (!callerIsPrivilegedGrantor()
                && users.findEffectiveRealmRoles(userId).stream().anyMatch(RoleAssignmentRules.PRIVILEGED_ROLES::contains)) {
            throw new AuthorizationDeniedException("Access denied: only " + RoleAssignmentRules.PRIVILEGED_GRANTOR
                    + " may manage this user");
        }

        if (Permissions.has(permission)) {
            return;
        }

        UserDTO target = users.findUserById(userId);
        checkSameOrganisation(target == null ? null : target.getOrganisationId());
    }

    /** The caller may create a user in {@code organisationId} with {@code permission}. */
    public void checkCanCreateUserIn(String organisationId, String permission) {

        if (!Permissions.has(permission)) {
            checkSameOrganisation(organisationId);
        }
    }

    /** The caller may assign (or revoke) {@code roles}; {@code permission} is users:manage. */
    public void checkCanAssign(Collection<String> roles, String permission) {

        boolean platformAdministrator = Permissions.has(permission);

        for (String role : roles) {
            if (!RoleAssignmentRules.ROLES.contains(role)) {
                throw new AuthorizationDeniedException("Access denied: unknown role " + role);
            }
            if (RoleAssignmentRules.PRIVILEGED_ROLES.contains(role) && !callerIsPrivilegedGrantor()) {
                throw new AuthorizationDeniedException("Access denied: only " + RoleAssignmentRules.PRIVILEGED_GRANTOR
                        + " may assign " + role);
            }
            if (!platformAdministrator && !RoleAssignmentRules.ORGANISATION_ROLES.contains(role)) {
                throw new AuthorizationDeniedException("Access denied: organisation administrators may only assign "
                        + RoleAssignmentRules.ORGANISATION_ROLES);
            }
        }
    }

    private void checkSameOrganisation(String organisationId) {

        String callerOrganisation = callerOrganisationId();
        if (StringUtils.isBlank(callerOrganisation) || !callerOrganisation.equals(organisationId)) {
            throw new AuthorizationDeniedException("Access denied: user is not in the caller's organisation");
        }
    }

    private String callerOrganisationId() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)) {
            return null;
        }
        UserDTO caller = users.findUserById(jwt.getSubject());
        return caller == null ? null : caller.getOrganisationId();
    }

    private boolean callerIsPrivilegedGrantor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authority = "ROLE_" + RoleAssignmentRules.PRIVILEGED_GRANTOR;  // realm roles, see KeycloakRoleConverter
        return auth != null && auth.getAuthorities().stream().anyMatch(a -> authority.equals(a.getAuthority()));
    }

}
