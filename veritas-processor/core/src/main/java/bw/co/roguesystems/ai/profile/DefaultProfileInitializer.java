package bw.co.roguesystems.ai.profile;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import bw.co.roguesystems.ai.config.VeritasProperties;

/**
 * Creates the "default" profile on first start: the PDF's text layer then Tesseract, and a local
 * language model (Ollama) for the other steps, with the default prompts. Callers adjust it, or
 * replace it, through the profile API.
 */
@Component
public class DefaultProfileInitializer implements ApplicationRunner {

    public static final String DEFAULT_CODE = "default";

    private static final Logger log = LoggerFactory.getLogger(DefaultProfileInitializer.class);

    private final ProcessingProfileService profiles;
    private final VeritasProperties properties;

    public DefaultProfileInitializer(ProcessingProfileService profiles, VeritasProperties properties) {
        this.profiles = profiles;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!properties.getBootstrap().isDefaultProfile() || profiles.exists(DEFAULT_CODE)) {
            return;
        }
        profiles.save(DEFAULT_CODE, defaultProfile(), "system");
        log.info("Created the '{}' processing profile", DEFAULT_CODE);
    }

    public static ProcessingProfileDTO defaultProfile() {
        List<ProviderType> llm = List.of(ProviderType.OLLAMA);
        return new ProcessingProfileDTO(DEFAULT_CODE, null, "Default",
                "Text layer or Tesseract OCR, then a local language model for cleanup, fields and classification",
                List.of(
                        new ProcessingStepDTO(StepType.TEXT_EXTRACTION,
                                List.of(ProviderType.PDF_TEXT, ProviderType.TESSERACT), null, null, null, Map.of()),
                        new ProcessingStepDTO(StepType.TEXT_CLEANUP, llm, null, null, null, Map.of()),
                        new ProcessingStepDTO(StepType.FIELD_EXTRACTION, llm, null, null, null, Map.of()),
                        new ProcessingStepDTO(StepType.CLASSIFICATION, llm, null, null, null, Map.of())),
                null, null);
    }
}
