package bw.co.roguesystems.ai.job;

import java.util.List;
import java.util.Map;

import bw.co.roguesystems.ai.profile.StepType;
import bw.co.roguesystems.ai.schema.DocumentRef;
import bw.co.roguesystems.ai.schema.DocumentTypeSpec;
import bw.co.roguesystems.ai.schema.FieldSpec;

/**
 * A document to process with a profile.
 *
 * @param jobId          unique id, chosen by the caller (results carry it back)
 * @param correlationId  the caller's reference, e.g. its document id
 * @param profileCode    the profile to run (its latest version)
 * @param document       the file; may be null when {@code text} is given and the job starts
 *                       after text extraction
 * @param documentType   the document type the caller expects
 * @param fields         the fields to extract
 * @param candidateTypes the types the classification step chooses from
 * @param fromStep       the first step to run (null: the profile's first); a step after text
 *                       extraction needs {@code text}, without it the whole profile runs on
 *                       {@code document}
 * @param text           text to start from, when {@code fromStep} comes after text extraction
 * @param attributes     free-form values the caller wants back on the result
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
        Map<String, String> attributes) {

    /** This job under another id. */
    public ProcessingJobRequest withJobId(String newJobId) {
        return new ProcessingJobRequest(newJobId, correlationId, profileCode, document, documentType, fields,
                candidateTypes, fromStep, text, attributes);
    }

    /** This job without its text (e.g. to store the request next to a result that holds the text). */
    public ProcessingJobRequest withoutText() {
        return new ProcessingJobRequest(jobId, correlationId, profileCode, document, documentType, fields,
                candidateTypes, fromStep, null, attributes);
    }
}
