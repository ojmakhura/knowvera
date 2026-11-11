package bw.co.roguesystems.ai.provider.ollama;

import java.net.http.HttpClient;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.StructuredOutputChatOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.core.retry.RetryPolicy;
import org.springframework.core.retry.RetryTemplate;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.ChatModelProvider;
import bw.co.roguesystems.ai.provider.LlmCall;
import bw.co.roguesystems.ai.provider.ProviderException;

/**
 * An Ollama server (veritas.ollama.*). Its own step option: numCtx, the context window in tokens.
 */
@Component
public class OllamaProvider extends ChatModelProvider {

    /** A cautious estimate (English text has about four characters per token). */
    private static final int CHARS_PER_TOKEN = 4;

    private final ChatModel model;
    private final String defaultModel;
    private final int defaultNumCtx;

    public OllamaProvider(RestClient.Builder builder, VeritasProperties properties) {
        super(ProviderType.OLLAMA, "Ollama");
        VeritasProperties.LlmServer server = properties.getOllama();
        this.defaultModel = server.getModel();
        this.defaultNumCtx = server.getNumCtx();

        // A model that hangs must not hold a job forever
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(server.getConnectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(server.getReadTimeout());
        this.model = OllamaChatModel.builder()
                .ollamaApi(OllamaApi.builder()
                        .baseUrl(server.getBaseUrl())
                        .restClientBuilder(builder.clone().requestFactory(requestFactory))
                        .build())
                .options(OllamaChatOptions.builder().model(defaultModel).build())
                .retryTemplate(new RetryTemplate(RetryPolicy.withMaxRetries(0)))
                .build();
    }

    @Override
    protected ChatModel model() {
        return model;
    }

    @Override
    protected String defaultModel() {
        return defaultModel;
    }

    @Override
    protected StructuredOutputChatOptions.Builder<?> options(LlmCall call) {
        return OllamaChatOptions.builder().numCtx(numCtx(call));
    }

    /**
     * Ollama drops what does not fit its context without saying so: the answer would then be based
     * on part of the document.
     */
    @Override
    protected void check(LlmCall call) {
        int promptTokens = call.promptLength() / CHARS_PER_TOKEN;
        if (promptTokens > numCtx(call)) {
            throw new ProviderException("the prompt (about " + promptTokens + " tokens) does not fit the context of "
                    + numCtx(call) + " tokens: raise the step's numCtx option");
        }
    }

    @Override
    protected String cutOffAdvice() {
        return "raise the step's maxTokens or numCtx option";
    }

    private int numCtx(LlmCall call) {
        return call.intOption("numCtx") == null ? defaultNumCtx : call.intOption("numCtx");
    }
}
