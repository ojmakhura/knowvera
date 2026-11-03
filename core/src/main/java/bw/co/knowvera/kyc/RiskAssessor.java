package bw.co.knowvera.kyc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.PepStatus;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationLegalForm;
import bw.co.knowvera.settings.Settings;

/**
 * Rates the money laundering risk of a customer from what their KYC record holds, as the
 * Financial Intelligence Act 2022 requires (s.16(2): due diligence follows the risk; s.21 and
 * Regulations r.11: what makes a customer high risk). Any high factor makes the customer high
 * risk; otherwise any medium factor makes them medium risk.
 */
@Component
public class RiskAssessor {

    /** Natural persons holding more than this share own a legal person (Regulations r.7). */
    public static final double BENEFICIAL_OWNERSHIP_THRESHOLD = 10.0;

    /** Roles that make a related party a beneficial owner whatever they hold (FI Act s.2). */
    private static final Set<RelatedPartyRole> OWNER_ROLES = EnumSet.of(RelatedPartyRole.BENEFICIAL_OWNER,
            RelatedPartyRole.SETTLOR, RelatedPartyRole.TRUSTEE, RelatedPartyRole.PROTECTOR,
            RelatedPartyRole.BENEFICIARY);

    /** Roles that act for the customer and must show their authority (FI Act s.20(2), Form A). */
    private static final Set<RelatedPartyRole> AGENT_ROLES = EnumSet.of(RelatedPartyRole.AGENT,
            RelatedPartyRole.AUTHORISED_SIGNATORY);

    private static final Set<String> BOTSWANA = Set.of("botswana", "motswana", "batswana", "bw", "bwa");

    public record Assessment(RiskLevel level, List<String> factors) {
    }

    private static final class Factors {

        private RiskLevel level = RiskLevel.LOW;
        private final List<String> reasons = new ArrayList<>();

        void add(RiskLevel factorLevel, String reason) {
            reasons.add(factorLevel + ": " + reason);
            if (factorLevel.ordinal() > level.ordinal()) {
                level = factorLevel;
            }
        }
    }

    /** Marks the related parties that are beneficial owners: more than 10% or an owner role. */
    public void markBeneficialOwners(Collection<RelatedParty> parties) {

        if (parties == null) {
            return;
        }

        for (RelatedParty party : parties) {
            boolean owns = party.getOwnershipPercentage() != null
                    && party.getOwnershipPercentage() > BENEFICIAL_OWNERSHIP_THRESHOLD;
            party.setBeneficialOwner(owns || OWNER_ROLES.contains(party.getRole()));
        }
    }

    /**
     * @param individual the record's owner when it is an individual, otherwise null
     * @param organisation the record's owner when it is an organisation, otherwise null
     */
    public Assessment assess(KycRecord record, Individual individual, Organisation organisation, Settings settings) {

        Factors factors = new Factors();
        Set<String> highRiskCountries = highRiskCountries(settings);

        if (Boolean.TRUE.equals(record.getSanctionsMatch())) {
            factors.add(RiskLevel.HIGH, "Declared sanctions match: no business relationship is permitted with a designated person (FI Act s.27)");
        }

        if (isPip(record.getPepStatus()) || (individual != null && isPip(individual.getPepStatus()))) {
            factors.add(RiskLevel.HIGH, "Prominent influential person, family member or close associate (FI Act s.22)");
        }

        if (individual != null) {
            country(factors, highRiskCountries, individual.getNationality(), "Nationality");

            if (individual.getIdentityType() == IndividualIdentityType.DRIVERS_LICENCE
                    || individual.getIdentityType() == IndividualIdentityType.BIRTH_CERTIFICATE) {
                factors.add(RiskLevel.MEDIUM, "Identified by a secondary or interim document (" + individual.getIdentityType() + ")");
            }
        }

        if (organisation != null) {
            country(factors, highRiskCountries, organisation.getCountryOfRegistration(), "Country of registration");

            if (organisation.getLegalForm() == OrganisationLegalForm.TRUST
                    || organisation.getLegalForm() == OrganisationLegalForm.OTHER) {
                factors.add(RiskLevel.MEDIUM, "Legal arrangement or structure that can hide ownership (Regulations r.11)");
            }

            List<RelatedParty> parties = record.getRelatedParties() == null ? List.of() : record.getRelatedParties();
            if (parties.stream().noneMatch(p -> Boolean.TRUE.equals(p.getBeneficialOwner()))) {
                factors.add(RiskLevel.MEDIUM, "No beneficial owner recorded: identify everyone holding more than 10%, or the senior managing official (Regulations r.7)");
            }
        }

        for (RelatedParty party : record.getRelatedParties() == null ? List.<RelatedParty>of() : record.getRelatedParties()) {
            String who = StringUtils.defaultIfBlank(party.getName(), "A related party");

            if (isPip(party.getPepStatus())) {
                factors.add(RiskLevel.HIGH, who + " (" + party.getRole() + ") is a prominent influential person (FI Act s.22)");
            }
            if (party.getNationality() != null && highRiskCountries.contains(normalise(party.getNationality()))) {
                factors.add(RiskLevel.HIGH, who + " (" + party.getRole() + ") is from a high-risk jurisdiction (FI Act s.21)");
            }
            if (AGENT_ROLES.contains(party.getRole()) && StringUtils.isBlank(party.getAuthorityReference())) {
                factors.add(RiskLevel.MEDIUM, who + " acts for the customer without proof of authority (FI Act s.20(2), Form A)");
            }
        }

        Collection<SourceOfFunds> funds = record.getSourceOfFunds() == null ? List.of() : record.getSourceOfFunds();
        List<SourceOfFunds> unexplained = funds.stream()
                .filter(f -> f == SourceOfFunds.GIFTS || f == SourceOfFunds.REMITTANCE || f == SourceOfFunds.OTHER)
                .toList();
        if (!unexplained.isEmpty() && StringUtils.isBlank(record.getSourceOfFundsDetails())) {
            factors.add(RiskLevel.MEDIUM, "Source of funds " + unexplained + " without an explanation");
        }

        return new Assessment(factors.level, factors.reasons);
    }

    /** Years between reviews for a customer of this risk (FI Act s.19, Regulations r.16). */
    public int reviewYears(RiskLevel level, Settings settings) {

        int low = settings != null && settings.getKycDuration() != null ? settings.getKycDuration() : 2;

        if (level == RiskLevel.HIGH) {
            return settings != null && settings.getHighRiskKycDuration() != null ? settings.getHighRiskKycDuration() : 1;
        }
        if (level == RiskLevel.MEDIUM) {
            return settings != null && settings.getMediumRiskKycDuration() != null
                    ? settings.getMediumRiskKycDuration()
                    : Math.min(low, 2);
        }
        return low;
    }

    /** The expiry date, brought forward when the customer's risk calls for an earlier review. */
    public LocalDate expiryFor(LocalDate uploadDate, LocalDate currentExpiry, RiskLevel level, Settings settings) {

        LocalDate due = uploadDate.plusYears(reviewYears(level, settings));
        return currentExpiry == null || due.isBefore(currentExpiry) ? due : currentExpiry;
    }

    private static boolean isPip(PepStatus status) {
        return status != null && status != PepStatus.NOT_PEP;
    }

    private static void country(Factors factors, Set<String> highRiskCountries, String country, String label) {

        if (StringUtils.isBlank(country)) {
            return;
        }
        if (highRiskCountries.contains(normalise(country))) {
            factors.add(RiskLevel.HIGH, label + " " + country + " is a high-risk jurisdiction (FI Act s.21)");
        } else if (!BOTSWANA.contains(normalise(country))) {
            factors.add(RiskLevel.MEDIUM, label + " " + country + " is outside Botswana");
        }
    }

    private static Set<String> highRiskCountries(Settings settings) {

        if (settings == null || settings.getHighRiskCountries() == null) {
            return Set.of();
        }
        return settings.getHighRiskCountries().stream()
                .filter(StringUtils::isNotBlank)
                .map(RiskAssessor::normalise)
                .collect(Collectors.toSet());
    }

    private static String normalise(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
