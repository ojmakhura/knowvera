package bw.co.roguesystems.ai.provider.gemini;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The Google GenAI client looks up Google Cloud credentials when it is created. Creating it (and
 * the beans built on it) on first use lets the processor start without credentials; only the
 * Gemini steps then fail, and fall back to their next provider.
 */
@Configuration
public class GeminiLazyInitConfig {

    private static final String[] GOOGLE_GENAI_BEANS = {
            "googleGenAiClient", "googleGenAiChatModel", "googleGenAiCachedContentService" };

    @Bean
    public static BeanFactoryPostProcessor lazyGoogleGenAiBeans() {
        return beanFactory -> {
            for (String name : GOOGLE_GENAI_BEANS) {
                if (beanFactory.containsBeanDefinition(name)) {
                    beanFactory.getBeanDefinition(name).setLazyInit(true);
                }
            }
        };
    }
}
