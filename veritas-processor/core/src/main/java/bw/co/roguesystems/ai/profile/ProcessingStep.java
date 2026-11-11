package bw.co.roguesystems.ai.profile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

/**
 * A step of a {@link ProcessingProfile} version. Steps are never changed once saved: a profile
 * change saves a new version with new steps.
 */
@Entity
@Table(name = "veritas_profile_step")
public class ProcessingStep {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "step_type", nullable = false, length = 40)
    private StepType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "veritas_profile_step_provider", joinColumns = @JoinColumn(name = "step_id"))
    @OrderColumn(name = "position")
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false, length = 40)
    private List<ProviderType> providers = new ArrayList<>();

    @Column(name = "model")
    private String model;

    @Column(name = "system_prompt", columnDefinition = "TEXT")
    private String systemPrompt;

    @Column(name = "user_prompt", columnDefinition = "TEXT")
    private String userPrompt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "veritas_profile_step_option", joinColumns = @JoinColumn(name = "step_id"))
    @MapKeyColumn(name = "option_key", length = 100)
    @Column(name = "option_value", length = 1000)
    private Map<String, String> options = new LinkedHashMap<>();

    public UUID getId() {
        return id;
    }

    public StepType getType() {
        return type;
    }

    public void setType(StepType type) {
        this.type = type;
    }

    public List<ProviderType> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderType> providers) {
        this.providers = providers;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String option(String key) {
        return options == null ? null : options.get(key);
    }
}
