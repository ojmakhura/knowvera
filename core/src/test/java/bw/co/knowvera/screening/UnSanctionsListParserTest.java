package bw.co.knowvera.screening;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

class UnSanctionsListParserTest {

    private static final String LIST = """
            <?xml version="1.0" encoding="UTF-8"?>
            <CONSOLIDATED_LIST dateGenerated="2026-11-02T10:00:00.000Z">
              <INDIVIDUALS>
                <INDIVIDUAL>
                  <DATAID>1</DATAID>
                  <FIRST_NAME>ABDUL</FIRST_NAME>
                  <SECOND_NAME>RAHMAN</SECOND_NAME>
                  <THIRD_NAME>YASIN</THIRD_NAME>
                  <REFERENCE_NUMBER>QDi.123</REFERENCE_NUMBER>
                  <LISTED_ON>2001-10-17</LISTED_ON>
                  <COMMENTS1>Listed pursuant to resolution 1267</COMMENTS1>
                  <NATIONALITY><VALUE>Iraq</VALUE></NATIONALITY>
                  <INDIVIDUAL_ALIAS><QUALITY>Good</QUALITY><ALIAS_NAME>Aboud Yasin</ALIAS_NAME></INDIVIDUAL_ALIAS>
                  <INDIVIDUAL_ALIAS><QUALITY>Low</QUALITY><ALIAS_NAME></ALIAS_NAME></INDIVIDUAL_ALIAS>
                  <INDIVIDUAL_DATE_OF_BIRTH><TYPE_OF_DATE>EXACT</TYPE_OF_DATE><DATE>1960-04-10</DATE></INDIVIDUAL_DATE_OF_BIRTH>
                  <INDIVIDUAL_DATE_OF_BIRTH><TYPE_OF_DATE>APPROXIMATELY</TYPE_OF_DATE><YEAR>1961</YEAR></INDIVIDUAL_DATE_OF_BIRTH>
                </INDIVIDUAL>
              </INDIVIDUALS>
              <ENTITIES>
                <ENTITY>
                  <DATAID>2</DATAID>
                  <FIRST_NAME>AL-RASHID TRUST</FIRST_NAME>
                  <REFERENCE_NUMBER>QDe.005</REFERENCE_NUMBER>
                  <LISTED_ON>2001-10-06</LISTED_ON>
                  <ENTITY_ALIAS><ALIAS_NAME>Al Rasheed Trust</ALIAS_NAME></ENTITY_ALIAS>
                </ENTITY>
              </ENTITIES>
            </CONSOLIDATED_LIST>
            """;

    private final UnSanctionsListParser parser = new UnSanctionsListParser();

    @Test
    void readsIndividualsAndEntities() throws Exception {
        UnSanctionsListParser.ParsedList list = parser.parse(new ByteArrayInputStream(LIST.strip().getBytes(StandardCharsets.UTF_8)));

        assertEquals("2026-11-02T10:00:00.000Z", list.version());
        assertEquals(2, list.entries().size());

        SanctionsListEntry person = list.entries().get(0);
        assertEquals(ScreeningSubjectType.INDIVIDUAL, person.getSubjectType());
        assertEquals("QDi.123", person.getReference());
        assertEquals("ABDUL RAHMAN YASIN", person.getName());
        assertEquals(List.of("Aboud Yasin"), person.getAliases());
        assertEquals(List.of("1960-04-10", "1961"), person.getDateOfBirths());
        assertEquals(List.of("Iraq"), person.getNationalities());
        assertEquals(LocalDate.of(2001, 10, 17), person.getListedOn());

        SanctionsListEntry entity = list.entries().get(1);
        assertEquals(ScreeningSubjectType.ENTITY, entity.getSubjectType());
        assertEquals("AL-RASHID TRUST", entity.getName());
        assertEquals(List.of("Al Rasheed Trust"), entity.getAliases());
    }

    @Test
    void refusesDocumentTypeDeclarations() {
        String hostile = """
                <?xml version="1.0"?>
                <!DOCTYPE x [<!ENTITY secret SYSTEM "file:///etc/passwd">]>
                <CONSOLIDATED_LIST><INDIVIDUALS><INDIVIDUAL><FIRST_NAME>&secret;</FIRST_NAME>
                <REFERENCE_NUMBER>X</REFERENCE_NUMBER></INDIVIDUAL></INDIVIDUALS></CONSOLIDATED_LIST>
                """;

        assertThrows(Exception.class, () -> parser.parse(new ByteArrayInputStream(hostile.strip().getBytes(StandardCharsets.UTF_8))));
    }
}
