package bw.co.centralkyc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
// import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient openAiClient(@Qualifier("openAiChatModel") ChatModel model) {
    return ChatClient.builder(model).build();
    }

    @Bean
    public ChatClient geminiClient(
            @Qualifier("vertexAiGeminiChat") ChatModel model) {
        return ChatClient.builder(model).build();
    }
}
