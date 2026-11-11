package bw.co.roguesystems.ai.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.Test;

import bw.co.roguesystems.ai.job.Classification;
import bw.co.roguesystems.ai.provider.ProviderException;
import tools.jackson.databind.json.JsonMapper;

class ModelOutputTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void dropsReasoningBlocks() {
        assertThat(ModelOutput.text("<think>hmm\nlet me see</think>\n Clean text ")).isEqualTo("Clean text");
    }

    @Test
    void findsTheJsonInsideProseAndCodeFences() {
        Map<String, Object> json = ModelOutput.json("<think>{\"no\":1}</think>Here you go:\n```json\n"
                + "{\"idNumber\": \"123\", \"dateOfBirth\": null}\n```", jsonMapper);

        assertThat(json).containsEntry("idNumber", "123").containsKey("dateOfBirth");
        assertThat(json).doesNotContainKey("no");
    }

    @Test
    void anAnswerWithoutJsonIsAProviderFailure() {
        assertThatThrownBy(() -> ModelOutput.json("I cannot help with that", jsonMapper))
                .isInstanceOf(ProviderException.class);
        assertThatThrownBy(() -> ModelOutput.json("{not json}", jsonMapper)).isInstanceOf(ProviderException.class);
    }

    @Test
    void computesTheScoreFromTheSignalsWhenTheModelGaveNone() {
        Classification classification = ModelOutput.classification(ModelOutput.json("""
                {"expectedType": "National ID", "detectedType": "National ID", "match": "true",
                 "signalScores": {"idNumber": 1, "surname": 1, "photo": 0, "expiry": true}, "reason": "ok"}""",
                jsonMapper));

        assertThat(classification.match()).isTrue();
        assertThat(classification.score()).isEqualTo(0.75);
        assertThat(classification.signalScores()).containsEntry("expiry", 1).containsEntry("photo", 0);
    }

    @Test
    void worksOutScoreAndMatchFromTheSignalsWhateverTheModelClaims() {
        Classification classification = ModelOutput.classification(
                ModelOutput.json("{\"match\": false, \"score\": 0.2, \"signalScores\": {\"a\": 1, \"b\": 1, \"c\": 0}}",
                        jsonMapper));

        assertThat(classification.score()).isCloseTo(0.667, org.assertj.core.data.Offset.offset(0.001));
        assertThat(classification.match()).isTrue();
    }

    @Test
    void keepsTheModelsScoreAndMatchWhenItGaveNoSignals() {
        Classification classification = ModelOutput.classification(
                ModelOutput.json("{\"match\": \"yes\", \"score\": 0.9}", jsonMapper));

        assertThat(classification.score()).isEqualTo(0.9);
        assertThat(classification.match()).isTrue();
    }

    @Test
    void readsTheFirstObjectWhenMoreFollows() {
        Map<String, Object> json = ModelOutput.json(
                "Use {braces} like this: {\"idNumber\": \"123\"}\nNote: {\"other\": 1} is not needed.", jsonMapper);

        assertThat(json).containsOnlyKeys("idNumber");
    }
}
