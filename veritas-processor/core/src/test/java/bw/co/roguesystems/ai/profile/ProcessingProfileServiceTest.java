package bw.co.roguesystems.ai.profile;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ProcessingProfileServiceTest {

    private static ProcessingProfileDTO profile(ProcessingStepDTO... steps) {
        return new ProcessingProfileDTO(null, null, "Test", null, List.of(steps), null, null);
    }

    private static ProcessingStepDTO step(StepType type, ProviderType... providers) {
        return new ProcessingStepDTO(type, List.of(providers), null, null, null, Map.of());
    }

    @Test
    void acceptsTheDefaultProfile() {
        assertThatCode(() -> ProcessingProfileService.validate("default", DefaultProfileInitializer.defaultProfile()))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsBadCodes() {
        ProcessingProfileDTO profile = profile(step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA));
        assertThatThrownBy(() -> ProcessingProfileService.validate("Has Spaces", profile))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProcessingProfileService.validate("-starts-with-dash", profile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsProvidersThatCannotRunTheStep() {
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.FIELD_EXTRACTION, ProviderType.TESSERACT))))
                .hasMessageContaining("only language model providers");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.TEXT_EXTRACTION, ProviderType.OLLAMA))))
                .hasMessageContaining("cannot read files");
    }

    private static ProcessingStepDTO step(StepType type, ProviderType provider, Map<String, String> options) {
        return new ProcessingStepDTO(type, List.of(provider), null, null, null, options);
    }

    @Test
    void acceptsOptionsTheProvidersCanRead() {
        assertThatCode(() -> ProcessingProfileService.validate("p", profile(
                step(StepType.TEXT_EXTRACTION, ProviderType.TESSERACT, Map.of("ocrDpi", " 200 ", "ocrLanguages", "eng+tsn")),
                step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA,
                        Map.of("temperature", "0", "topP", "0.9", "topK", "40")))))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsOptionsTheProvidersCannotRead() {
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA, Map.of("temperature", "low")))))
                .hasMessageContaining("temperature");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA, Map.of("topP", "1.5")))))
                .hasMessageContaining("topP");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.TEXT_EXTRACTION, ProviderType.TESSERACT, Map.of("ocrDpi", "300dpi")))))
                .hasMessageContaining("ocrDpi");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA, Map.of("temprature", "0")))))
                .hasMessageContaining("unknown");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p",
                profile(step(StepType.FIELD_EXTRACTION, ProviderType.OLLAMA, Map.of("ocrDpi", "300")))))
                .hasMessageContaining("only applies to");
    }

    @Test
    void rejectsStepsOutOfOrderOrRepeated() {
        assertThatThrownBy(() -> ProcessingProfileService.validate("p", profile(
                step(StepType.CLASSIFICATION, ProviderType.OLLAMA), step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA))))
                .hasMessageContaining("order");
        assertThatThrownBy(() -> ProcessingProfileService.validate("p", profile(
                step(StepType.TEXT_CLEANUP, ProviderType.OLLAMA), step(StepType.TEXT_CLEANUP, ProviderType.GEMINI))))
                .hasMessageContaining("twice");
    }
}
