package bw.co.roguesystems.ai.provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.StructuredOutputChatOptions;
import org.springframework.ai.retry.TransientAiException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import bw.co.roguesystems.ai.profile.ProviderType;
import tools.jackson.databind.json.JsonMapper;

/**
 * A language model reached through Spring AI: every model is called, checked and reported the
 * same way. A provider adds the model itself and what only it has (its options, its limits).
 *
 * <p>Spring AI uses a prompt's options as they are, without the model's defaults: the options of
 * a call are always complete. Its own retries are off: a step that failed is tried again by the
 * pipeline (veritas.jobs.attempts).
 */
public abstract class ChatModelProvider implements LlmProvider {

    private static final JsonMapper JSON = JsonMapper.builder().build();

    private final ProviderType type;
    private final String name;

    /**
     * @param name the provider as failures name it ("Ollama")
     */
    protected ChatModelProvider(ProviderType type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public ProviderType type() {
        return type;
    }

    /**
     * @throws ProviderException when the model is not configured
     */
    protected abstract ChatModel model();

    /** The model of a call that names none (null: the one the model was built with). */
    protected abstract String defaultModel();

    /**
     * The provider's own options builder, with what only this provider has. The model, temperature,
     * topP, topK, maxTokens and the answer's schema are set on it for every provider.
     */
    protected abstract StructuredOutputChatOptions.Builder<?> options(LlmCall call);

    /**
     * Refuses a call the model cannot answer properly.
     *
     * @throws ProviderException when it cannot
     */
    protected void check(LlmCall call) {
    }

    /** Whether the model takes the answer's JSON schema (otherwise the prompt alone gives the shape). */
    protected boolean takesSchema() {
        return true;
    }

    /** Whether a failure that is not plainly a network or server one is still worth another try. */
    protected boolean retryable(Throwable failure) {
        return false;
    }

    /** What to change when an answer was cut off. */
    protected String cutOffAdvice() {
        return "raise the step's maxTokens option";
    }

    @Override
    public LlmResponse chat(LlmCall call) {
        List<Message> messages = new ArrayList<>();
        if (call.systemPrompt() != null && !call.systemPrompt().isBlank()) {
            messages.add(new SystemMessage(call.systemPrompt()));
        }
        messages.add(new UserMessage(call.userPrompt()));
        return complete(messages, call);
    }

    /**
     * Sends the messages with the call's model, options and schema.
     *
     * @throws ProviderException when the model fails, answers with nothing or was cut off
     */
    protected LlmResponse complete(List<Message> messages, LlmCall call) {
        check(call);
        ChatModel chatModel = model();
        String requested = call.model() == null ? defaultModel() : call.model();

        StructuredOutputChatOptions.Builder<?> options = options(call);
        options.model(requested);
        options.temperature(call.temperature());
        options.topP(call.doubleOption("topP"));
        options.topK(call.intOption("topK"));
        options.maxTokens(call.intOption("maxTokens"));
        if (call.responseSchema() != null && takesSchema()) {
            options.outputSchema(JSON.writeValueAsString(call.responseSchema()));
        }

        ChatResponse response;
        try {
            response = chatModel.call(new Prompt(messages, options.build()));
        } catch (RuntimeException e) {
            throw new ProviderException(name + " call failed: " + e.getMessage(), e, transientFailure(e));
        }
        Generation result = response == null ? null : response.getResult();
        String text = result == null || result.getOutput() == null ? null : result.getOutput().getText();
        if (text == null || text.isBlank()) {
            throw new ProviderException(name + " returned no text");
        }
        if (cutOff(result.getMetadata() == null ? null : result.getMetadata().getFinishReason())) {
            throw new ProviderException(name + "'s answer was cut off at the token limit: " + cutOffAdvice());
        }
        String used = response.getMetadata() == null ? null : response.getMetadata().getModel();
        return new LlmResponse(text, used == null || used.isBlank() ? requested : used);
    }

    /** The token limit as the servers name it: "length" (Ollama, OpenAI) and MAX_TOKENS (Gemini). */
    private static boolean cutOff(String finishReason) {
        return "length".equalsIgnoreCase(finishReason) || "MAX_TOKENS".equalsIgnoreCase(finishReason);
    }

    /** The server was unreachable, timed out or failed itself: it may answer later. */
    private boolean transientFailure(Throwable failure) {
        for (Throwable cause = failure; cause != null; cause = cause.getCause() == cause ? null : cause.getCause()) {
            if (cause instanceof TransientAiException || cause instanceof ResourceAccessException
                    || cause instanceof HttpServerErrorException || cause instanceof IOException
                    || retryable(cause)) {
                return true;
            }
        }
        return false;
    }
}
