package bw.co.knowvera.veritas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyDocumentType;
import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyPrompt;
import bw.co.knowvera.veritas.LegacyProcessingSettings.LegacyTool;
import bw.co.roguesystems.ai.profile.ProcessingProfileDTO;
import bw.co.roguesystems.ai.profile.ProcessingStepDTO;
import bw.co.roguesystems.ai.profile.ProviderType;
import bw.co.roguesystems.ai.profile.StepType;
import tools.jackson.databind.json.JsonMapper;

class VeritasProfileSeederTest {

    private static LegacyTool tool(String tool, Integer position) {
        return new LegacyTool(tool, position);
    }

    @Test
    void buildsTheDefaultStepsFromTheLegacyToolChains() {
        List<ProcessingStepDTO> steps = VeritasProfileSeeder.defaultSteps(
                List.of(tool("GEMINI", 2), tool("TESSERACT", 1)),
                List.of(tool("LM_STUDIO", 1)),
                List.of(tool("GEMINI", 1), tool("LM_STUDIO", 2)),
                List.of(tool("TESSERACT", 1)), "ollama");

        assertEquals(List.of(StepType.TEXT_EXTRACTION, StepType.TEXT_CLEANUP, StepType.FIELD_EXTRACTION),
                steps.stream().map(ProcessingStepDTO::type).toList(), "a step with no language model is left out");
        assertEquals(List.of(ProviderType.PDF_TEXT, ProviderType.TESSERACT, ProviderType.GEMINI), steps.get(0).providers());
        assertEquals(List.of(ProviderType.OLLAMA), steps.get(1).providers(), "LM_STUDIO meant LLM_ID's server");
        assertEquals(List.of(ProviderType.GEMINI, ProviderType.OLLAMA), steps.get(2).providers());
        assertEquals(List.of(ProviderType.LM_STUDIO), VeritasProfileSeeder.defaultSteps(List.of(),
                List.of(tool("LM_STUDIO", 1)), List.of(), List.of(), "lmstudio").get(1).providers());
    }

    @Test
    void withoutLegacyToolsExtractionStillFallsBackToOcr() {
        List<ProcessingStepDTO> steps = VeritasProfileSeeder.defaultSteps(List.of(), List.of(), List.of(), List.of(),
                "ollama");

        assertEquals(1, steps.size());
        assertEquals(List.of(ProviderType.PDF_TEXT, ProviderType.TESSERACT), steps.get(0).providers());
    }

    @Test
    void convertsTheLegacyFormatPrompts() {
        assertEquals("Types:\n{{candidateTypes}} 100%",
                VeritasProfileSeeder.template("Types:%n%s 100%%", "{{candidateTypes}}"));
        assertEquals("Is it a {{documentType}}?\n{{text}}",
                VeritasProfileSeeder.template("Is it a %s?\n%s", "{{documentType}}", "{{text}}"));
    }

    @Test
    void documentTypesWithTheirOwnPromptsGetTheirOwnProfile() {
        List<ProcessingStepDTO> steps = new ArrayList<>(VeritasProfileSeeder.defaultSteps(List.of(), List.of(),
                List.of(), List.of(), "ollama"));
        steps.add(new ProcessingStepDTO(StepType.CLASSIFICATION, List.of(ProviderType.OLLAMA), null, null, null, Map.of()));
        assertNull(VeritasProfileSeeder.customProfile(
                new LegacyDocumentType("1", "PAYSLIP", "Payslip", List.of(), List.of()), steps));

        ProcessingProfileDTO profile = VeritasProfileSeeder.customProfile(new LegacyDocumentType("2", "NATIONAL_ID",
                "National ID", List.of(new LegacyPrompt("system", "Types: %s"), new LegacyPrompt("user", "Expected %s in %s")),
                List.of()), steps);

        assertNotNull(profile);
        assertEquals("doctype-national-id", profile.code());
        assertEquals("National ID", profile.name());
        ProcessingStepDTO classification = profile.steps().getLast();
        assertEquals("Types: {{candidateTypes}}", classification.systemPrompt());
        assertEquals("Expected {{documentType}} in {{text}}", classification.userPrompt());
    }

    @Test
    void readsTheStoredPrompts() {
        LegacyProcessingSettings legacy = new LegacyProcessingSettings(null, JsonMapper.builder().build());

        assertEquals(List.of(new LegacyPrompt("system", "Be strict"), new LegacyPrompt("user", null)),
                legacy.prompts("[{\"role\": \"system\", \"content\": \"Be strict\", \"images\": null}, {\"role\": \"user\"}]"));
        assertEquals(List.of(), legacy.prompts(null));
        assertEquals(List.of(), legacy.prompts(" "));
    }
}
