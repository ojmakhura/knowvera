package bw.co.knowvera.veritas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeService;
import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyDocumentType;
import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyPrompt;
import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyTool;
import bw.co.knowvera.veritas.LegacyProcessingSettings.Stage;
import bw.co.roguesystems.ai.profile.ProcessingProfileDTO;
import bw.co.roguesystems.ai.profile.ProcessingStepDTO;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;

/**
 * One-off migration to veritas-processor (app.veritas.seed-profiles=true): saves the default
 * profile, built from the removed pipeline's tool selectors, and a profile for each document type
 * that had its own prompts (both read by {@link LegacyProcessingSettings}); then links every
 * document type without a profile to its profile. Saving a profile that has not changed keeps its
 * version, so running it again is harmless.
 */
@Component
@ConditionalOnProperty(name = "app.veritas.seed-profiles", havingValue = "true")
public class VeritasProfileSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(VeritasProfileSeeder.class);

    private final VeritasProfilesClient client;
    private final LegacyProcessingSettings legacy;
    private final DocumentTypeService documentTypeService;
    private final String defaultProfile;
    private final String llmId;

    public VeritasProfileSeeder(VeritasProfilesClient client, LegacyProcessingSettings legacy,
            DocumentTypeService documentTypeService,
            @Value("${app.processing.default-profile:default}") String defaultProfile,
            @Value("${app.veritas.legacy-llm-id:ollama}") String llmId) {
        this.client = client;
        this.legacy = legacy;
        this.documentTypeService = documentTypeService;
        this.defaultProfile = defaultProfile;
        this.llmId = llmId;
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            seed();
        } catch (RuntimeException e) {
            log.error("Seeding the veritas-processor profiles failed; the document types were left unchanged", e);
        }
    }

    void seed() {
        List<ProcessingStepDTO> steps = defaultSteps(legacy.tools(Stage.TEXT_EXTRACTION),
                legacy.tools(Stage.TEXT_CLEANUP), legacy.tools(Stage.TEXT_PROCESSING),
                legacy.tools(Stage.DOCUMENT_CONFIRMATION), llmId);
        ProcessingProfileDTO saved = client.save(defaultProfile, new ProcessingProfileDTO(defaultProfile, null,
                "Default", "From the knowvera settings' tool selectors", steps, null, null));
        log.info("veritas-processor profile '{}' is at version {}", defaultProfile, saved.version());

        for (LegacyDocumentType legacyType : legacy.documentTypes()) {
            String code = defaultProfile;
            ProcessingProfileDTO custom = customProfile(legacyType, steps);
            if (custom != null) {
                code = custom.code();
                ProcessingProfileDTO savedCustom = client.save(code, custom);
                log.info("veritas-processor profile '{}' (document type {}) is at version {}", code,
                        legacyType.code(), savedCustom.version());
            }
            DocumentTypeDTO type = documentTypeService.findById(legacyType.id());
            if (type != null && StringUtils.isBlank(type.getProcessingProfileCode())) {
                type.setProcessingProfileCode(code);
                documentTypeService.save(type);
                log.info("Document type {} now uses profile '{}'", type.getCode(), code);
            }
        }
    }

    /** The legacy tool chains as profile steps; steps without a usable tool are left out. */
    static List<ProcessingStepDTO> defaultSteps(List<LegacyTool> extractionTools, List<LegacyTool> cleanupTools,
            List<LegacyTool> processingTools, List<LegacyTool> confirmationTools, String llmId) {
        List<ProcessingStepDTO> steps = new ArrayList<>();
        List<ProviderType> extraction = new ArrayList<>();
        // The legacy pipeline always read the PDF's text layer first and only then used the tool
        extraction.add(ProviderType.PDF_TEXT);
        providers(extractionTools, llmId).stream()
                .filter(provider -> provider == ProviderType.TESSERACT || provider == ProviderType.GEMINI)
                .forEach(extraction::add);
        if (extraction.size() == 1) {
            extraction.add(ProviderType.TESSERACT);
        }
        steps.add(step(StepType.TEXT_EXTRACTION, extraction));
        addLlmStep(steps, StepType.TEXT_CLEANUP, cleanupTools, llmId);
        addLlmStep(steps, StepType.FIELD_EXTRACTION, processingTools, llmId);
        addLlmStep(steps, StepType.CLASSIFICATION, confirmationTools, llmId);
        return steps;
    }

    /** A copy of the default steps with the document type's own prompts, or null when it has none. */
    static @Nullable ProcessingProfileDTO customProfile(LegacyDocumentType type, List<ProcessingStepDTO> defaultSteps) {
        String validationSystem = prompt(type.validationPrompts(), "system");
        String validationUser = prompt(type.validationPrompts(), "user");
        String ocrSystem = prompt(type.textExtractionPrompts(), "system");
        String ocrUser = prompt(type.textExtractionPrompts(), "user");
        boolean customValidation = validationSystem != null && validationUser != null;
        boolean customOcr = ocrSystem != null || ocrUser != null;
        if (!customValidation && !customOcr) {
            return null;
        }
        List<ProcessingStepDTO> steps = new ArrayList<>();
        for (ProcessingStepDTO step : defaultSteps) {
            if (customValidation && step.type() == StepType.CLASSIFICATION) {
                steps.add(new ProcessingStepDTO(step.type(), step.providers(), step.model(),
                        template(validationSystem, "{{candidateTypes}}"),
                        template(validationUser, "{{documentType}}", "{{text}}"), step.options()));
            } else if (customOcr && step.type() == StepType.TEXT_EXTRACTION) {
                steps.add(new ProcessingStepDTO(step.type(), step.providers(), step.model(), ocrSystem, ocrUser,
                        step.options()));
            } else {
                steps.add(step);
            }
        }
        return new ProcessingProfileDTO(profileCode(type), null, StringUtils.defaultIfBlank(type.name(), type.code()),
                "The prompts of document type " + type.code(), steps, null, null);
    }

    static String profileCode(LegacyDocumentType type) {
        String code = StringUtils.defaultIfBlank(type.code(), type.name()).toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-").replaceAll("(^-+|-+$)", "");
        return StringUtils.left("doctype-" + code, 100);
    }

    /**
     * A legacy String.format prompt as a veritas template: each %s becomes the next placeholder
     * (the legacy prompts filled them in this order), %% becomes %.
     */
    static String template(String legacy, String... placeholders) {
        StringBuilder template = new StringBuilder();
        int next = 0;
        for (int i = 0; i < legacy.length(); i++) {
            char c = legacy.charAt(i);
            if (c == '%' && i + 1 < legacy.length()) {
                char spec = legacy.charAt(i + 1);
                if (spec == 's' && next < placeholders.length) {
                    template.append(placeholders[next++]);
                    i++;
                    continue;
                }
                if (spec == '%') {
                    template.append('%');
                    i++;
                    continue;
                }
                if (spec == 'n') {
                    template.append('\n');
                    i++;
                    continue;
                }
            }
            template.append(c);
        }
        return template.toString();
    }

    private static void addLlmStep(List<ProcessingStepDTO> steps, StepType type, List<LegacyTool> tools,
            String llmId) {
        List<ProviderType> providers = providers(tools, llmId).stream().filter(ProviderType::isLlm).distinct().toList();
        if (!providers.isEmpty()) {
            steps.add(step(type, providers));
        }
    }

    /** The tools in order; LM_STUDIO meant the configured local server (LLM_ID: ollama or LM Studio). */
    static List<ProviderType> providers(List<LegacyTool> tools, String llmId) {
        return tools.stream()
                .sorted(Comparator.comparing(LegacyTool::position, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(tool -> provider(tool.tool(), llmId))
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private static @Nullable ProviderType provider(@Nullable String tool, String llmId) {
        if (tool == null) {
            return null;
        }
        return switch (tool) {
            case "TESSERACT" -> ProviderType.TESSERACT;
            case "GEMINI" -> ProviderType.GEMINI;
            case "OLLAMA" -> ProviderType.OLLAMA;
            case "LM_STUDIO" -> "ollama".equalsIgnoreCase(llmId) ? ProviderType.OLLAMA : ProviderType.LM_STUDIO;
            default -> null;
        };
    }

    private static ProcessingStepDTO step(StepType type, List<ProviderType> providers) {
        return new ProcessingStepDTO(type, List.copyOf(providers), null, null, null, new LinkedHashMap<>());
    }

    private static @Nullable String prompt(List<LegacyPrompt> prompts, String role) {
        return prompts.stream()
                .filter(prompt -> role.equals(prompt.role()) && StringUtils.isNotBlank(prompt.content()))
                .map(LegacyPrompt::content)
                .findFirst()
                .orElse(null);
    }

}
