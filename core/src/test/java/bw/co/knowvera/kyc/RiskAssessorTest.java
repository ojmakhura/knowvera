package bw.co.knowvera.kyc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import bw.co.knowvera.SourceOfFunds;
import bw.co.knowvera.individual.Individual;
import bw.co.knowvera.individual.IndividualIdentityType;
import bw.co.knowvera.individual.PepStatus;
import bw.co.knowvera.organisation.Organisation;
import bw.co.knowvera.organisation.OrganisationLegalForm;
import bw.co.knowvera.settings.Settings;

class RiskAssessorTest {

    private final RiskAssessor assessor = new RiskAssessor();

    private static KycRecord record() {
        KycRecord record = KycRecord.Factory.newInstance();
        record.setPepStatus(PepStatus.NOT_PEP);
        record.setSanctionsMatch(false);
        record.setSourceOfFunds(List.of(SourceOfFunds.SALARY));
        record.setRelatedParties(new ArrayList<>());
        return record;
    }

    private static Individual motswana() {
        Individual individual = Individual.Factory.newInstance();
        individual.setNationality("Motswana");
        individual.setIdentityType(IndividualIdentityType.OMANG);
        individual.setPepStatus(PepStatus.NOT_PEP);
        return individual;
    }

    private static RelatedParty party(RelatedPartyRole role, Double ownership) {
        RelatedParty party = new RelatedParty();
        party.setName("Kabo Seretse");
        party.setRole(role);
        party.setOwnershipPercentage(ownership);
        party.setPepStatus(PepStatus.NOT_PEP);
        return party;
    }

    @Test
    void aResidentSalariedCitizenIsLowRisk() {
        RiskAssessor.Assessment assessment = assessor.assess(record(), motswana(), null, Settings.Factory.newInstance());

        assertEquals(RiskLevel.LOW, assessment.level());
        assertTrue(assessment.factors().isEmpty());
    }

    @Test
    void aProminentInfluentialPersonIsHighRisk() {
        Individual individual = motswana();
        individual.setPepStatus(PepStatus.PEP_RELATIVE);

        assertEquals(RiskLevel.HIGH, assessor.assess(record(), individual, null, Settings.Factory.newInstance()).level());
    }

    @Test
    void aDeclaredSanctionsMatchIsHighRisk() {
        KycRecord record = record();
        record.setSanctionsMatch(true);

        assertEquals(RiskLevel.HIGH, assessor.assess(record, motswana(), null, Settings.Factory.newInstance()).level());
    }

    @Test
    void aForeignNationalIsMediumRiskAndAHighRiskJurisdictionIsHigh() {
        Individual individual = motswana();
        individual.setNationality("Zimbabwe");
        Settings settings = Settings.Factory.newInstance();

        assertEquals(RiskLevel.MEDIUM, assessor.assess(record(), individual, null, settings).level());

        settings.setHighRiskCountries(List.of("Zimbabwe"));
        assertEquals(RiskLevel.HIGH, assessor.assess(record(), individual, null, settings).level());
    }

    @Test
    void unexplainedGiftsAreMediumRiskUntilExplained() {
        KycRecord record = record();
        record.setSourceOfFunds(List.of(SourceOfFunds.GIFTS));

        assertEquals(RiskLevel.MEDIUM, assessor.assess(record, motswana(), null, Settings.Factory.newInstance()).level());

        record.setSourceOfFundsDetails("Wedding gift from parents");
        assertEquals(RiskLevel.LOW, assessor.assess(record, motswana(), null, Settings.Factory.newInstance()).level());
    }

    @Test
    void holdingMoreThanTenPercentOrAnOwnerRoleMakesABeneficialOwner() {
        RelatedParty tenPercent = party(RelatedPartyRole.SHAREHOLDER, 10.0);
        RelatedParty eleven = party(RelatedPartyRole.SHAREHOLDER, 10.5);
        RelatedParty trustee = party(RelatedPartyRole.TRUSTEE, null);

        assessor.markBeneficialOwners(List.of(tenPercent, eleven, trustee));

        assertFalse(tenPercent.getBeneficialOwner());
        assertTrue(eleven.getBeneficialOwner());
        assertTrue(trustee.getBeneficialOwner());
    }

    @Test
    void anOrganisationWithoutBeneficialOwnersIsMediumRisk() {
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCountryOfRegistration("Botswana");
        organisation.setLegalForm(OrganisationLegalForm.PRIVATE_COMPANY);
        KycRecord record = record();

        assertEquals(RiskLevel.MEDIUM, assessor.assess(record, null, organisation, Settings.Factory.newInstance()).level());

        record.getRelatedParties().add(party(RelatedPartyRole.SHAREHOLDER, 60.0));
        assessor.markBeneficialOwners(record.getRelatedParties());
        assertEquals(RiskLevel.LOW, assessor.assess(record, null, organisation, Settings.Factory.newInstance()).level());
    }

    @Test
    void aBeneficialOwnerWhoIsAProminentInfluentialPersonMakesTheCustomerHighRisk() {
        Organisation organisation = Organisation.Factory.newInstance();
        organisation.setCountryOfRegistration("Botswana");
        KycRecord record = record();
        RelatedParty owner = party(RelatedPartyRole.SHAREHOLDER, 60.0);
        owner.setPepStatus(PepStatus.PEP_SELF);
        record.getRelatedParties().add(owner);
        assessor.markBeneficialOwners(record.getRelatedParties());

        assertEquals(RiskLevel.HIGH, assessor.assess(record, null, organisation, Settings.Factory.newInstance()).level());
    }

    @Test
    void anAgentWithoutProofOfAuthorityIsMediumRisk() {
        KycRecord record = record();
        RelatedParty agent = party(RelatedPartyRole.AGENT, null);
        record.getRelatedParties().add(agent);

        assertEquals(RiskLevel.MEDIUM, assessor.assess(record, motswana(), null, Settings.Factory.newInstance()).level());

        agent.setAuthorityReference("Form A signed 2026-10-01");
        assertEquals(RiskLevel.LOW, assessor.assess(record, motswana(), null, Settings.Factory.newInstance()).level());
    }

    @Test
    void higherRiskCustomersAreReviewedSooner() {
        Settings settings = Settings.Factory.newInstance();
        settings.setKycDuration(3);
        LocalDate uploaded = LocalDate.of(2026, 1, 1);

        assertEquals(3, assessor.reviewYears(RiskLevel.LOW, settings));
        assertEquals(2, assessor.reviewYears(RiskLevel.MEDIUM, settings));
        assertEquals(1, assessor.reviewYears(RiskLevel.HIGH, settings));

        // The review is brought forward, never pushed back
        assertEquals(LocalDate.of(2027, 1, 1),
                assessor.expiryFor(uploaded, LocalDate.of(2029, 1, 1), RiskLevel.HIGH, settings));
        assertEquals(LocalDate.of(2026, 6, 1),
                assessor.expiryFor(uploaded, LocalDate.of(2026, 6, 1), RiskLevel.LOW, settings));
    }
}
