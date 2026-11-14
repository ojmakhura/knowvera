package bw.co.knowvera.screening;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Compares names against sanctions list entries. Names are normalised (case, accents,
 * punctuation, word order) and compared word by word with Jaro-Winkler similarity, so
 * transliteration differences and reordered names still match; a different year of birth
 * lowers the score. Matches are potential: a screening analyst decides on each one.
 */
@Component
public class SanctionsMatcher {

    /** Two words are the same word from this similarity. */
    private static final double WORD_THRESHOLD = 0.88;

    private final double threshold;

    public SanctionsMatcher(@Value("${app.screening.match-threshold:0.85}") double threshold) {
        this.threshold = threshold;
    }

    /**
     * Someone to screen.
     *
     * @param yearOfBirth null when unknown (and always for entities)
     */
    public record Subject(String name, ScreeningSubjectType type, Integer yearOfBirth, String role) {
    }

    public record Match(SanctionsListEntry entry, String matchedName, double score) {
    }

    public List<Match> match(Subject subject, Collection<SanctionsListEntry> entries) {

        List<Match> matches = new ArrayList<>();
        List<String> subjectWords = words(subject.name());
        if (subjectWords.isEmpty()) {
            return matches;
        }

        for (SanctionsListEntry entry : entries) {
            if (entry.getSubjectType() != subject.type()) {
                continue;
            }

            String bestName = null;
            double best = 0;
            List<String> names = new ArrayList<>();
            names.add(entry.getName());
            if (entry.getAliases() != null) {
                names.addAll(entry.getAliases());
            }
            for (String name : names) {
                double score = score(subjectWords, words(name));
                if (score > best) {
                    best = score;
                    bestName = name;
                }
            }

            best = adjustForBirth(best, subject.yearOfBirth(), entry.getDateOfBirths());
            if (best >= threshold) {
                matches.add(new Match(entry, bestName, Math.round(best * 1000) / 1000.0));
            }
        }

        matches.sort((a, b) -> Double.compare(b.score(), a.score()));
        return matches;
    }

    /**
     * How well two names agree: every word of the shorter name is paired with its closest word
     * in the longer one (order ignored) and the similarities averaged, then scaled down when
     * the longer name has words the shorter one lacks. A one-word name must match closely.
     */
    double score(List<String> a, List<String> b) {

        if (a.isEmpty() || b.isEmpty()) {
            return 0;
        }

        List<String> shorter = a.size() <= b.size() ? a : b;
        List<String> longer = a.size() <= b.size() ? b : a;

        double total = 0;
        int matched = 0;
        for (String word : shorter) {
            double best = 0;
            for (String other : longer) {
                best = Math.max(best, wordSimilarity(word, other));
            }
            total += best;
            if (best >= WORD_THRESHOLD) {
                matched++;
            }
        }

        double average = total / shorter.size();
        if (matched < shorter.size()) {
            return average * 0.8;  // a word with no counterpart: at best a weak match
        }
        if (shorter.size() == 1) {
            return longer.size() == 1 ? average : average * 0.75;  // a single name is common
        }

        // Extra words in the longer name (middle names, titles) count only a little against it
        double coverage = (double) shorter.size() / longer.size();
        return average * (0.9 + 0.1 * coverage);
    }

    private static double adjustForBirth(double score, Integer yearOfBirth, List<String> listedDates) {

        if (yearOfBirth == null || listedDates == null || listedDates.isEmpty()) {
            return score;
        }

        boolean any = false;
        for (String date : listedDates) {
            if (date != null && date.length() >= 4 && StringUtils.isNumeric(date.substring(0, 4))) {
                any = true;
                if (Math.abs(Integer.parseInt(date.substring(0, 4)) - yearOfBirth) <= 1) {
                    return Math.min(1.0, score + 0.05);
                }
            }
        }
        return any ? score * 0.85 : score;
    }

    /**
     * How alike two words are: Jaro-Winkler, or a strong match when their consonant skeletons
     * agree, which catches transliterations Jaro-Winkler scores low (Muhammad / Mohammed,
     * Yusuf / Yousef: both "mhmd", "ysf").
     */
    static double wordSimilarity(String a, String b) {

        double similarity = jaroWinkler(a, b);
        String skeletonA = skeleton(a);
        if (skeletonA.length() >= 3 && skeletonA.equals(skeleton(b))) {
            similarity = Math.max(similarity, 0.92);
        }
        return similarity;
    }

    /** The word without vowels (or the y and w that stand for them) and without doubled letters. */
    static String skeleton(String word) {

        StringBuilder skeleton = new StringBuilder();
        String stripped = word.charAt(0) + word.substring(1).replaceAll("[aeiouyw]", "");
        for (char c : stripped.toCharArray()) {
            if (skeleton.length() == 0 || skeleton.charAt(skeleton.length() - 1) != c) {
                skeleton.append(c);
            }
        }
        return skeleton.toString();
    }

    /**
     * Jaro-Winkler similarity of two words, from 0 (nothing in common) to 1 (the same): suited to
     * short strings such as names, it forgives transpositions and rewards a common prefix.
     */
    static double jaroWinkler(String a, String b) {

        if (a.equals(b)) {
            return 1.0;
        }
        if (a.isEmpty() || b.isEmpty()) {
            return 0.0;
        }

        int window = Math.max(0, Math.max(a.length(), b.length()) / 2 - 1);
        boolean[] aMatched = new boolean[a.length()];
        boolean[] bMatched = new boolean[b.length()];
        int matches = 0;
        for (int i = 0; i < a.length(); i++) {
            int from = Math.max(0, i - window);
            int to = Math.min(b.length() - 1, i + window);
            for (int j = from; j <= to; j++) {
                if (!bMatched[j] && a.charAt(i) == b.charAt(j)) {
                    aMatched[i] = true;
                    bMatched[j] = true;
                    matches++;
                    break;
                }
            }
        }
        if (matches == 0) {
            return 0.0;
        }

        int transpositions = 0;
        for (int i = 0, j = 0; i < a.length(); i++) {
            if (aMatched[i]) {
                while (!bMatched[j]) {
                    j++;
                }
                if (a.charAt(i) != b.charAt(j)) {
                    transpositions++;
                }
                j++;
            }
        }

        double m = matches;
        double jaro = (m / a.length() + m / b.length() + (m - transpositions / 2.0) / m) / 3.0;

        int prefix = 0;
        while (prefix < Math.min(4, Math.min(a.length(), b.length())) && a.charAt(prefix) == b.charAt(prefix)) {
            prefix++;
        }
        return jaro + prefix * 0.1 * (1 - jaro);
    }

    static List<String> words(String name) {

        if (StringUtils.isBlank(name)) {
            return List.of();
        }
        String plain = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        plain = plain.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}]+", " ").trim();
        return Arrays.stream(plain.split(" "))
                .filter(word -> word.length() > 1)  // initials say little
                .collect(Collectors.toList());
    }
}
