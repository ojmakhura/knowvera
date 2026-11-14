package bw.co.knowvera.processing;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bw.co.knowvera.document.DocumentDTO;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeService;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.processing.job.ProcessingJobRequest;
import bw.co.knowvera.processing.profile.StepType;
import bw.co.knowvera.processing.schema.DocumentRef;
import bw.co.knowvera.processing.schema.DocumentTypeSpec;
import bw.co.knowvera.processing.schema.FieldSpec;
import bw.co.knowvera.processing.schema.FieldType;

/**
 * Builds a the processing job for a document: its type's profile, expected fields, and every
 * document type as a classification candidate.
 */
@Component
public class ProcessingJobFactory {

    private final DocumentTypeService documentTypeService;
    private final String defaultProfile;

    public ProcessingJobFactory(DocumentTypeService documentTypeService,
            @Value("${app.processing.default-profile:default}") String defaultProfile) {
        this.documentTypeService = documentTypeService;
        this.defaultProfile = defaultProfile;
    }

    /**
     * A job whose correlation id is the document id.
     *
     * @param fromStep null for the whole profile, or the step to start from (with the document's
     *                 stored text when after text extraction)
     */
    public ProcessingJobRequest create(DocumentDTO document, @Nullable StepType fromStep) {
        DocumentTypeDTO type = StringUtils.isBlank(document.getDocumentTypeId()) ? null
                : documentTypeService.findById(document.getDocumentTypeId());
        String profile = type == null || StringUtils.isBlank(type.getProcessingProfileCode()) ? defaultProfile
                : type.getProcessingProfileCode();
        boolean fromText = fromStep != null && fromStep != StepType.TEXT_EXTRACTION;

        // What is done with a document is defined by its type (the fields expected in it, and prompts
        // of its own): the job names that type by id, and gives the types classification chooses
        // from by name, each with its id
        return new ProcessingJobRequest(
                UUID.randomUUID().toString(),
                document.getId(),
                profile,
                new DocumentRef(document.getUrl(), contentType(document), document.getFileName()),
                type == null ? document.getDocumentType() : type.getName(),
                null,
                documentTypeService.getAll().stream()
                        .map(candidate -> new DocumentTypeSpec(candidate.getName(), null, candidate.getId()))
                        .toList(),
                fromStep,
                fromText ? document.getFileContent() : null,
                Map.of(),
                type == null ? null : type.getId());
    }

    public static List<FieldSpec> fields(@Nullable List<ExpectedFieldDTO> expectedFields) {
        if (expectedFields == null) {
            return List.of();
        }
        return expectedFields.stream()
                .map(ProcessingJobFactory::field)
                .toList();
    }

    /** An expected field as the processor's field: same name, label, type, format and flags. */
    static FieldSpec field(ExpectedFieldDTO field) {
        return new FieldSpec(field.getField(), StringUtils.trimToNull(field.getFieldLabel()),
                field.getFieldType() == null ? FieldType.STRING : FieldType.valueOf(field.getFieldType().name()),
                StringUtils.trimToNull(field.getFormat()), field.getMany(), field.getMandatory());
    }

    private static @Nullable String contentType(DocumentDTO document) {
        Map<?, ?> metadata = document.getMetadata();
        Object contentType = metadata == null ? null : metadata.get("contentType");
        return contentType == null ? null : String.valueOf(contentType);
    }
}
