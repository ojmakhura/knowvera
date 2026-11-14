package bw.co.knowvera.screening;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class SanctionsMatcherTest {

    private final SanctionsMatcher matcher = new SanctionsMatcher(0.85);

    private static SanctionsListEntry individual(String reference, String name, List<String> aliases, List<String> births) {
        SanctionsListEntry entry = SanctionsListEntry.Factory.newInstance();
        entry.setSource(ScreeningListSource.UN_SECURITY_COUNCIL);
        entry.setSubjectType(ScreeningSubjectType.INDIVIDUAL);
        entry.setReference(reference);
        entry.setName(name);
        entry.setAliases(aliases);
        entry.setDateOfBirths(births);
        return entry;
    }

    private List<SanctionsMatcher.Match> match(String name, Integer year, SanctionsListEntry... entries) {
        return matcher.match(new SanctionsMatcher.Subject(name, ScreeningSubjectType.INDIVIDUAL, year, "Customer"), List.of(entries));
    }

    @Test
    void jaroWinklerAgreesWithTheReferenceValues() {
        assertEquals(0.961, SanctionsMatcher.jaroWinkler("martha", "marhta"), 0.001);
        assertEquals(1.0, SanctionsMatcher.jaroWinkler("kabo", "kabo"));
        assertEquals(0.0, SanctionsMatcher.jaroWinkler("abc", "xyz"));
    }

    @Test
    void matchesTheSameNameWhateverTheCaseAccentsAndWordOrder() {
        SanctionsListEntry entry = individual("QDi.001", "José María GONZÁLEZ", List.of(), List.of());

        assertEquals(1, match("Gonzalez Jose Maria", null, entry).size());
    }

    @Test
    void matchesTransliterationsAndAliases() {
        SanctionsListEntry entry = individual("QDi.002", "Mohammed Abdullah", List.of("Abu Abdullah"), List.of());

        assertEquals(1, match("Muhammad Abdulla", null, entry).size());
        assertEquals("Abu Abdullah", match("Abu Abdulla", null, entry).get(0).matchedName());
    }

    @Test
    void doesNotMatchDifferentPeople() {
        SanctionsListEntry entry = individual("QDi.003", "Mohammed Abdullah", List.of(), List.of());

        assertTrue(match("Kabo Seretse", null, entry).isEmpty());
        assertTrue(match("Mohammed Seretse", null, entry).isEmpty());
    }

    @Test
    void aSingleCommonNameIsNotEnough() {
        SanctionsListEntry entry = individual("QDi.004", "Mohammed Abdullah Hassan", List.of(), List.of());

        assertTrue(match("Hassan", null, entry).isEmpty());
    }

    @Test
    void aDifferentYearOfBirthLowersTheScore() {
        SanctionsListEntry entry = individual("QDi.005", "Kabelo Dintwe", List.of(), List.of("1961-04-02"));

        double sameYear = match("Kabelo Dintwe", 1961, entry).get(0).score();
        assertTrue(match("Kabelo Dintwe", 1990, entry).isEmpty() || match("Kabelo Dintwe", 1990, entry).get(0).score() < sameYear);
    }

    @Test
    void individualsAreNotScreenedAgainstEntities() {
        SanctionsListEntry entity = individual("QDe.001", "Al Rashid Trust", List.of(), List.of());
        entity.setSubjectType(ScreeningSubjectType.ENTITY);

        assertTrue(match("Al Rashid Trust", null, entity).isEmpty());
        assertEquals(1, matcher.match(new SanctionsMatcher.Subject("Al-Rashid Trust", ScreeningSubjectType.ENTITY, null, "Customer"),
                List.of(entity)).size());
    }
}
