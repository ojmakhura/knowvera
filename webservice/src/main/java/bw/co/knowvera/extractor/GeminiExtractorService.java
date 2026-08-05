package bw.co.knowvera.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import bw.co.knowvera.gemini.GeminiProperties;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Service
public class GeminiExtractorService {

    private static final String EXTRACTION_PROMPT = "Extract all readable text from this document. Return only plain text.";

    private final GeminiProperties properties;
    // private final RestClient.Builder restClientBuilder;
    private final RestClient restClient;
    private final JsonMapper jsonMapper;

    public GeminiExtractorService(GeminiProperties properties, RestClient restClient, JsonMapper jsonMapper) {
        this.properties = properties;
        this.restClient = restClient;
        this.jsonMapper = jsonMapper;
    }

    @Value("${app.gemini.base-url}")
    private String geminiBaseUrl;

    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Value("${app.gemini.model}")
    private String geminiModel;

    private GoogleCredentials scopedCredentials;
    private String resolvedProjectId;

    public String extractTextFromPdf(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF payload is empty");
        }

        if (StringUtils.isBlank(geminiApiKey)) {
            throw new IllegalStateException("GEMINI_API_KEY is not configured");
        }

        String projectId = getResolvedProjectId();
        if (projectId.isBlank()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "google.gemini.project-id is required, or credentials JSON must include project_id"
            );
        }

        String encodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        Map<String, Object> requestBody = buildRequestBody(encodedPdf);

        String token = getAccessToken();
        String responseBody;
        try {
            responseBody = restClient.post()
                .uri(buildEndpoint(projectId))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);
        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Gemini API call failed: HTTP " + e.getStatusCode().value() + " - " + e.getResponseBodyAsString(),
                e
            );
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini API call failed", e);
        }

        if (StringUtils.isBlank(responseBody)) {
            throw new IllegalStateException("Gemini returned an empty response");
        }

        String extractedText = parseExtractedText(responseBody);
        if (StringUtils.isBlank(extractedText)) {
            throw new IllegalStateException("Gemini response did not contain extracted text");
        }

        return extractedText;
    }

    private synchronized GoogleCredentials getScopedCredentials() {
        if (scopedCredentials != null) {
            return scopedCredentials;
        }

        if (properties.getCredentialsPath() == null || properties.getCredentialsPath().isBlank()) {
            throw new IllegalStateException("google.gemini.credentials-path is required");
        }

        try (var input = Files.newInputStream(Path.of(properties.getCredentialsPath()))) {
            scopedCredentials = GoogleCredentials.fromStream(input)
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
            return scopedCredentials;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read google.gemini.credentials-path", e);
        }
    }

    private synchronized String getResolvedProjectId() {
        if (resolvedProjectId != null) {
            return resolvedProjectId;
        }

        if (properties.getProjectId() != null && !properties.getProjectId().isBlank()) {
            resolvedProjectId = properties.getProjectId();
            return resolvedProjectId;
        }

        GoogleCredentials creds = getScopedCredentials();
        if (creds instanceof ServiceAccountCredentials serviceAccountCredentials) {
            resolvedProjectId = serviceAccountCredentials.getProjectId();
        } else {
            resolvedProjectId = "";
        }

        return resolvedProjectId == null ? "" : resolvedProjectId;
    }

    private String getAccessToken() {
        GoogleCredentials creds = getScopedCredentials();
        try {
            creds.refreshIfExpired();
            if (creds.getAccessToken() != null) {
                return creds.getAccessToken().getTokenValue();
            }
            return creds.refreshAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to refresh Google access token", e);
        }
    }

    private String buildEndpoint(String projectId) {
        return "https://" + properties.getLocation() + "-aiplatform.googleapis.com/v1/projects/"
            + projectId + "/locations/" + properties.getLocation() + "/publishers/google/models/"
            + properties.getModel() + ":generateContent";
    }

    private Map<String, Object> buildRequestBody(String encodedPdf) {
        return Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", EXTRACTION_PROMPT),
                                Map.of("inline_data", Map.of(
                                        "mime_type", "application/pdf",
                                        "data", encodedPdf))))),
                "generationConfig", Map.of("temperature", 0));
    }

    private String parseExtractedText(String responseBody) {
        try {
            Map<?, ?> response = jsonMapper.readValue(responseBody, Map.class);
            Object candidatesObj = response.get("candidates");
            if (!(candidatesObj instanceof List<?> candidates)) {
                return "";
            }

            List<String> chunks = new ArrayList<>();
            for (Object candidateObj : candidates) {
                if (!(candidateObj instanceof Map<?, ?> candidate)) {
                    continue;
                }

                Object contentObj = candidate.get("content");
                if (!(contentObj instanceof Map<?, ?> content)) {
                    continue;
                }

                Object partsObj = content.get("parts");
                if (!(partsObj instanceof List<?> parts)) {
                    continue;
                }

                for (Object partObj : parts) {
                    if (!(partObj instanceof Map<?, ?> part)) {
                        continue;
                    }
                    Object textObj = part.get("text");
                    if (textObj != null) {
                        String text = textObj.toString().trim();
                        if (!text.isEmpty()) {
                            chunks.add(text);
                        }
                    }
                }
            }

            return String.join("\n", chunks).trim();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Gemini response", e);
        }
    }
}
