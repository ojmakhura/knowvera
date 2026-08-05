package bw.co.knowvera.novu;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/novu")
public class NovuAuthController {
    
    private final NovuAuthService novuAuthService;

    public NovuAuthController(NovuAuthService novuAuthService) {
        this.novuAuthService = novuAuthService;
    }

    @GetMapping("/config")
    public Map<String, String> getNovuConfig(@AuthenticationPrincipal Jwt jwt) {
        // Extracting unique keycloak user identifier ('sub' claim)
        String subscriberId = jwt.getClaimAsString("email");
        // Generate HMAC signature using your application.yml key
        String subscriberHash = novuAuthService.generateSubscriberHash(subscriberId);

        return Map.of(
            "subscriberId", subscriberId,
            "subscriberHash", subscriberHash
        );
    }
    
}
