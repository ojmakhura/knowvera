package bw.co.knowvera.matcher;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UniversalStringMatcherTest {

    private final UniversalStringMatcher matcher = new UniversalStringMatcher();

    @Test
    void filteredSimilarityIsZeroWhenEitherSideIsBlank() {
        assertEquals(0.0, matcher.calculateFilteredSimilarity("John Doe", null));
        assertEquals(0.0, matcher.calculateFilteredSimilarity("John Doe", " "));
        assertEquals(0.0, matcher.calculateFilteredSimilarity(null, "John Doe"));
    }

    @Test
    void filteredSimilarityIsOneWhenBothSidesAreBlank() {
        assertEquals(1.0, matcher.calculateFilteredSimilarity(null, ""));
    }

    @Test
    void filteredSimilarityScoresSharedTokens() {
        assertEquals(1.0, matcher.calculateFilteredSimilarity("John Doe", "Mr. John Doe"));
        assertEquals(0.5, matcher.calculateFilteredSimilarity("John Doe", "John Smith"));
    }
}
