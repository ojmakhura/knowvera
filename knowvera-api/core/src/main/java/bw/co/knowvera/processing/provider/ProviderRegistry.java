package bw.co.knowvera.processing.provider;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import bw.co.knowvera.processing.ProcessingProperties;
import bw.co.knowvera.processing.profile.ProviderType;

/**
 * The providers by type, and whether the deployment allows them.
 */
@Component
public class ProviderRegistry {

    private final Map<ProviderType, TextExtractionProvider> extractors = new EnumMap<>(ProviderType.class);
    private final Map<ProviderType, LlmProvider> llms = new EnumMap<>(ProviderType.class);
    private final ProcessingProperties properties;

    public ProviderRegistry(List<TextExtractionProvider> extractors, List<LlmProvider> llms,
            ProcessingProperties properties) {
        extractors.forEach(provider -> this.extractors.put(provider.type(), provider));
        llms.forEach(provider -> this.llms.put(provider.type(), provider));
        this.properties = properties;
    }

    public Optional<TextExtractionProvider> extractor(ProviderType type) {
        return Optional.ofNullable(extractors.get(type));
    }

    public Optional<LlmProvider> llm(ProviderType type) {
        return Optional.ofNullable(llms.get(type));
    }

    /** Whether the deployment allows the provider (local-only deployments refuse cloud ones). */
    public boolean allowed(ProviderType type) {
        return !(properties.isLocalOnly() && type.isCloud());
    }
}
