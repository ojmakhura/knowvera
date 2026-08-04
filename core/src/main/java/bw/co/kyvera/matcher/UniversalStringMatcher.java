package bw.co.kyvera.matcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class UniversalStringMatcher {

    /**
     * Cleans and splits a string into a set of unique words.
     */
    private Set<String> getTokens(String input) {
        if (input == null || input.isBlank()) {
            return new HashSet<>();
        }
        
        // 1. Lowercase
        // 2. Remove non-alphanumeric characters (keeps only letters and numbers)
        // 3. Split by whitespace
        String cleaned = input.toLowerCase().replaceAll("[^a-z0-9\\s]", "");
        String[] tokens = cleaned.split("\\s+");
        
        return new HashSet<>(Arrays.asList(tokens));
    }

    /**
     * Calculates Jaccard Similarity: (A ∩ B) / (A ∪ B)
     */
    public double calculateSimilarity(String str1, String str2) {
        Set<String> set1 = getTokens(str1);
        Set<String> set2 = getTokens(str2);

        if (set1.isEmpty() && set2.isEmpty()) return 1.0;
        if (set1.isEmpty() || set2.isEmpty()) return 0.0;

        // Find Intersection
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        // Find Union
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    public boolean isMatch(String str1, String str2, double threshold) {
        return calculateSimilarity(str1, str2) >= threshold;
    }

    public double calculateFilteredSimilarity(String shortStr, String longStr) {

        if(StringUtils.isNotBlank(shortStr) && StringUtils.isBlank(longStr)) {
            return 1.0;
        } else if(StringUtils.isBlank(shortStr) && StringUtils.isBlank(longStr)) {
            return 1.0;
        } else if(StringUtils.isBlank(shortStr) && StringUtils.isNotBlank(longStr)) {
            return 0.0;
        }

        Set<String> smallSet = getTokens(shortStr.toLowerCase());
        Set<String> largeSet = getTokens(longStr.toLowerCase());

        // Swap if user put them in the wrong order
        if (smallSet.size() > largeSet.size()) {
            Set<String> temp = smallSet;
            smallSet = largeSet;
            largeSet = temp;
        }

        // This is your logic: keep only tokens in the large set that exist in the small set
        Set<String> filteredLargeSet = new HashSet<>(largeSet);
        filteredLargeSet.retainAll(smallSet);

        // Now compare the small set to the filtered version
        if (smallSet.isEmpty()) return 0.0;

        // This will return 1.0 if all tokens in the smaller string
        // are found somewhere in the larger string.

        double score = filteredLargeSet.size() / (double) smallSet.size();

        return score;
    }
}
