package bw.co.knowvera.novu;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/novu")
@Tag(name = "Novu", description = "Endpoints for Novu integration")
public class NovuAuthController {
    
    private final NovuAuthService novuAuthService;

    public NovuAuthController(NovuAuthService novuAuthService) {
        this.novuAuthService = novuAuthService;
    }

    @GetMapping("/config")
    @Operation(summary = "Get Novu Configuration", description = "Retrieve the Novu configuration for the authenticated user")
    public Map<String, String> getNovuConfig(@AuthenticationPrincipal Jwt jwt) {

        if (!novuAuthService.isConfigured()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Notifications are not configured");
        }

        // Subscribers are keyed by email address, as in NovuQueueProcessor
        String subscriberId = jwt == null ? null : jwt.getClaimAsString("email");
        if (StringUtils.isBlank(subscriberId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The access token has no email claim");
        }

        return Map.of(
            "subscriberId", subscriberId,
            "subscriberHash", novuAuthService.generateSubscriberHash(subscriberId)
        );
    }

}
