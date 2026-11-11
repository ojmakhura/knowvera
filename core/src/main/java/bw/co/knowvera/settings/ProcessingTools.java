package bw.co.knowvera.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Chooses the tool for a document processing step from the tools configured in settings, in
 * order. With {@code app.processing.local-only} on, documents never leave the deployment:
 * cloud tools (Gemini) are skipped, keeping identity documents in Botswana (Data Protection
 * Act 2024, ss.74 to 76, transfers outside Botswana).
 */
@Component
public class ProcessingTools {

    private final boolean localOnly;

    public ProcessingTools(@Value("${app.processing.local-only:false}") boolean localOnly) {
        this.localOnly = localOnly;
    }

    public boolean isLocalOnly() {
        return localOnly;
    }

    /** Whether the tool sends documents to a service outside the deployment. */
    public static boolean isCloud(Tool tool) {
        return tool == Tool.GEMINI;
    }

    /** The first configured tool that may be used, or null when there is none. */
    public Tool first(List<ToolSelectorDTO> tools) {

        if (tools == null) {
            return null;
        }

        return tools.stream()
                .map(ToolSelectorDTO::getTool)
                .filter(tool -> tool != null && !(localOnly && isCloud(tool)))
                .findFirst()
                .orElse(null);
    }
}
