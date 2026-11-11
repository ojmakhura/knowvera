package bw.co.roguesystems.ai.provider.ollama;

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
import org.springframework.web.client.RestClient;

import com.sun.net.httpserver.HttpServer;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.LlmResponse;
import bw.co.roguesystems.ai.provider.ProviderException;
import tools.jackson.databind.json.JsonMapper;

class OllamaProviderTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();
    private final AtomicReference<String> requestBody = new AtomicReference<>();
    private final VeritasProperties properties = new VeritasProperties();

    private HttpServer server;
    private int status = 200;
    private String answer = "{\"model\": \"gemma4:latest\", \"message\": {\"role\": \"assistant\", \"content\": \"hello\"}, "
            + "\"done_reason\": \"stop\"}";
    private long answerDelayMillis;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/api/chat", exchange -> {
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
        properties.getOllama().setBaseUrl("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private OllamaProvider provider() {
        return new OllamaProvider(RestClient.builder(), properties);
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendsTheStepsOptionsAndTheSchema() {
        Map<String, Object> schema = Map.of("type", "object");

        LlmResponse response = provider().chat(new LlmCall("system", "user", null,
                Map.of("topK", "40", "numCtx", "16384", "maxTokens", "500"), schema));

        assertThat(response.content()).isEqualTo("hello");
        assertThat(response.model()).isEqualTo("gemma4:latest");
        Map<String, Object> body = jsonMapper.readValue(requestBody.get(), Map.class);
        assertThat(body).containsEntry("model", "gemma4").containsEntry("stream", false).containsEntry("format", schema);
        assertThat((Map<String, Object>) body.get("options")).containsEntry("temperature", 0.0)
                .containsEntry("num_ctx", 16384).containsEntry("top_k", 40).containsEntry("num_predict", 500);
        assertThat((List<Object>) body.get("messages")).hasSize(2);
    }

    @Test
    void refusesAPromptThatDoesNotFitTheContext() {
        properties.getOllama().setNumCtx(1024);

        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "x".repeat(5000), null, Map.of())))
                .isInstanceOf(ProviderException.class).hasMessageContaining("does not fit the context");
        assertThat(requestBody.get()).isNull();
    }

    @Test
    void anAnswerCutOffAtTheTokenLimitIsAFailure() {
        answer = answer.replace("\"stop\"", "\"length\"");

        assertThatThrownBy(() -> provider().chat(new LlmCall("system", "user", null, Map.of())))
                .isInstanceOf(ProviderException.class).hasMessageContaining("cut off");
    }

    @Test
    void aModelThatDoesNotAnswerInTimeIsARetryableFailure() {
        properties.getOllama().setReadTimeout(Duration.ofMillis(200));
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
