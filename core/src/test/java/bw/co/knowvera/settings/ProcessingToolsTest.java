package bw.co.knowvera.settings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

class ProcessingToolsTest {

    private static ToolSelectorDTO tool(Tool tool) {
        ToolSelectorDTO selector = new ToolSelectorDTO();
        selector.setTool(tool);
        return selector;
    }

    @Test
    void usesTheFirstConfiguredTool() {
        ProcessingTools tools = new ProcessingTools(false);

        assertEquals(Tool.GEMINI, tools.first(List.of(tool(Tool.GEMINI), tool(Tool.LM_STUDIO))));
    }

    @Test
    void skipsCloudToolsWhenProcessingMustStayLocal() {
        ProcessingTools tools = new ProcessingTools(true);

        assertEquals(Tool.LM_STUDIO, tools.first(List.of(tool(Tool.GEMINI), tool(Tool.LM_STUDIO))));
        assertNull(tools.first(List.of(tool(Tool.GEMINI))));
        assertNull(tools.first(null));
    }
}
