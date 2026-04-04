package bw.co.centralkyc.document.processor;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import bw.co.centralkyc.KeyField;
import bw.co.centralkyc.document.DataVerification;
import bw.co.centralkyc.document.type.verification.VerificationDataConfigDTO;
import bw.co.centralkyc.matcher.UniversalStringMatcher;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataVerificationService {

    private final UniversalStringMatcher stringMatcher;

    public DataVerification getIndividualAddressVerificationTagResult(VerificationDataConfigDTO config,
            Map<KeyField, List<String>> matched) {

        DataVerification dataVerification = new DataVerification();
    //     VerificationTagResult verificationTagResults = new VerificationTagResult();
    //     verificationTagResults.setVerificationTag(tag);

    //     StringBuilder expectedValues = new StringBuilder();
    //     StringBuilder extractedValues = new StringBuilder();

    //     List<String> foundPhysicalAddresses = matched.get(KeyField.INDIVIDUAL_PHYSICAL_ADDRESS);
    //     double physicalAddressSimilarity = 0.0;

    //     if (foundPhysicalAddresses != null && foundPhysicalAddresses.size() == 2) {
    //         expectedValues.append(foundPhysicalAddresses.get(0));
    //         extractedValues.append(foundPhysicalAddresses.get(1));
    //         physicalAddressSimilarity = stringMatcher.calculateFilteredSimilarity(foundPhysicalAddresses.get(0),
    //                 foundPhysicalAddresses.get(1));

    //     } else if (foundPhysicalAddresses != null && foundPhysicalAddresses.size() == 1) {
    //         expectedValues.append(foundPhysicalAddresses.get(0));
    //     }

    //     List<String> foundPostalAddresses = matched.get(KeyField.INDIVIDUAL_POSTAL_ADDRESS);
    //     double postalAddressSimilarity = 0.0;

    //     if (foundPostalAddresses != null && foundPostalAddresses.size() == 2) {
    //         expectedValues.append(' ').append(foundPostalAddresses.get(0));
    //         extractedValues.append(' ').append(foundPostalAddresses.get(1));
    //         postalAddressSimilarity = stringMatcher.calculateFilteredSimilarity(foundPostalAddresses.get(0),
    //                 foundPostalAddresses.get(1));

    //     } else if (foundPostalAddresses != null && foundPostalAddresses.size() == 1) {
    //         expectedValues.append(' ').append(foundPostalAddresses.get(0));
    //     }

    //     verificationTagResults.setScore((physicalAddressSimilarity + postalAddressSimilarity) / 2);

    //     if (verificationTagResults.getScore() > 0.8) {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

    //     } else {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
    //     }



    //     verificationTagResults.setValues(List.of(
    //             expectedValues.toString(),
    //             extractedValues.toString()));
    
        return dataVerification;
    }

    // public VerificationTagResult getOrganisationAddressTagResult(VerificationTag tag,
    //         Map<KeyField, List<String>> matched) {
    //     VerificationTagResult verificationTagResults = new VerificationTagResult();
    //     verificationTagResults.setVerificationTag(tag);

    //     StringBuilder expectedValues = new StringBuilder();
    //     StringBuilder extractedValues = new StringBuilder();

    //     List<String> foundPhysicalAddresses = matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS);
    //     double physicalAddressSimilarity = 0.0;

    //     if (foundPhysicalAddresses != null && foundPhysicalAddresses.size() == 2) {
    //         expectedValues.append(foundPhysicalAddresses.get(0)).append('\n');
    //         extractedValues.append(foundPhysicalAddresses.get(1)).append('\n');
    //         physicalAddressSimilarity = stringMatcher.calculateSimilarity(foundPhysicalAddresses.get(0),
    //                 foundPhysicalAddresses.get(1));

    //     } else if (foundPhysicalAddresses != null && foundPhysicalAddresses.size() == 1) {
    //         expectedValues.append(foundPhysicalAddresses.get(0)).append('\n');
    //     }

    //     List<String> foundPostalAddresses = matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS);
    //     double postalAddressSimilarity = 0.0;

    //     if (foundPostalAddresses != null && foundPostalAddresses.size() == 2) {
    //         expectedValues.append(' ').append(foundPostalAddresses.get(0)).append('\n');
    //         extractedValues.append(' ').append(foundPostalAddresses.get(1)).append('\n');
    //         postalAddressSimilarity = stringMatcher.calculateSimilarity(foundPostalAddresses.get(0),
    //                 foundPostalAddresses.get(1));

    //     } else if (foundPostalAddresses != null && foundPostalAddresses.size() == 1) {
    //         expectedValues.append(' ').append(foundPostalAddresses.get(0)).append('\n');
    //     }

    //     if (physicalAddressSimilarity > 0.8 && postalAddressSimilarity > 0.8) {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

    //     } else {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
    //     }

    //     verificationTagResults.setScore((physicalAddressSimilarity + postalAddressSimilarity) / 2);

    //     verificationTagResults.setValues(List.of(
    //             expectedValues.toString(),
    //             extractedValues.toString()));

    //     return verificationTagResults;
    // }

    // public VerificationTagResult getOrganisationVerificationResult(VerificationTag tag,
    //         Map<KeyField, List<String>> matched) {
    //     VerificationTagResult verificationTagResults = new VerificationTagResult();
    //     verificationTagResults.setVerificationTag(tag);

    //     StringBuilder expectedValues = new StringBuilder();
    //     StringBuilder extractedValues = new StringBuilder();

    //     List<String> foundRegistrationNos = matched.get(KeyField.ORGANISATION_REGISTRATION_NO);
    //     double registrationNoSimilarity = 0.0;

    //     if (foundRegistrationNos != null && foundRegistrationNos.size() == 2) {
    //         expectedValues.append(foundRegistrationNos.get(0));
    //         extractedValues.append(foundRegistrationNos.get(1));
    //         registrationNoSimilarity = stringMatcher.calculateSimilarity(foundRegistrationNos.get(0),
    //                 foundRegistrationNos.get(1));

    //     } else if (foundRegistrationNos != null && foundRegistrationNos.size() == 1) {
    //         expectedValues.append(foundRegistrationNos.get(0));
    //     }

    //     List<String> foundOrganisationNames = matched.get(KeyField.ORGANISATION_NAME);
    //     double organisationNameSimilarity = 0.0;

    //     if (foundOrganisationNames != null && foundOrganisationNames.size() == 2) {
    //         expectedValues.append(' ').append(foundOrganisationNames.get(0));
    //         extractedValues.append(' ').append(foundOrganisationNames.get(1));
    //         organisationNameSimilarity = stringMatcher.calculateSimilarity(foundOrganisationNames.get(0),
    //                 foundOrganisationNames.get(1));

    //     } else if (foundOrganisationNames != null && foundOrganisationNames.size() == 1) {
    //         expectedValues.append(' ').append(foundOrganisationNames.get(0));
    //     }

    //     List<String> foundOrganisationAbbreviations = matched.get(KeyField.ORGANISATION_ABBREVIATION);
    //     if (foundOrganisationAbbreviations != null && foundOrganisationAbbreviations.size() == 2) {
    //         expectedValues.append(' ').append(foundOrganisationAbbreviations.get(0));
    //         extractedValues.append(' ').append(foundOrganisationAbbreviations.get(1));
    //     } else if (foundOrganisationAbbreviations != null && foundOrganisationAbbreviations.size() == 1) {
    //         expectedValues.append(' ').append(foundOrganisationAbbreviations.get(0));
    //     }

    //     List<String> foundOrganisationPhysicalAddresses = matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS);
    //     if (foundOrganisationPhysicalAddresses != null && foundOrganisationPhysicalAddresses.size() == 2) {
    //         expectedValues.append(' ').append(foundOrganisationPhysicalAddresses.get(0));
    //         extractedValues.append(' ').append(foundOrganisationPhysicalAddresses.get(1));
    //     } else if (foundOrganisationPhysicalAddresses != null && foundOrganisationPhysicalAddresses.size() == 1) {
    //         expectedValues.append(' ').append(foundOrganisationPhysicalAddresses.get(0));
    //     }

    //     List<String> foundOrganisationPostalAddresses = matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS);
    //     if (foundOrganisationPostalAddresses != null && foundOrganisationPostalAddresses.size() == 2) {
    //         expectedValues.append(' ').append(foundOrganisationPostalAddresses.get(0));
    //         extractedValues.append(' ').append(foundOrganisationPostalAddresses.get(1));
    //     } else if (foundOrganisationPostalAddresses != null && foundOrganisationPostalAddresses.size() == 1) {
    //         expectedValues.append(' ').append(foundOrganisationPostalAddresses.get(0));
    //     }

    //     List<String> foundOrganisationContacts = matched.get(KeyField.ORGANISATION_CONTACT);
    //     if (foundOrganisationContacts != null && foundOrganisationContacts.size() == 2) {
    //         expectedValues.append(' ').append(foundOrganisationContacts.get(0));
    //         extractedValues.append(' ').append(foundOrganisationContacts.get(1));
    //     } else if (foundOrganisationContacts != null && foundOrganisationContacts.size() == 1) {
    //         expectedValues.append(' ').append(foundOrganisationContacts.get(0));
    //     }

    //     if (registrationNoSimilarity > 0.8 && organisationNameSimilarity > 0.8) {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

    //     } else {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
    //     }

    //     verificationTagResults.setScore((registrationNoSimilarity + organisationNameSimilarity) / 2);

    //     verificationTagResults.setValues(List.of(
    //             expectedValues.toString(),
    //             extractedValues.toString()));

    //     return verificationTagResults;
    // }

    // public VerificationTagResult getIndividualIdentityVerificationResult(VerificationTag tag,
    //         Map<KeyField, List<String>> matched) {

    //     StringBuilder expectedValues = new StringBuilder();
    //     StringBuilder extractedValues = new StringBuilder();

    //     VerificationTagResult verificationTagResults = new VerificationTagResult();
    //     verificationTagResults.setVerificationTag(tag);

    //     List<String> foundIdentityNos = matched.get(KeyField.INDIVIDUAL_IDENTITY_NO);

    //     double idSimilarity = 0.0;

    //     if (foundIdentityNos != null && foundIdentityNos.size() == 2) {
    //         expectedValues.append(foundIdentityNos.get(0));
    //         extractedValues.append(foundIdentityNos.get(1));

    //         idSimilarity = stringMatcher.calculateSimilarity(foundIdentityNos.get(0), foundIdentityNos.get(1));

    //     } else if (foundIdentityNos != null && foundIdentityNos.size() == 1) {
    //         expectedValues.append(foundIdentityNos.get(0));
    //     }

    //     List<String> foundIdentityTypes = matched.get(KeyField.INDIVIDUAL_IDENTITY_TYPE);
    //     double typeSimilarity = 0.0;
    //     // ? stringMatcher.calculateSimilarity(foundIdentityTypes.get(0),
    //     // foundIdentityTypes.get(1))
    //     // : 0.0;

    //     if (foundIdentityTypes != null && foundIdentityTypes.size() == 2) {
    //         expectedValues.append(' ').append(foundIdentityTypes.get(0));
    //         extractedValues.append(' ').append(foundIdentityTypes.get(1));
    //         typeSimilarity = stringMatcher.calculateSimilarity(foundIdentityTypes.get(0), foundIdentityTypes.get(1));

    //     } else if (foundIdentityTypes != null && foundIdentityTypes.size() == 1) {
    //         expectedValues.append(' ').append(foundIdentityTypes.get(0));
    //     }

    //     List<String> foundFirstNames = matched.get(KeyField.INDIVIDUAL_FIRST_NAME);
    //     double firstNameSimilarity = foundFirstNames != null && foundFirstNames.size() == 2
    //             ? stringMatcher.calculateSimilarity(foundFirstNames.get(0), foundFirstNames.get(1))
    //             : 0.0;

    //     List<String> foundMiddleNames = matched.get(KeyField.INDIVIDUAL_MIDDLE_NAME);
    //     double middleNameSimilarity = 0.0;
    //     // ? stringMatcher.calculateSimilarity(foundMiddleNames.get(0),
    //     // foundMiddleNames.get(1))
    //     // : 0.0;

    //     if (foundMiddleNames != null && foundMiddleNames.size() == 2) {
    //         expectedValues.append(' ').append(foundMiddleNames.get(0));
    //         extractedValues.append(' ').append(foundMiddleNames.get(1));
    //         middleNameSimilarity = stringMatcher.calculateSimilarity(foundMiddleNames.get(0), foundMiddleNames.get(1));

    //     } else if (foundMiddleNames != null && foundMiddleNames.size() == 1) {
    //         expectedValues.append(' ').append(foundMiddleNames.get(0));
    //     }

    //     List<String> foundSurnames = matched.get(KeyField.INDIVIDUAL_SURNAME);
    //     double surnameSimilarity = 0.0;

    //     if (foundSurnames != null && foundSurnames.size() == 2) {
    //         expectedValues.append(' ').append(foundSurnames.get(0));
    //         extractedValues.append(' ').append(foundSurnames.get(1));
    //         surnameSimilarity = stringMatcher.calculateSimilarity(foundSurnames.get(0), foundSurnames.get(1));

    //     } else if (foundSurnames != null && foundSurnames.size() == 1) {
    //         expectedValues.append(' ').append(foundSurnames.get(0));
    //     }

    //     if (idSimilarity > 0.8 && firstNameSimilarity > 0.8 && surnameSimilarity > 0.8) {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

    //     } else {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
    //     }

    //     verificationTagResults.setScore((idSimilarity + firstNameSimilarity + surnameSimilarity) / 3);

    //     verificationTagResults.setValues(List.of(
    //             expectedValues.toString(),
    //             extractedValues.toString()));

    //     return verificationTagResults;
    // }

    // public VerificationTagResult getEmploymentVerificationTagResult(VerificationTag tag,
    //         Map<KeyField, List<String>> matched) {

    //     StringBuilder expectedValues = new StringBuilder();
    //     StringBuilder extractedValues = new StringBuilder();

    //     VerificationTagResult verificationTagResults = new VerificationTagResult();
    //     verificationTagResults.setVerificationTag(tag);

    //     verificationTagResults.setVerificationTagStatus(VerificationTagStatus.UNCHECKED);

    //     double similarity = 0.0;

    //     List<String> foundEmploymentPositions = matched.get(KeyField.EMPLOYMENT_POSITION);
    //     boolean hasEmploymentPosition = foundEmploymentPositions != null && !foundEmploymentPositions.isEmpty();

    //     if (hasEmploymentPosition) {
    //         extractedValues.append("Position: ")
    //                 .append(foundEmploymentPositions.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundOrganisationNames = matched.get(KeyField.ORGANISATION_NAME);

    //     boolean hasOrganisationName = foundOrganisationNames != null && !foundOrganisationNames.isEmpty();

    //     if (hasOrganisationName) {
    //         extractedValues.append("Organisation: ")
    //                 .append(foundOrganisationNames.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundOrganisationAbbreviations = matched.get(KeyField.ORGANISATION_ABBREVIATION);
    //     boolean hasOrganisationAbbreviation = foundOrganisationAbbreviations != null
    //             && !foundOrganisationAbbreviations.isEmpty();

    //     if (hasOrganisationAbbreviation) {
    //         extractedValues.append("Abbreviation: ")
    //                 .append(foundOrganisationAbbreviations.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundOrganisationPhysicalAddresses = matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS);
    //     boolean hasOrganisationPhysicalAddress = foundOrganisationPhysicalAddresses != null
    //             && !foundOrganisationPhysicalAddresses.isEmpty();

    //     if (hasOrganisationPhysicalAddress) {
    //         extractedValues.append("Physical Address: ")
    //                 .append(foundOrganisationPhysicalAddresses.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundOrganisationPostalAddresses = matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS);
    //     boolean hasOrganisationPostalAddress = foundOrganisationPostalAddresses != null
    //             && !foundOrganisationPostalAddresses.isEmpty();

    //     if (hasOrganisationPostalAddress) {
    //         extractedValues.append("Postal Address: ")
    //                 .append(foundOrganisationPostalAddresses.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundOrganisationContacts = matched.get(KeyField.ORGANISATION_CONTACT);
    //     boolean hasOrganisationContact = foundOrganisationContacts != null && !foundOrganisationContacts.isEmpty();

    //     if (hasOrganisationContact) {
    //         extractedValues.append("Contact: ")
    //                 .append(foundOrganisationContacts.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundEmploymentStartDates = matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_START);
    //     boolean hasEmploymentStartDate = foundEmploymentStartDates != null && !foundEmploymentStartDates.isEmpty();

    //     if (hasEmploymentStartDate) {
    //         extractedValues.append("Employment Start Date: ")
    //                 .append(foundEmploymentStartDates.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     List<String> foundEmploymentEndDates = matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_END);
    //     boolean hasEmploymentEndDate = foundEmploymentEndDates != null && !foundEmploymentEndDates.isEmpty();

    //     if (hasEmploymentEndDate) {
    //         extractedValues.append("Employment End Date: ")
    //                 .append(foundEmploymentEndDates.get(0))
    //                 .append('\n');
    //         similarity = similarity + 1;
    //     }

    //     if (hasEmploymentPosition && hasOrganisationName) {
    //         verificationTagResults.setScore(1.0);
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

    //     } else {
    //         verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
    //         verificationTagResults.setScore(0.0);
    //     }

    //     verificationTagResults.setValues(List.of(
    //             extractedValues.toString()));

    //     return verificationTagResults;
    // }

}
