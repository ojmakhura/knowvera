package bw.co.knowvera.novu;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NovuAuthService {
    
    // Pulls from app: novu: api-key: in your application.yml
    @Value("${app.novu.api-key}")
    private String novuApiKey;

    /** Whether a Novu API key is set (NOVU_API_KEY). */
    public boolean isConfigured() {
        return novuApiKey != null && !novuApiKey.isBlank();
    }

    public String generateSubscriberHash(String subscriberId) {
        if (!isConfigured()) {
            throw new IllegalStateException("Novu API key is not configured in application.yml");
        }
        
        try {
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                novuApiKey.getBytes(StandardCharsets.UTF_8), 
                "HmacSHA256"
            );
            hmacSha256.init(secretKeySpec);

            byte[] hashBytes = hmacSha256.doFinal(subscriberId.getBytes(StandardCharsets.UTF_8));

            // Converts the byte array directly to an lower-case Hex string
            return HexFormat.of().formatHex(hashBytes);
            
        } catch (Exception e) {
            throw new RuntimeException("Error computing HMAC signature for Novu", e);
        }
    }
}
