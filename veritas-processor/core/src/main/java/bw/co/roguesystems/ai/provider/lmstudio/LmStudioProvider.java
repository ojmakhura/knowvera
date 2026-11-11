package bw.co.roguesystems.ai.provider.lmstudio;

import java.time.Duration;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.tool.StructuredOutputChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Component;

import com.openai.errors.OpenAIIoException;
import com.openai.errors.OpenAIServiceException;

import bw.co.roguesystems.ai.config.VeritasProperties;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.provider.ChatModelProvider;
import bw.co.roguesystems.ai.provider.LlmCall;

/**
 * An LM Studio server through its OpenAI compatible API (veritas.lm-studio.*). The context window
 * is set on the server; the topK option is not sent. The OpenAI client has one timeout for a whole
 * call (read-timeout): connect-timeout is not used.
 */
@Component
public class LmStudioProvider extends ChatModelProvider {

    private final ChatModel model;
    private final String defaultModel;
    private final Duration readTimeout;

    public LmStudioProvider(VeritasProperties properties) {
        super(ProviderType.LM_STUDIO, "LM Studio");
        VeritasProperties.LlmServer server = properties.getLmStudio();
        this.defaultModel = server.getModel();
        this.readTimeout = server.getReadTimeout();
        this.model = OpenAiChatModel.builder()
                .options(connection(server).model(defaultModel).build())
                .build();
    }

    /** No key (LM Studio asks for none), no retries of the client's own. */
    private static OpenAiChatOptions.Builder connection(VeritasProperties.LlmServer server) {
        String baseUrl = server.getBaseUrl().replaceAll("/+$", "");
        return OpenAiChatOptions.builder()
                .baseUrl(baseUrl.endsWith("/v1") ? baseUrl : baseUrl + "/v1")
                .apiKey("")
                .timeout(server.getReadTimeout())
                .maxRetries(0);
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
        // The time a model may take is a per request setting of the client
        return OpenAiChatOptions.builder().timeout(readTimeout);
    }

    @Override
    protected String cutOffAdvice() {
        return "raise the step's maxTokens option or the model's context length";
    }

    @Override
    protected boolean retryable(Throwable failure) {
        return failure instanceof OpenAIIoException
                || failure instanceof OpenAIServiceException error && error.statusCode() >= 500;
    }
}
