package bw.co.roguesystems.ai.pipeline;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bw.co.roguesystems.ai.job.ProcessingJobRequest;
import bw.co.roguesystems.ai.schema.DocumentTypeSpec;
import bw.co.roguesystems.ai.schema.FieldSpec;

/**
 * The JSON schemas the model servers hold an answer to (Ollama's format, LM Studio's
 * response_format), so an answer is the JSON object asked for rather than prose around one.
 */
final class ResponseSchemas {

    private ResponseSchemas() {
    }

    /** One property per requested field, null allowed for what the document does not show. */
    static Map<String, Object> fields(List<FieldSpec> fields) {
        if (fields == null || fields.isEmpty()) {
            return null;
        }
        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> names = new ArrayList<>();
        for (FieldSpec field : fields) {
            properties.put(field.name(), field.many()
                    ? Map.of("type", List.of("array", "null"), "items", Map.of("type", types(field, false)))
                    : Map.of("type", types(field, true)));
            names.add(field.name());
        }
        return object(properties, names);
    }

    /** Numbers may come as text ("P 1,250.50"): FieldValues reads them. */
    private static List<String> types(FieldSpec field, boolean nullable) {
        List<String> types = new ArrayList<>(switch (field.typeOrDefault()) {
            case STRING, DATE -> List.of("string");
            case NUMBER -> List.of("number", "string");
            case BOOLEAN -> List.of("boolean");
            case OBJECT -> List.of("object");
        });
        if (nullable) {
            types.add("null");
        }
        return types;
    }

    /** The answer the default classification prompt asks for, its signals those of the expected type. */
    static Map<String, Object> classification(ProcessingJobRequest request) {
        Map<String, Object> signals = new LinkedHashMap<>();
        if (request.candidateTypes() != null && request.documentType() != null) {
            for (DocumentTypeSpec type : request.candidateTypes()) {
                if (request.documentType().equalsIgnoreCase(type.name()) && type.fields() != null) {
                    type.fields().forEach(field -> signals.put(field.name(), Map.of("type", "integer", "enum", List.of(0, 1))));
                }
            }
        }
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("expectedType", Map.of("type", "string"));
        properties.put("detectedType", Map.of("type", "string"));
        properties.put("match", Map.of("type", "boolean"));
        properties.put("score", Map.of("type", "number"));
        properties.put("signalScores", signals.isEmpty() ? Map.of("type", "object")
                : object(signals, new ArrayList<>(signals.keySet())));
        properties.put("reason", Map.of("type", "string"));
        return object(properties, new ArrayList<>(properties.keySet()));
    }

    private static Map<String, Object> object(Map<String, Object> properties, List<String> required) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", required);
        schema.put("additionalProperties", false);
        return schema;
    }
}
