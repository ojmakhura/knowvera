package bw.co.knowvera.processing.pipeline;

import java.util.Map;
import java.util.function.Supplier;

import bw.co.knowvera.processing.job.Classification;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.provider.DocumentFile;

/**
 * What a job has produced so far, handed from step to step.
 */
public final class StepContext {

    private final ProcessingJobRequest request;
    private final Supplier<DocumentFile> fileLoader;
    private DocumentFile file;
    private String text;
    private Map<String, Object> fields;
    private Classification classification;

    StepContext(ProcessingJobRequest request, Supplier<DocumentFile> fileLoader) {
        this.request = request;
        this.fileLoader = fileLoader;
        this.text = request.text();
    }

    public ProcessingJobRequest request() {
        return request;
    }

    /** The document, read from storage the first time a step needs it. */
    public DocumentFile file() {
        if (file == null) {
            file = fileLoader.get();
        }
        return file;
    }

    public String text() {
        return text;
    }

    void text(String text) {
        this.text = text;
    }

    public Map<String, Object> fields() {
        return fields;
    }

    void fields(Map<String, Object> fields) {
        this.fields = fields;
    }

    public Classification classification() {
        return classification;
    }

    void classification(Classification classification) {
        this.classification = classification;
    }
}
