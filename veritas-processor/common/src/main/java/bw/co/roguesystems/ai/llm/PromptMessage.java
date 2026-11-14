package bw.co.roguesystems.ai.llm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A message of a prompt: who says it (system or user), what is said, and the images sent with it.
 * The model names this type (types::bw.co.knowvera::llm::PromptMessage) without defining it.
 */
public class PromptMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String role;
    private String content;
    private List<String> images = new ArrayList<>();

    public PromptMessage() {
    }

    public PromptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
