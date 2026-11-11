package bw.co.knowvera.veritas;

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
import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.profile.StepType;
import bw.co.roguesystems.ai.schema.DocumentRef;
import bw.co.roguesystems.ai.schema.DocumentTypeSpec;
import bw.co.roguesystems.ai.schema.FieldSpec;
import bw.co.roguesystems.ai.schema.FieldType;

/**
 * Builds a veritas-processor job for a document: its type's profile, expected fields, and every
 * document type as a classification candidate.
 */
@Component
public class VeritasJobFactory {

    private final DocumentTypeService documentTypeService;
    private final String defaultProfile;

    public VeritasJobFactory(DocumentTypeService documentTypeService,
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

        // The fields to extract are kept in veritas-processor's schemas, which know nothing of document
        // types: the job names the type's schema, and gives the types classification chooses from by
        // name, each with the schema that holds its fields
        return new ProcessingJobRequest(
                UUID.randomUUID().toString(),
                document.getId(),
                profile,
                new DocumentRef(document.getUrl(), contentType(document), document.getFileName()),
                type == null ? document.getDocumentType() : type.getName(),
                null,
                documentTypeService.getAll().stream()
                        .map(candidate -> new DocumentTypeSpec(candidate.getName(), null, schemaCode(candidate)))
                        .toList(),
                fromStep,
                fromText ? document.getFileContent() : null,
                Map.of(),
                type == null ? null : schemaCode(type));
    }

    /**
     * The code of the schema this API keeps for a document type in veritas-processor: the type's code (its id when
     * it has none), with what a schema code cannot hold replaced.
     */
    public static @Nullable String schemaCode(DocumentTypeDTO type) {
        return schemaCode(type.getCode(), type.getId());
    }

    /** The schema code of the document type with this code and id. */
    public static @Nullable String schemaCode(@Nullable String typeCode, @Nullable String typeId) {
        String code = StringUtils.defaultIfBlank(typeCode, typeId);
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return StringUtils.left(code.trim().replaceAll("[^A-Za-z0-9_-]", "_").replaceAll("^[_-]+", ""), 100);
    }

    public static List<FieldSpec> fields(@Nullable List<ExpectedFieldDTO> expectedFields) {
        if (expectedFields == null) {
            return List.of();
        }
        return expectedFields.stream()
                .map(VeritasJobFactory::field)
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
