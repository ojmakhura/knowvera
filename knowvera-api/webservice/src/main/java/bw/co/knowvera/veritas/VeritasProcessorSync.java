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
import bw.co.roguesystems.ai.processor.DataProcessorDTO;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldType;

/**
 * A document type is linked to a data processor in veritas-processor by its id (see
 * {@link VeritasJobFactory#processorId}), which defines what is done with the type's documents:
 * the fields expected in them, with their definition (label, type, format, many, mandatory).
 * That definition is kept there only ({@link VeritasFieldDefinitions} reads it back).
 * <p>
 * Saving a document type or expected fields here therefore saves the fields to the data processor
 * first: when veritas-processor cannot take them the save fails, and nothing is half saved. A
 * document type without a link is linked to the data processor with its code, which is made, with
 * the type's code and name, when there is none.
 * After the save, a removed or renamed field leaves the data processor. Fields the data processor
 * has beyond the expected fields here stay: they were added in veritas-processor.
 */
@Aspect
@Component
public class VeritasProcessorSync {

    private static final Logger log = LoggerFactory.getLogger(VeritasProcessorSync.class);

    private final VeritasProfilesClient client;
    private final VeritasFieldDefinitions definitions;
    private final DocumentTypeService documentTypeService;
    private final ExpectedFieldService expectedFieldService;

    public VeritasProcessorSync(VeritasProfilesClient client, VeritasFieldDefinitions definitions,
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
        List<ExpectedFieldDTO> saved = incoming == null ? List.of()
                : incoming.stream().filter(field -> StringUtils.isNotBlank(field.getField())).toList();
        DocumentTypeDTO before = find(documentTypeId);
        DocumentTypeDTO target = saving != null ? saving : before;

        // The definitions first: they are kept nowhere else, so a save that cannot store them fails
        if (linkable(target) && !saved.isEmpty()) {
            try {
                DataProcessorDTO processor = processor(target);
                push(processor, saved, Set.of(), List.of());
                if (saving != null) {
                    saving.setDataProcessorId(processor.getId());
                }
            } catch (RuntimeException e) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                        "The fields could not be saved in veritas-processor, so nothing was saved. Try again "
                                + "when it is reachable.", e);
            }
        }

        Object result = call.proceed();

        try {
            DocumentTypeDTO after = find(result instanceof DocumentTypeDTO type ? type.getId() : documentTypeId);
            if (linkable(after)) {
                DataProcessorDTO processor = processor(after);
                if (!processor.getId().equalsIgnoreCase(StringUtils.defaultString(after.getDataProcessorId()))) {
                    definitions.link(after.getId(), processor.getId());
                    if (result instanceof DocumentTypeDTO type) {
                        type.setDataProcessorId(processor.getId());
                    }
                }
                Set<String> removed = new LinkedHashSet<>(names(before));
                removed.removeAll(names(after));
                // A field removed here leaves the data processor only when the type still uses the same one
                boolean sameProcessor = before == null || StringUtils.isBlank(before.getDataProcessorId())
                        || processor.getId().equalsIgnoreCase(before.getDataProcessorId());
                push(processor, saved, sameProcessor ? removed : Set.of(),
                        after.getExpectedFields() == null ? List.of() : after.getExpectedFields());
            }
        } catch (RuntimeException e) {
            log.error("The data processor of document type {} in veritas-processor could not be brought in line with "
                    + "its expected fields: save the document type again", documentTypeId, e);
        }
        withDefinitions(result, incoming);
        return result;
    }

    private static boolean linkable(@Nullable DocumentTypeDTO type) {
        return type != null && StringUtils.isNotBlank(type.getCode()) && StringUtils.isNotBlank(type.getName());
    }

    /**
     * The document type's data processor: the one it is linked to by id; for a type without a link
     * (or whose processor is gone) the processor with the type's code, made when there is none.
     */
    private DataProcessorDTO processor(DocumentTypeDTO type) {
        String id = VeritasJobFactory.processorId(type);
        DataProcessorDTO processor = id == null ? null : client.processor(id);
        if (processor == null) {
            processor = client.processorByCode(type.getCode().trim());
        }
        if (processor == null) {
            DataProcessorDTO created = new DataProcessorDTO();
            created.setCode(type.getCode().trim());
            created.setName(type.getName().trim());
            processor = client.saveProcessor(created);
            log.info("Created the data processor '{}' in veritas-processor", processor.getCode());
        }
        return processor;
    }

    /**
     * Saves the fields to the data processor.
     *
     * @param saved    fields saved now, with their definitions
     * @param removed  names of expected fields that no longer exist here (removed or renamed)
     * @param expected the expected fields that exist here now: each must be in the data processor
     */
    private void push(DataProcessorDTO processor, List<ExpectedFieldDTO> saved, Set<String> removed,
            List<ExpectedFieldDTO> expected) {
        List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> existing = processor.getExpectedFields() == null
                ? List.of() : processor.getExpectedFields();
        List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> toSave = toSave(existing, saved, expected);
        if (!toSave.isEmpty()) {
            client.saveExpectedFields(processor.getId(), toSave);
        }
        for (bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO field : existing) {
            String key = VeritasFieldDefinitions.key(field.getField());
            boolean savedAgain = saved.stream().anyMatch(s -> VeritasFieldDefinitions.key(s.getField()).equals(key));
            if (removed.contains(key) && !savedAgain && field.getId() != null) {
                client.removeExpectedField(field.getId());
            }
        }
        definitions.invalidate();
    }

    /**
     * What to send to the data processor: the saved fields (with the id of the processor's field
     * of that name, so that it is updated and not added twice), and every expected field the
     * processor still lacks.
     */
    static List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> toSave(
            List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> existing, List<ExpectedFieldDTO> saved,
            List<ExpectedFieldDTO> expected) {
        List<bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO> toSave = new ArrayList<>();
        Set<String> placed = new LinkedHashSet<>();
        for (ExpectedFieldDTO field : saved) {
            String key = VeritasFieldDefinitions.key(field.getField());
            if (placed.add(key)) {
                bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO converted = convert(field);
                existing.stream().filter(e -> VeritasFieldDefinitions.key(e.getField()).equals(key)).findFirst()
                        .ifPresent(e -> converted.setId(e.getId()));
                toSave.add(converted);
            }
        }
        for (ExpectedFieldDTO field : expected) {
            if (StringUtils.isBlank(field.getField())) {
                continue;
            }
            String key = VeritasFieldDefinitions.key(field.getField());
            boolean there = existing.stream().anyMatch(e -> VeritasFieldDefinitions.key(e.getField()).equals(key));
            if (!there && placed.add(key)) {
                toSave.add(convert(field));
            }
        }
        return toSave;
    }

    /**
     * An expected field here as the data processor's: its definition only. What it is matched against
     * (match to, target type, exact match) is verification, which stays here.
     */
    static bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO convert(ExpectedFieldDTO field) {
        bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO converted = new bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO();
        converted.setField(field.getField().trim());
        converted.setFieldLabel(StringUtils.trimToNull(field.getFieldLabel()));
        converted.setFieldType(field.getFieldType() == null ? ExpectedFieldType.STRING
                : ExpectedFieldType.valueOf(field.getFieldType().name()));
        converted.setFormat(StringUtils.trimToNull(field.getFormat()));
        converted.setMany(Boolean.TRUE.equals(field.getMany()));
        converted.setMandatory(Boolean.TRUE.equals(field.getMandatory()));
        return converted;
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
