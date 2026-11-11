package bw.co.roguesystems.ai.provider.lmstudio;

import java.util.LinkedHashMap;
import java.util.List;
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
 * An LM Studio server through its OpenAI compatible chat completions API (veritas.lm-studio.*).
 * Step options: temperature, topP, maxTokens (the context window is set on the server).
 */
@Component
public class LmStudioProvider implements LlmProvider {

    private final RestClient client;
    private final String defaultModel;

    public LmStudioProvider(RestClient.Builder builder, VeritasProperties properties) {
        this.client = LlmServerClient.of(builder, properties.getLmStudio());
        this.defaultModel = properties.getLmStudio().getModel();
    }

    @Override
    public ProviderType type() {
        return ProviderType.LM_STUDIO;
    }

    @Override
    public LlmResponse chat(LlmCall call) {
        String model = call.model() == null ? defaultModel : call.model();
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", model);
        body.put("stream", false);
        body.put("messages", ChatMessages.of(call));
        body.put("temperature", call.temperature());
        if (call.doubleOption("topP") != null) {
            body.put("top_p", call.doubleOption("topP"));
        }
        if (call.intOption("maxTokens") != null) {
            body.put("max_tokens", call.intOption("maxTokens"));
        }
        if (call.responseSchema() != null) {
            body.put("response_format", Map.of("type", "json_schema",
                    "json_schema", Map.of("name", "answer", "strict", true, "schema", call.responseSchema())));
        }

        ChatCompletion response;
        try {
            response = client.post().uri("/v1/chat/completions").contentType(MediaType.APPLICATION_JSON).body(body)
                    .retrieve().body(ChatCompletion.class);
        } catch (RestClientException e) {
            throw LlmServerClient.failure("LM Studio", e);
        }
        if (response == null || response.choices() == null || response.choices().isEmpty()
                || response.choices().getFirst().message() == null
                || response.choices().getFirst().message().content() == null
                || response.choices().getFirst().message().content().isBlank()) {
            throw new ProviderException("LM Studio returned no text");
        }
        if ("length".equals(response.choices().getFirst().finishReason())) {
            throw new ProviderException("LM Studio's answer was cut off at the token limit: raise the step's "
                    + "maxTokens option or the model's context length");
        }
        return new LlmResponse(response.choices().getFirst().message().content(),
                response.model() == null ? model : response.model());
    }

    record ChatCompletion(String model, List<Choice> choices) {
    }

    record Choice(ChatMessage message, @JsonProperty("finish_reason") String finishReason) {
    }

    record ChatMessage(String role, String content) {
    }
}
