package bw.co.roguesystems.ai.pipeline;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import bw.co.roguesystems.ai.job.Classification;
import bw.co.roguesystems.ai.provider.ProviderException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.json.JsonMapper;

/**
 * Reads what a language model answered.
 */
final class ModelOutput {

    private static final Pattern THINK_BLOCK = Pattern.compile("(?s)<think>.*?</think>");

    /** Share of the expected type's signals a document must show to match (as in DefaultPrompts). */
    static final double MATCH_THRESHOLD = 0.6;

    private ModelOutput() {
    }

    /** The answer without reasoning blocks (e.g. deepseek's &lt;think&gt;). */
    static String text(String content) {
        return content == null ? "" : THINK_BLOCK.matcher(content).replaceAll("").trim();
    }

    /**
     * The first JSON object in the answer: models often wrap it in prose or a code fence, or add
     * to it afterwards.
     */
    static Map<String, Object> json(String content, JsonMapper jsonMapper) {
        String answer = text(content);
        int start = answer.indexOf('{');
        if (start < 0) {
            throw new ProviderException("the answer holds no JSON object");
        }
        JacksonException invalid = null;
        for (; start >= 0; start = answer.indexOf('{', start + 1)) {
            // Reads one value and leaves what follows it
            try (JsonParser parser = jsonMapper.createParser(answer.substring(start))) {
                Map<?, ?> raw = parser.readValueAs(Map.class);
                Map<String, Object> json = new LinkedHashMap<>();
                raw.forEach((key, value) -> json.put(String.valueOf(key), value));
                return json;
            } catch (JacksonException e) {
                invalid = invalid == null ? e : invalid;
            }
        }
        throw new ProviderException("the answer's JSON is invalid: " + invalid.getOriginalMessage(), invalid);
    }

    /**
     * The score and the match are worked out here from the signals the model found (models are
     * unreliable at arithmetic); the model's own are kept only when it gave no signals.
     */
    static Classification classification(Map<String, Object> json) {
        Map<String, Integer> signalScores = new LinkedHashMap<>();
        if (json.get("signalScores") instanceof Map<?, ?> scores) {
            scores.forEach((signal, value) -> signalScores.put(String.valueOf(signal), toInt(value) > 0 ? 1 : 0));
        }
        Double score = toDouble(json.get("score"));
        Boolean match = toBoolean(json.get("match"));
        if (!signalScores.isEmpty()) {
            score = signalScores.values().stream().mapToInt(Integer::intValue).sum() / (double) signalScores.size();
            match = score >= MATCH_THRESHOLD;
        }
        return new Classification(
                asString(json.get("expectedType")),
                asString(json.get("detectedType")),
                match,
                score,
                signalScores,
                asString(json.get("reason")));
    }

    private static String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    private static Integer toInt(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof Boolean bool) {
            return bool ? 1 : 0;
        }
        try {
            return value == null ? 0 : (int) Double.parseDouble(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static Double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        try {
            return value == null ? null : Double.valueOf(String.valueOf(value).trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Boolean toBoolean(Object value) {
        if (value instanceof Boolean bool) {
            return bool;
        }
        if (value == null) {
            return null;
        }
        return switch (String.valueOf(value).trim().toLowerCase(Locale.ROOT)) {
            case "true", "yes", "y", "1" -> Boolean.TRUE;
            case "false", "no", "n", "0" -> Boolean.FALSE;
            default -> null;
        };
    }
}
