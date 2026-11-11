package bw.co.roguesystems.ai.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Saves and reads processing profiles. Saving a profile adds its next version; versions are never
 * changed, so a job's result always names the exact steps and prompts that produced it.
 */
@Service
@Transactional(readOnly = true)
public class ProcessingProfileService {

    /** The options a step can carry (see the providers), and those only text extraction reads. */
    private static final List<String> OPTIONS = List.of("temperature", "topP", "topK", "numCtx", "maxTokens",
            "structuredOutput", "ocrLanguages", "ocrDpi");
    private static final List<String> OCR_OPTIONS = List.of("ocrLanguages", "ocrDpi");

    private final ProcessingProfileRepository repository;

    public ProcessingProfileService(ProcessingProfileRepository repository) {
        this.repository = repository;
    }

    public ProcessingProfile latest(String code) {
        return repository.findFirstByCodeOrderByVersionDesc(code).orElseThrow(() -> new ProfileNotFoundException(code));
    }

    public ProcessingProfileDTO findLatest(String code) {
        return toDTO(latest(code));
    }

    public ProcessingProfileDTO findVersion(String code, int version) {
        return toDTO(repository.findByCodeAndVersion(code, version)
                .orElseThrow(() -> new ProfileNotFoundException(code, version)));
    }

    public List<ProcessingProfileDTO> versions(String code) {
        List<ProcessingProfile> versions = repository.findByCodeOrderByVersionDesc(code);
        if (versions.isEmpty()) {
            throw new ProfileNotFoundException(code);
        }
        return versions.stream().map(ProcessingProfileService::toDTO).toList();
    }

    public List<ProfileSummary> summaries() {
        return repository.findLatestVersions().stream()
                .map(p -> new ProfileSummary(p.getCode(), p.getVersion(), p.getName(), p.getDescription(),
                        p.getSteps().stream().map(ProcessingStep::getType).toList()))
                .toList();
    }

    public boolean exists(String code) {
        return repository.findFirstByCodeOrderByVersionDesc(code).isPresent();
    }

    /**
     * Saves {@code profile} as the next version of {@code code}. Saving what the latest version
     * already holds changes nothing and returns that version, so seeding can run repeatedly.
     */
    @Transactional
    public ProcessingProfileDTO save(String code, ProcessingProfileDTO profile, String user) {
        validate(code, profile);
        ProcessingProfile latest = repository.findFirstByCodeOrderByVersionDesc(code).orElse(null);
        if (latest != null && sameContent(toDTO(latest), profile)) {
            return toDTO(latest);
        }

        ProcessingProfile entity = new ProcessingProfile();
        entity.setCode(code);
        entity.setVersion(latest == null ? 1 : latest.getVersion() + 1);
        entity.setName(profile.name());
        entity.setDescription(profile.description());
        entity.setCreatedBy(user);
        for (ProcessingStepDTO step : profile.steps()) {
            ProcessingStep stepEntity = new ProcessingStep();
            stepEntity.setType(step.type());
            stepEntity.setProviders(new ArrayList<>(step.providers()));
            stepEntity.setModel(StringUtils.trimToNull(step.model()));
            stepEntity.setSystemPrompt(StringUtils.trimToNull(step.systemPrompt()));
            stepEntity.setUserPrompt(StringUtils.trimToNull(step.userPrompt()));
            stepEntity.setOptions(step.options() == null ? new LinkedHashMap<>() : new LinkedHashMap<>(step.options()));
            entity.getSteps().add(stepEntity);
        }
        return toDTO(repository.save(entity));
    }

    static void validate(String code, ProcessingProfileDTO profile) {
        if (StringUtils.isBlank(code) || !code.matches("[a-z0-9][a-z0-9-]{0,99}")) {
            throw new IllegalArgumentException(
                    "A profile code is 1-100 lower case letters, digits and dashes, starting with a letter or digit");
        }
        if (profile == null || StringUtils.isBlank(profile.name())) {
            throw new IllegalArgumentException("A profile needs a name");
        }
        if (profile.steps() == null || profile.steps().isEmpty()) {
            throw new IllegalArgumentException("A profile needs at least one step");
        }
        List<StepType> seen = new ArrayList<>();
        for (ProcessingStepDTO step : profile.steps()) {
            if (step.type() == null) {
                throw new IllegalArgumentException("Every step needs a type");
            }
            if (seen.contains(step.type())) {
                throw new IllegalArgumentException("Step " + step.type() + " appears twice");
            }
            if (!seen.isEmpty() && step.type().ordinal() < seen.getLast().ordinal()) {
                throw new IllegalArgumentException("Steps run in the order " + List.of(StepType.values()));
            }
            seen.add(step.type());
            if (step.providers() == null || step.providers().isEmpty()) {
                throw new IllegalArgumentException("Step " + step.type() + " needs at least one provider");
            }
            for (ProviderType provider : step.providers()) {
                if (step.type() != StepType.TEXT_EXTRACTION && !provider.isLlm()) {
                    throw new IllegalArgumentException(provider + " cannot run " + step.type()
                            + ": only language model providers can");
                }
                if (step.type() == StepType.TEXT_EXTRACTION && provider.isLlm() && provider != ProviderType.GEMINI) {
                    throw new IllegalArgumentException(provider + " cannot read files: use PDF_TEXT, TESSERACT or GEMINI");
                }
            }
            validateOptions(step);
        }
    }

    /**
     * A step's options are checked when the profile is saved: a value a provider cannot read would
     * otherwise only show as a failed step on every job.
     */
    private static void validateOptions(ProcessingStepDTO step) {
        if (step.options() == null) {
            return;
        }
        step.options().forEach((key, value) -> {
            String where = "Step " + step.type() + " option " + key;
            if (key == null || !OPTIONS.contains(key)) {
                throw new IllegalArgumentException(where + " is unknown: use one of " + OPTIONS);
            }
            if (OCR_OPTIONS.contains(key) && step.type() != StepType.TEXT_EXTRACTION) {
                throw new IllegalArgumentException(where + " only applies to " + StepType.TEXT_EXTRACTION);
            }
            if (StringUtils.isBlank(value)) {
                return;
            }
            String text = value.trim();
            switch (key) {
                case "temperature" -> number(where, text, 0, 2);
                case "topP" -> number(where, text, 0, 1);
                case "topK" -> integer(where, text, 1, 1000);
                case "numCtx" -> integer(where, text, 512, 2_000_000);
                case "maxTokens" -> integer(where, text, 1, 1_000_000);
                case "structuredOutput" -> {
                    if (!text.equalsIgnoreCase("true") && !text.equalsIgnoreCase("false")) {
                        throw new IllegalArgumentException(where + " is true or false, not '" + value + "'");
                    }
                }
                case "ocrDpi" -> integer(where, text, 72, 600);
                case "ocrLanguages" -> {
                    if (!text.matches("[A-Za-z_]+(\\+[A-Za-z_]+)*")) {
                        throw new IllegalArgumentException(where + " lists Tesseract languages, e.g. eng or eng+tsn");
                    }
                }
                default -> throw new IllegalStateException("Unchecked option " + key);
            }
        });
    }

    private static void number(String where, String value, double min, double max) {
        double number;
        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            number = Double.NaN;
        }
        if (!(number >= min && number <= max)) {
            throw new IllegalArgumentException(where + " is a number from " + min + " to " + max + ", not '" + value + "'");
        }
    }

    private static void integer(String where, String value, int min, int max) {
        int number;
        try {
            number = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            number = min - 1;
        }
        if (number < min || number > max) {
            throw new IllegalArgumentException(
                    where + " is a whole number from " + min + " to " + max + ", not '" + value + "'");
        }
    }

    private static boolean sameContent(ProcessingProfileDTO saved, ProcessingProfileDTO candidate) {
        if (!Objects.equals(saved.name(), candidate.name())
                || !Objects.equals(StringUtils.trimToNull(saved.description()), StringUtils.trimToNull(candidate.description()))
                || saved.steps().size() != candidate.steps().size()) {
            return false;
        }
        for (int i = 0; i < saved.steps().size(); i++) {
            ProcessingStepDTO a = saved.steps().get(i);
            ProcessingStepDTO b = candidate.steps().get(i);
            if (a.type() != b.type() || !a.providers().equals(b.providers())
                    || !Objects.equals(a.model(), StringUtils.trimToNull(b.model()))
                    || !Objects.equals(a.systemPrompt(), StringUtils.trimToNull(b.systemPrompt()))
                    || !Objects.equals(a.userPrompt(), StringUtils.trimToNull(b.userPrompt()))
                    || !Objects.equals(a.options(), b.options() == null ? new LinkedHashMap<>() : b.options())) {
                return false;
            }
        }
        return true;
    }

    static ProcessingProfileDTO toDTO(ProcessingProfile entity) {
        return new ProcessingProfileDTO(entity.getCode(), entity.getVersion(), entity.getName(), entity.getDescription(),
                entity.getSteps().stream()
                        .map(s -> new ProcessingStepDTO(s.getType(), List.copyOf(s.getProviders()), s.getModel(),
                                s.getSystemPrompt(), s.getUserPrompt(), new LinkedHashMap<>(s.getOptions())))
                        .toList(),
                entity.getCreatedAt(), entity.getCreatedBy());
    }
}
