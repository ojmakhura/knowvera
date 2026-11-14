package bw.co.knowvera.screening;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Reads the UN Security Council Consolidated List (https://scsanctions.un.org, XML): the
 * individuals and entities designated under the UNSC sanctions regimes that Botswana gives
 * effect to (FI Act s.27, Financial Intelligence (Implementation of UNSC Resolutions)
 * Regulations 2022).
 */
@Component
public class UnSanctionsListParser {

    /** The list's version and its entries, not yet saved. */
    public record ParsedList(String version, List<SanctionsListEntry> entries) {
    }

    public ParsedList parse(InputStream xml) throws Exception {

        Document document = secureBuilder().parse(xml);
        Element root = document.getDocumentElement();
        String version = StringUtils.defaultIfBlank(root.getAttribute("dateGenerated"), LocalDate.now().toString());

        List<SanctionsListEntry> entries = new ArrayList<>();
        for (Element individual : children(root, "INDIVIDUALS", "INDIVIDUAL")) {
            entries.add(entry(individual, ScreeningSubjectType.INDIVIDUAL, version, "INDIVIDUAL_ALIAS"));
        }
        for (Element entity : children(root, "ENTITIES", "ENTITY")) {
            entries.add(entry(entity, ScreeningSubjectType.ENTITY, version, "ENTITY_ALIAS"));
        }

        entries.removeIf(e -> StringUtils.isBlank(e.getReference()) || StringUtils.isBlank(e.getName()));
        return new ParsedList(version, entries);
    }

    private static SanctionsListEntry entry(Element element, ScreeningSubjectType type, String version, String aliasTag) {

        SanctionsListEntry entry = SanctionsListEntry.Factory.newInstance();
        entry.setSource(ScreeningListSource.UN_SECURITY_COUNCIL);
        entry.setSubjectType(type);
        entry.setListVersion(version);
        entry.setReference(text(element, "REFERENCE_NUMBER"));
        entry.setComments(text(element, "COMMENTS1"));

        // Individuals' names come in up to four parts; an entity's name is in FIRST_NAME
        List<String> parts = new ArrayList<>();
        for (String tag : List.of("FIRST_NAME", "SECOND_NAME", "THIRD_NAME", "FOURTH_NAME")) {
            String part = text(element, tag);
            if (StringUtils.isNotBlank(part)) {
                parts.add(part);
            }
        }
        entry.setName(String.join(" ", parts));

        String listedOn = text(element, "LISTED_ON");
        if (StringUtils.isNotBlank(listedOn)) {
            try {
                entry.setListedOn(LocalDate.parse(listedOn.trim()));
            } catch (DateTimeParseException e) {
                // Leave it unset: the date is informational
            }
        }

        Set<String> aliases = new LinkedHashSet<>();
        for (Element alias : direct(element, aliasTag)) {
            String name = text(alias, "ALIAS_NAME");
            if (StringUtils.isNotBlank(name)) {
                aliases.add(name.trim());
            }
        }
        entry.setAliases(new ArrayList<>(aliases));

        Set<String> nationalities = new LinkedHashSet<>();
        for (Element nationality : direct(element, "NATIONALITY")) {
            for (Element value : direct(nationality, "VALUE")) {
                if (StringUtils.isNotBlank(value.getTextContent())) {
                    nationalities.add(value.getTextContent().trim());
                }
            }
        }
        entry.setNationalities(new ArrayList<>(nationalities));

        Set<String> birthDates = new LinkedHashSet<>();
        for (Element birth : direct(element, "INDIVIDUAL_DATE_OF_BIRTH")) {
            String date = StringUtils.defaultIfBlank(text(birth, "DATE"), text(birth, "YEAR"));
            if (StringUtils.isNotBlank(date)) {
                birthDates.add(date.trim());
            }
        }
        entry.setDateOfBirths(new ArrayList<>(birthDates));

        return entry;
    }

    private static DocumentBuilder secureBuilder() throws Exception {

        // The list comes from outside: no DTDs, external entities or schema fetching (XXE)
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        return factory.newDocumentBuilder();
    }

    private static List<Element> children(Element root, String container, String tag) {
        List<Element> result = new ArrayList<>();
        for (Element group : direct(root, container)) {
            result.addAll(direct(group, tag));
        }
        return result;
    }

    private static List<Element> direct(Element parent, String tag) {
        List<Element> result = new ArrayList<>();
        NodeList nodes = parent.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element element && tag.equals(element.getTagName())) {
                result.add(element);
            }
        }
        return result;
    }

    private static String text(Element parent, String tag) {
        List<Element> found = direct(parent, tag);
        return found.isEmpty() ? null : StringUtils.trimToNull(found.get(0).getTextContent());
    }
}
