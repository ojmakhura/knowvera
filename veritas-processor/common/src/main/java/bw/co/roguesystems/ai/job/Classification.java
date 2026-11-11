package bw.co.roguesystems.ai.job;

import java.util.Map;

/**
 * The classification step's answer, as the model gave it. Deciding what it means for the
 * document (e.g. rejecting a mismatch) is up to the caller.
 *
 * @param expectedType  the type the caller expected
 * @param detectedType  the type the model detected, or UNKNOWN
 * @param match         whether the document is the expected type: the score reaches the match
 *                      threshold (the model's own verdict when it gave no signal scores)
 * @param score         0-1 share of the expected type's signals found, computed from the
 *                      signal scores (the model's own when it gave no signal scores)
 * @param signalScores  1 or 0 per signal (field) of the expected type
 * @param reason        the model's short explanation
 */
public record Classification(
        String expectedType,
        String detectedType,
        Boolean match,
        Double score,
        Map<String, Integer> signalScores,
        String reason) {
}
