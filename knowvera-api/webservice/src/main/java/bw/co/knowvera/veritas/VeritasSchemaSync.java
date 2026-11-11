package bw.co.knowvera.veritas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeService;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldService;
import bw.co.roguesystems.ai.schema.ExtractionSchemaDTO;
import bw.co.roguesystems.ai.schema.FieldSpec;

/**
 * The definition of a field to extract (label, type, format, many, mandatory) is kept in
 * veritas-processor only, in the schema of the field's document type (see
 * {@link VeritasJobFactory#schemaCode}); an expected field here keeps the name and what the
 * extracted value is matched against ({@link VeritasFieldDefinitions} reads the definitions).
 * <p>
 * Saving a document type or expected fields here therefore saves the definitions to the schema
 * first: when veritas-processor cannot take them the save fails, and nothing is half saved.
 * After the save the schema is brought in line with the expected fields: a removed or renamed
 * field leaves it, a document type whose code changed gets its schema under the new code. Fields
 * the schema has beyond the expected fields were configured in veritas-processor and stay.
 */
@Aspect
@Component
public class VeritasSchemaSync {

    private static final Logger log = LoggerFactory.getLogger(VeritasSchemaSync.class);

    private final VeritasProfilesClient client;
    private final VeritasFieldDefinitions definitions;
    private final DocumentTypeService documentTypeService;
    private final ExpectedFieldService expectedFieldService;

    public VeritasSchemaSync(VeritasProfilesClient client, VeritasFieldDefinitions definitions,
            DocumentTypeService documentTypeService, ExpectedFieldService expectedFieldService) {
        this.client = client;
        this.definitions = definitions;
        this.documentTypeService = documentTypeService;
        this.expectedFieldService = expectedFieldService;
    }

    @Around("execution(* bw.co.knowvera.document.type.DocumentTypeService.save(..)) && args(documentType)")
    public Object documentTypeSaved(ProceedingJoinPoint call, DocumentTypeDTO documentType) throws Throwable {
        return save(call, documentType.getId(), documentType, documentType.getExpectedFields());
    }

    @Around("execution(* bw.co.knowvera.document.type.DocumentTypeService.addExpectedField(..)) "
            + "&& args(id, expectedFields)")
    public Object expectedFieldsAdded(ProceedingJoinPoint call, String id, Set<ExpectedFieldDTO> expectedFields)
            throws Throwable {
        return save(call, id, null, expectedFields);
    }

    @Around("execution(* bw.co.knowvera.document.type.field.ExpectedFieldService.save(..)) && args(expectedField)")
    public Object expectedFieldSaved(ProceedingJoinPoint call, ExpectedFieldDTO expectedField) throws Throwable {
        return save(call, expectedField.getDocumentTypeId(), null, List.of(expectedField));
    }

    @Around("execution(* bw.co.knowvera.document.type.field.ExpectedFieldService.remove(..)) && args(id)")
    public Object expectedFieldRemoved(ProceedingJoinPoint call, String id) throws Throwable {
        String documentTypeId = null;
        try {
            documentTypeId = expectedFieldService.findById(id).getDocumentTypeId();
        } catch (RuntimeException e) {
            log.debug("Expected field {} was not found before its removal", id);
        }
        return save(call, documentTypeId, null, List.of());
    }

    /**
     * @param documentTypeId the document type as it is before the save (null: a new one)
     * @param saving         the document type being saved, when the call saves one
     * @param incoming       the expected fields the call saves, with their definitions
     */
    private Object save(ProceedingJoinPoint call, @Nullable String documentTypeId, @Nullable DocumentTypeDTO saving,
            @Nullable Collection<ExpectedFieldDTO> incoming) throws Throwable {
        List<FieldSpec> saved = incoming == null ? List.of()
                : incoming.stream().filter(field -> StringUtils.isNotBlank(field.getField()))
                        .map(VeritasJobFactory::field).toList();
        DocumentTypeDTO before = find(documentTypeId);
        String codeBefore = before == null ? null : VeritasJobFactory.schemaCode(before);
        DocumentTypeDTO target = saving != null ? saving : before;
        String code = target == null ? null : VeritasJobFactory.schemaCode(target);

        // The definitions first: they are kept nowhere else, so a save that cannot store them fails
        if (code != null && StringUtils.isNotBlank(target.getName()) && !saved.isEmpty()) {
            try {
                push(code, codeBefore, target.getName(), saved, Set.of(), Set.of());
            } catch (RuntimeException e) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "The fields could not be saved in veritas-processor, so nothing was saved. Try again "
                                + "when it is reachable.", e);
            }
        }

        Object result = call.proceed();

        try {
            DocumentTypeDTO after = find(result instanceof DocumentTypeDTO type ? type.getId() : documentTypeId);
            String codeAfter = after == null ? null : VeritasJobFactory.schemaCode(after);
            if (codeAfter != null && StringUtils.isNotBlank(after.getName())) {
                Set<String> names = names(after);
                Set<String> removed = new LinkedHashSet<>(names(before));
                removed.removeAll(names);
                push(codeAfter, codeBefore, after.getName(), saved, removed, names);
            }
        } catch (RuntimeException e) {
            log.error("The schema of document type {} in veritas-processor could not be brought in line with its "
                    + "expected fields: save the document type again", documentTypeId, e);
        }
        withDefinitions(result, incoming);
        return result;
    }

    /** Saves the schema with the fields written over it; {@code copyFrom} is the code it had before. */
    private void push(String code, @Nullable String copyFrom, String name, List<FieldSpec> saved, Set<String> removed,
            Set<String> expectedNames) {
        ExtractionSchemaDTO schema = client.schema(code);
        if (schema == null && copyFrom != null && !copyFrom.equals(code)) {
            schema = client.schema(copyFrom);
        }
        List<FieldSpec> fields = merge(schema == null ? List.of() : schema.fields(), saved, removed, expectedNames);
        client.saveSchema(code, new ExtractionSchemaDTO(code, null, name, schema == null ? null : schema.description(),
                fields, null, null));
        definitions.invalidate();
    }

    /**
     * The schema's fields after a save here: without the removed ones, the saved ones written over
     * the fields of their name or added, and a plain text field for every expected field the
     * schema still lacks. The schema's other fields stay as they are.
     *
     * @param saved         fields saved now, with their definitions
     * @param removed       names of expected fields that no longer exist (removed or renamed)
     * @param expectedNames names of the expected fields that exist now (empty: not checked)
     */
    static List<FieldSpec> merge(@Nullable List<FieldSpec> schemaFields, List<FieldSpec> saved, Set<String> removed,
            Set<String> expectedNames) {
        Set<String> gone = new LinkedHashSet<>();
        removed.stream().filter(StringUtils::isNotBlank).map(VeritasFieldDefinitions::key).forEach(gone::add);
        saved.forEach(field -> gone.remove(VeritasFieldDefinitions.key(field.name())));

        List<FieldSpec> merged = new ArrayList<>();
        Set<String> placed = new LinkedHashSet<>();
        for (FieldSpec field : schemaFields == null ? List.<FieldSpec>of() : schemaFields) {
            String key = VeritasFieldDefinitions.key(field.name());
            if (gone.contains(key) || !placed.add(key)) {
                continue;
            }
            merged.add(saved.stream().filter(s -> VeritasFieldDefinitions.key(s.name()).equals(key)).findFirst()
                    .orElse(field));
        }
        for (FieldSpec field : saved) {
            if (placed.add(VeritasFieldDefinitions.key(field.name()))) {
                merged.add(field);
            }
        }
        for (String name : expectedNames) {
            if (StringUtils.isNotBlank(name) && placed.add(VeritasFieldDefinitions.key(name))) {
                merged.add(FieldSpec.text(name.trim()));
            }
        }
        return merged;
    }

    /** The saved fields in the result get the definitions they were saved with (the result was read before). */
    private static void withDefinitions(@Nullable Object result, @Nullable Collection<ExpectedFieldDTO> incoming) {
        if (incoming == null || incoming.isEmpty()) {
            return;
        }
        Collection<ExpectedFieldDTO> returned = result instanceof ExpectedFieldDTO field ? List.of(field)
                : result instanceof DocumentTypeDTO type && type.getExpectedFields() != null ? type.getExpectedFields()
                        : List.of();
        for (ExpectedFieldDTO field : returned) {
            incoming.stream()
                    .filter(in -> in.getField() != null && field.getField() != null
                            && VeritasFieldDefinitions.key(in.getField()).equals(VeritasFieldDefinitions.key(field.getField())))
                    .findFirst().ifPresent(in -> {
                        field.setFieldLabel(in.getFieldLabel());
                        field.setFieldType(in.getFieldType());
                        field.setFormat(in.getFormat());
                        field.setMany(in.getMany());
                        field.setMandatory(in.getMandatory());
                    });
        }
    }

    private @Nullable DocumentTypeDTO find(@Nullable String documentTypeId) {
        if (StringUtils.isBlank(documentTypeId)) {
            return null;
        }
        try {
            return documentTypeService.findById(documentTypeId);
        } catch (RuntimeException e) {
            log.debug("Document type {} was not found", documentTypeId);
            return null;
        }
    }

    private static Set<String> names(@Nullable DocumentTypeDTO type) {
        Set<String> names = new LinkedHashSet<>();
        if (type != null && type.getExpectedFields() != null) {
            type.getExpectedFields().stream().map(ExpectedFieldDTO::getField).filter(StringUtils::isNotBlank)
                    .map(VeritasFieldDefinitions::key).forEach(names::add);
        }
        return names;
    }
}
