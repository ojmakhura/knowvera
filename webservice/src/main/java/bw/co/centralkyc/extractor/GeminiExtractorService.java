package bw.co.centralkyc.extractor;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.json.JsonMapper;

@Service
@RequiredArgsConstructor
public class GeminiExtractorService {

    private static final String EXTRACTION_PROMPT = "Extract all readable text from this document. Return only plain text.";

    private final RestClient.Builder restClientBuilder;
    private final JsonMapper jsonMapper;

    @Value("${app.gemini.base-url}")
    private String geminiBaseUrl;

    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Value("${app.gemini.model}")
    private String geminiModel;

    public String extractTextFromPdf(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new IllegalArgumentException("PDF payload is empty");
        }

        if (StringUtils.isBlank(geminiApiKey)) {
            throw new IllegalStateException("GEMINI_API_KEY is not configured");
        }

        String encodedPdf = Base64.getEncoder().encodeToString(pdfBytes);
        Map<String, Object> requestBody = buildRequestBody(encodedPdf);

        String responseBody = restClientBuilder
                .baseUrl(geminiBaseUrl)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/{model}:generateContent")
                        .queryParam("key", geminiApiKey)
                        .build(geminiModel))
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        if (StringUtils.isBlank(responseBody)) {
            throw new IllegalStateException("Gemini returned an empty response");
        }

        String extractedText = parseExtractedText(responseBody);
        if (StringUtils.isBlank(extractedText)) {
            throw new IllegalStateException("Gemini response did not contain extracted text");
        }

        return extractedText;
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
