package bw.co.roguesystems.ai.provider.ollama;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonProperty;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.ChatMessages;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmProvider;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.LlmServerClient;
import bw.co.roguesystems.ai.provider.ProviderException;

/**
 * An Ollama server through its chat API (veritas.ollama.*). Step options: temperature, topP, topK,
 * numCtx (the context window, in tokens), maxTokens (the longest answer).
 */
@Component
public class OllamaProvider implements LlmProvider {

    /** A cautious estimate (English text has about four characters per token). */
    private static final int CHARS_PER_TOKEN = 4;

    private final RestClient client;
    private final String defaultModel;
    private final int defaultNumCtx;

    public OllamaProvider(RestClient.Builder builder, VeritasProperties properties) {
        this.client = LlmServerClient.of(builder, properties.getOllama());
        this.defaultModel = properties.getOllama().getModel();
        this.defaultNumCtx = properties.getOllama().getNumCtx();
    }

    @Override
    public ProviderType type() {
        return ProviderType.OLLAMA;
    }

    @Override
    public LlmResponse chat(LlmCall call) {
        String model = call.model() == null ? defaultModel : call.model();
        int numCtx = call.intOption("numCtx") == null ? defaultNumCtx : call.intOption("numCtx");
        // Ollama drops what does not fit its context without saying so: the answer would then be
        // based on part of the document
        int promptTokens = call.promptLength() / CHARS_PER_TOKEN;
        if (promptTokens > numCtx) {
            throw new ProviderException("the prompt (about " + promptTokens + " tokens) does not fit the context of "
                    + numCtx + " tokens: raise the step's numCtx option");
        }

        Map<String, Object> options = new LinkedHashMap<>();
        options.put("temperature", call.temperature());
        options.put("num_ctx", numCtx);
        if (call.doubleOption("topP") != null) {
            options.put("top_p", call.doubleOption("topP"));
        }
        if (call.intOption("topK") != null) {
            options.put("top_k", call.intOption("topK"));
        }
        if (call.intOption("maxTokens") != null) {
            options.put("num_predict", call.intOption("maxTokens"));
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("stream", false);
        body.put("messages", ChatMessages.of(call));
        body.put("options", options);
        if (call.responseSchema() != null) {
            body.put("format", call.responseSchema());
        }

        ChatResponse response;
        try {
            response = client.post().uri("/api/chat").contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(ChatResponse.class);
        } catch (RestClientException e) {
            throw LlmServerClient.failure("Ollama", e);
        }
        if (response == null || response.message() == null || response.message().content() == null
                || response.message().content().isBlank()) {
            throw new ProviderException("Ollama returned no text");
        }
        if ("length".equals(response.doneReason())) {
            throw new ProviderException("Ollama's answer was cut off at the token limit: raise the step's maxTokens "
                    + "or numCtx option");
        }
        return new LlmResponse(response.message().content(), response.model() == null ? model : response.model());
    }

    record ChatResponse(String model, ChatMessage message, @JsonProperty("done_reason") String doneReason) {
    }

    record ChatMessage(String role, String content) {
    }
}
