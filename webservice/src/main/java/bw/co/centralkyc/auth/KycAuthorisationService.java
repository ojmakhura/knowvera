package bw.co.centralkyc.auth;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.keycloak.KeycloakUserService;
import bw.co.centralkyc.kyc.KycRecordService;
import lombok.RequiredArgsConstructor;

@Service("kycAuthService")
@RequiredArgsConstructor
public class KycAuthorisationService {

    private final KycRecordService kycRecordService;
    private final KeycloakUserService keycloakUserService;

    public boolean canViewRequest(UUID requestId, Authentication auth) {
        // 1. Extract orgId from JWT

        // 2. Load request
        // 3. Verify ownership + status
        return true;
    }

    public Boolean isKycRecordOwner(String kycRecordId) {
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }

        Jwt jwt = (Jwt) auth.getPrincipal();
        String userId = jwt.getSubject(); // usually Keycloak userId (sub)

        return kycRecordService.confirmOwnership(kycRecordId, keycloakUserService.findUserById(userId));
    }
}
