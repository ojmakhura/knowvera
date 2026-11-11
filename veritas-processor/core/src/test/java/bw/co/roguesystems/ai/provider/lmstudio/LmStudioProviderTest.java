package bw.co.roguesystems.ai.provider.lmstudio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpServer;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import tools.jackson.databind.json.JsonMapper;

class LmStudioProviderTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private final AtomicReference<String> requestBody = new AtomicReference<>();
    private final VeritasProperties properties = new VeritasProperties();

    private HttpServer server;
    private int status = 200;
    private String answer = "{\"id\": \"chatcmpl-1\", \"object\": \"chat.completion\", \"created\": 1, "
            + "\"model\": \"gemma-4-12b\", \"choices\": [{\"index\": 0, \"finish_reason\": \"stop\", "
            + "\"message\": {\"role\": \"assistant\", \"content\": \"hello\"}}]}";
    private long answerDelayMillis;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/v1/chat/completions", exchange -> {
            requestBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            try {
                Thread.sleep(answerDelayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            byte[] body = answer.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        server.start();
        properties.getLmStudio().setBaseUrl("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private LmStudioProvider provider() {
        return new LmStudioProvider(properties);
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendsTheStepsOptionsAndTheSchema() {
        Map<String, Object> schema = Map.of("type", "object", "properties", Map.of("name", Map.of("type", "string")));

        LlmResponse response = provider().chat(new LlmCall("system", "user", null,
                Map.of("topP", "0.9", "maxTokens", "500"), schema));

        assertThat(response.content()).isEqualTo("hello");
        assertThat(response.model()).isEqualTo("gemma-4-12b");
        Map<String, Object> body = jsonMapper.readValue(requestBody.get(), Map.class);
        assertThat(body).containsEntry("model", "gemma4").containsEntry("temperature", 0.0)
                .containsEntry("top_p", 0.9).containsEntry("max_tokens", 500);
        assertThat((List<Object>) body.get("messages")).hasSize(2);
        Map<String, Object> format = (Map<String, Object>) body.get("response_format");
        assertThat(format).containsEntry("type", "json_schema");
        assertThat((Map<String, Object>) format.get("json_schema")).containsEntry("strict", true)
                .containsEntry("schema", schema);
    }

    @Test
    void anAnswerCutOffAtTheTokenLimitIsAFailure() {
        answer = answer.replace("\"stop\"", "\"length\"");

        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "user", null, Map.of())))
                .isInstanceOf(ProviderException.class).hasMessageContaining("cut off");
    }

    @Test
    void aModelThatDoesNotAnswerInTimeIsARetryableFailure() {
        properties.getLmStudio().setReadTimeout(Duration.ofMillis(300));
        answerDelayMillis = 2000;

        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "user", null, Map.of())))
                .isInstanceOfSatisfying(ProviderException.class, e -> assertThat(e.isRetryable()).isTrue());
    }

    @Test
    void aServerErrorIsRetryableAndARefusedRequestIsNot() {
        status = 503;
        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "user", null, Map.of())))
                .isInstanceOfSatisfying(ProviderException.class, e -> assertThat(e.isRetryable()).isTrue());

        status = 404;
        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "user", null, Map.of())))
                .isInstanceOfSatisfying(ProviderException.class, e -> assertThat(e.isRetryable()).isFalse());
    }
}
