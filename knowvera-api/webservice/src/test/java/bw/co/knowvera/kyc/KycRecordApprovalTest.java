package bw.co.knowvera.kyc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import bw.co.knowvera.auth.CurrentCaller;
import bw.co.knowvera.document.DocumentApi;
import bw.co.knowvera.individual.IndividualService;
import bw.co.knowvera.keycloak.KeycloakOrganisationService;
import bw.co.knowvera.keycloak.KeycloakUserService;

/** Only KYC approvers may make a high-risk record current (FI Act s.20(1)(d) and s.22). */
class KycRecordApprovalTest {

    private final KycRecordService service = mock(KycRecordService.class);
    private KycRecordApiImpl api;

    @BeforeEach
    void setUp() throws Exception {
        api = new KycRecordApiImpl(service, mock(KeycloakOrganisationService.class), mock(KeycloakUserService.class),
                mock(IndividualService.class), mock(DocumentApi.class), mock(CurrentCaller.class));

        KycRecordDTO highRisk = new KycRecordDTO();
        highRisk.setRiskLevel(RiskLevel.HIGH);
        when(service.findById("r1")).thenReturn(highRisk);
        when(service.updateStatus(any(), any(), any())).thenReturn(new KycRecordDTO());
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    private static void signIn(String... roles) {
        String[] authorities = java.util.Arrays.stream(roles).map(r -> "ROLE_" + r).toArray(String[]::new);
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("someone", null, authorities));
    }

    @Test
    void aReviewerCannotApproveAHighRiskRecord() throws Exception {
        signIn("KYC_REVIEWER");

        ResponseStatusException denied = assertThrows(ResponseStatusException.class,
                () -> api.updateStatus("r1", KycComplianceStatus.CURRENT));

        assertEquals(HttpStatus.FORBIDDEN, denied.getStatusCode());
        verify(service, never()).updateStatus(any(), any(), any());
    }

    @Test
    void anApproverCanApproveAHighRiskRecord() throws Exception {
        signIn("KYC_APPROVER");

        api.updateStatus("r1", KycComplianceStatus.CURRENT);

        verify(service).updateStatus("r1", KycComplianceStatus.CURRENT, "someone");
    }

    @Test
    void aReviewerCanStillRejectAHighRiskRecord() throws Exception {
        signIn("KYC_REVIEWER");

        api.updateStatus("r1", KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED);

        verify(service).updateStatus("r1", KycComplianceStatus.DOCUMENT_VERIFICATION_FAILED, "someone");
    }
}
