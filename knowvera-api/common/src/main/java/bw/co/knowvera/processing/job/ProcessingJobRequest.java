package bw.co.knowvera.processing.job;

import java.util.List;
import java.util.Map;

import bw.co.knowvera.processing.profile.StepType;
import bw.co.knowvera.processing.schema.DocumentRef;
import bw.co.knowvera.processing.schema.DocumentTypeSpec;
import bw.co.knowvera.processing.schema.FieldSpec;

/**
 * A document to process with a profile.
 *
 * @param jobId          unique id, chosen by the caller (results carry it back)
 * @param correlationId  the caller's reference, e.g. its document id
 * @param profileCode    the profile to run (its latest version)
 * @param document       the file; may be null when {@code text} is given and the job starts
 *                       after text extraction
 * @param documentType   the type the caller expects the text to be, for the classification step
 * @param fields         the fields to extract (null or empty: the expected fields of
 *                       {@code documentTypeId})
 * @param candidateTypes the types the classification step chooses from, each with its fields or
 *                       the id of the document type that holds them
 * @param fromStep       the first step to run (null: the profile's first); a step after text
 *                       extraction needs {@code text}, without it the whole profile runs on
 *                       {@code document}
 * @param text           text to start from, when {@code fromStep} comes after text extraction
 * @param attributes     free-form values the caller wants back on the result
 * @param documentTypeId the id of the document type that defines the processing: its expected fields
 *                       are the fields to extract, and its own prompts, when it has any, replace the
 *                       profile's; null when {@code fields} are given
 */
public record ProcessingJobRequest(
        String jobId,
        String correlationId,
        String profileCode,
        DocumentRef document,
        String documentType,
        List<FieldSpec> fields,
        List<DocumentTypeSpec> candidateTypes,
        StepType fromStep,
        String text,
        Map<String, String> attributes,
        String documentTypeId) {

    /** A job that carries its fields and candidate types itself. */
    public ProcessingJobRequest(String jobId, String correlationId, String profileCode, DocumentRef document,
            String documentType, List<FieldSpec> fields, List<DocumentTypeSpec> candidateTypes, StepType fromStep,
            String text, Map<String, String> attributes) {
        this(jobId, correlationId, profileCode, document, documentType, fields, candidateTypes, fromStep, text,
                attributes, null);
    }

    /** This job with the fields and candidate types its document types hold. */
    public ProcessingJobRequest withTypeFields(List<FieldSpec> newFields, List<DocumentTypeSpec> newCandidateTypes) {
        return new ProcessingJobRequest(jobId, correlationId, profileCode, document, documentType, newFields,
                newCandidateTypes, fromStep, text, attributes, documentTypeId);
    }

    /** This job under another id. */
    public ProcessingJobRequest withJobId(String newJobId) {
        return new ProcessingJobRequest(newJobId, correlationId, profileCode, document, documentType, fields,
                candidateTypes, fromStep, text, attributes, documentTypeId);
    }

    /** This job without its text (e.g. to store the request next to a result that holds the text). */
    public ProcessingJobRequest withoutText() {
        return new ProcessingJobRequest(jobId, correlationId, profileCode, document, documentType, fields,
                candidateTypes, fromStep, null, attributes, documentTypeId);
    }
}
