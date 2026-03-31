package bw.co.centralkyc.document.processor;

import bw.co.centralkyc.KeyField;
import bw.co.centralkyc.QueueObject;
import bw.co.centralkyc.TargetEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import bw.co.centralkyc.document.DocumentAnalyticsStatus;
import bw.co.centralkyc.document.DocumentDTO;
import bw.co.centralkyc.document.DocumentService;
import bw.co.centralkyc.document.VerificationTagResult;
import bw.co.centralkyc.document.VerificationTagStatus;
import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.individual.Individual;
import bw.co.centralkyc.individual.IndividualRepository;
import bw.co.centralkyc.kyc.KycRecord;
import bw.co.centralkyc.kyc.KycRecordRepository;
import bw.co.centralkyc.kyc.verification.VerificationTag;
import bw.co.centralkyc.matcher.UniversalStringMatcher;
import bw.co.centralkyc.organisation.Organisation;
import bw.co.centralkyc.organisation.OrganisationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformationConfirmationService {

    private final KycRecordRepository kycRecordRepository;
    private final IndividualRepository individualRepository;
    private final OrganisationRepository organisationRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final DocumentService documentService;
    private final JsonMapper jsonMapper;
    private final UniversalStringMatcher stringMatcher;

    @RabbitListener(queues = "${app.rabbitmq.informationConfirmationQueue}")
    public void handleInformationConfirmation(QueueObject queueObject) {
        // Implement your logic to process the extracted information here
        // For example, you could perform validation, enrichment, or trigger further
        // workflows based on the extracted data

        log.info("Processing extracted information for document ID: {}", queueObject.documentId());
        DocumentDTO document = documentService.findById(queueObject.documentId());
        if (document != null) {
            // Example: Log the extracted information
            log.info("Extracted Information for Document ID {}: {}", queueObject.documentId(),
                    document.getExtractedInformation());
        } else {
            log.warn("Document not found for ID: {}", queueObject.documentId());
        }

        switch (document.getTarget()) {
            case KYC_RECORD -> processKycRecordInformation(document);
            case INDIVIDUAL -> processIndividualInformation(document);
            case ORGANISATION -> processOrganisationInformation(document);
            // Add more cases here for different targets as needed
            default -> log.warn("Unknown target type: {}", document.getTarget());
        }

        document.setAnalyticsStatus(DocumentAnalyticsStatus.INFORMATION_CONFIRMATION_COMPLETE);

        document = documentService.save(document);

    }

    private Map<KeyField, List<String>> createMatchMap() {

        Map<KeyField, List<String>> matched = new HashMap<>();

        matched.put(KeyField.INDIVIDUAL_FIRST_NAME, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_MIDDLE_NAME, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_SURNAME, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_IDENTITY_NO, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_IDENTITY_TYPE, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_POSTAL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_PHYSICAL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_EMAIL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_SEX, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_NATIONALITY, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_EMPLOYMENT_STATUS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_PEP_STATUS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_MARITAL_STATUS, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_EMPLOYMENT_START, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_EMPLOYMENT_END, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_NAME, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_REGISTRATION_NO, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_PHONE_NUMBER, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_PHYSICAL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_POSTAL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_REGISTRATION_COUNTRY, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_EMAIL_ADDRESS, new java.util.ArrayList<>());
        matched.put(KeyField.CONTRACT_START, new java.util.ArrayList<>());
        matched.put(KeyField.CONTRACT_END, new java.util.ArrayList<>());
        matched.put(KeyField.EMPLOYMENT_POSITION, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_CONTACT, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_CONTACT_NUMBER, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_DOMAIN, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_CONTACT_EMAIL, new java.util.ArrayList<>());
        matched.put(KeyField.INDIVIDUAL_TITLE, new java.util.ArrayList<>());
        matched.put(KeyField.ORGANISATION_ABBREVIATION, new java.util.ArrayList<>());
        matched.put(KeyField.DOCUMENT_DATE, new java.util.ArrayList<>());

        return matched;

    }

    private void readIndividualMatches(Individual individual, Map<KeyField, List<String>> matched) {

        matched.get(KeyField.INDIVIDUAL_FIRST_NAME).add(individual.getFirstName());
        matched.get(KeyField.INDIVIDUAL_MIDDLE_NAME).add(individual.getMiddleName());
        matched.get(KeyField.INDIVIDUAL_SURNAME).add(individual.getSurname());
        matched.get(KeyField.INDIVIDUAL_IDENTITY_NO).add(individual.getIdentityNo());
        matched.get(KeyField.INDIVIDUAL_IDENTITY_TYPE).add(individual.getIdentityType().getValue());
        matched.get(KeyField.INDIVIDUAL_POSTAL_ADDRESS).add(individual.getPostalAddress());
        matched.get(KeyField.INDIVIDUAL_PHYSICAL_ADDRESS).add(individual.getPhysicalAddress());
        matched.get(KeyField.INDIVIDUAL_EMAIL_ADDRESS).add(individual.getEmailAddress());
        matched.get(KeyField.INDIVIDUAL_SEX).add(individual.getSex().getValue());
        matched.get(KeyField.INDIVIDUAL_NATIONALITY).add(individual.getNationality());
        matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_STATUS).add(individual.getEmploymentStatus().getValue());

        if (individual.getPepStatus() != null) {
            matched.get(KeyField.INDIVIDUAL_PEP_STATUS).add(individual.getPepStatus().getValue());
        }
        matched.get(KeyField.INDIVIDUAL_MARITAL_STATUS).add(individual.getMaritalStatus().getValue());
        // matched.get(KeyField.INDIV

    }

    private void readOrganisationMatches(Organisation organisation, Map<KeyField, List<String>> matched) {

        matched.get(KeyField.ORGANISATION_NAME).add(organisation.getName());
        matched.get(KeyField.ORGANISATION_REGISTRATION_NO).add(organisation.getRegistrationNo());
        // matched.get(KeyField.ORGANISATION_PHONE_NUMBER).add(organisation.getPhoneNumbers().stream().reduce((a,
        // b) -> a + ", " + b));
        matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS).add(organisation.getPhysicalAddress());
        matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS).add(organisation.getPostalAddress());
        // matched.get(KeyField.ORGANISATION_REGISTRATION_COUNTRY).add(organisation.getRegistrationCountry());
        // matched.get(KeyField.ORGANISATION_EMAIL_ADDRESS).add(organisation.getEmailAddress());

        // matched.get(KeyField.ORGANISATION_DOMAIN).add(organisation.getDomain());
        matched.get(KeyField.ORGANISATION_ABBREVIATION).add(organisation.getCode());

    }

    private void processKycRecordInformation(DocumentDTO document) {

        KycRecord kycRecord = kycRecordRepository.findById(UUID.fromString(document.getTargetId()))
                .orElseThrow(() -> new RuntimeException("KYC Record not found for ID: " + document.getTargetId()));

        DocumentType type = documentTypeRepository.findById(UUID.fromString(document.getDocumentTypeId()))
                .orElseThrow(
                        () -> new RuntimeException("Document Type not found for ID: " + document.getDocumentTypeId()));

        Map<KeyField, List<String>> matched = createMatchMap();

        if (kycRecord.getTarget() == TargetEntity.INDIVIDUAL) {

            Individual individual = individualRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(() -> new RuntimeException("Individual not found for ID: " + kycRecord.getTargetId()));

            this.readIndividualMatches(individual, matched);

        } else if (kycRecord.getTarget() == TargetEntity.ORGANISATION) {

            Organisation organisation = organisationRepository.findById(UUID.fromString(kycRecord.getTargetId()))
                    .orElseThrow(
                            () -> new RuntimeException("Organisation not found for ID: " + kycRecord.getTargetId()));
            this.readOrganisationMatches(organisation, matched);

        }

        type.getExpectedFields().forEach(expectedField -> {

            switch (expectedField.getKeyField()) {

                case INDIVIDUAL_FIRST_NAME:
                    String firstName = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(firstName)) {
                        matched.get(KeyField.INDIVIDUAL_FIRST_NAME).add(firstName);
                    }

                    break;
                case INDIVIDUAL_MIDDLE_NAME:
                    String middleName = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(middleName)) {
                        matched.get(KeyField.INDIVIDUAL_MIDDLE_NAME).add(middleName);
                    }
                    break;
                case INDIVIDUAL_SURNAME:
                    String surname = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(surname)) {
                        matched.get(KeyField.INDIVIDUAL_SURNAME).add(surname);
                    }
                    break;
                case INDIVIDUAL_IDENTITY_NO:
                    String identityNo = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(identityNo)) {
                        matched.get(KeyField.INDIVIDUAL_IDENTITY_NO).add(identityNo);
                    }
                    break;
                case INDIVIDUAL_IDENTITY_TYPE:
                    String identityType = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(identityType)) {
                        matched.get(KeyField.INDIVIDUAL_IDENTITY_TYPE).add(identityType);
                    }
                    break;
                case INDIVIDUAL_POSTAL_ADDRESS:
                    String postalAddress = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(postalAddress)) {
                        matched.get(KeyField.INDIVIDUAL_POSTAL_ADDRESS).add(postalAddress);
                    }
                    break;
                case INDIVIDUAL_PHYSICAL_ADDRESS:
                    String physicalAddress = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(physicalAddress)) {
                        matched.get(KeyField.INDIVIDUAL_PHYSICAL_ADDRESS).add(physicalAddress);
                    }
                    break;
                case INDIVIDUAL_EMAIL_ADDRESS:
                    String emailAddress = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(emailAddress)) {
                        matched.get(KeyField.INDIVIDUAL_EMAIL_ADDRESS).add(emailAddress);
                    }
                    break;
                case INDIVIDUAL_SEX:
                    String sex = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(sex)) {
                        matched.get(KeyField.INDIVIDUAL_SEX).add(sex);
                    }
                    break;
                case INDIVIDUAL_NATIONALITY:
                    String nationality = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(nationality)) {
                        matched.get(KeyField.INDIVIDUAL_NATIONALITY).add(nationality);
                    }

                    break;
                case INDIVIDUAL_EMPLOYMENT_STATUS:
                    String employmentStatus = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(employmentStatus)) {
                        matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_STATUS).add(employmentStatus);
                    }
                    break;
                case INDIVIDUAL_PEP_STATUS:
                    String pepStatus = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(pepStatus)) {
                        matched.get(KeyField.INDIVIDUAL_PEP_STATUS).add(pepStatus);
                    }
                    break;
                case INDIVIDUAL_MARITAL_STATUS:
                    String maritalStatus = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(maritalStatus)) {
                        matched.get(KeyField.INDIVIDUAL_MARITAL_STATUS).add(maritalStatus);
                    }
                    break;
                case INDIVIDUAL_EMPLOYMENT_START:
                    String employmentStart = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(employmentStart)) {
                        matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_START).add(employmentStart);
                    }
                    break;
                case INDIVIDUAL_EMPLOYMENT_END:
                    String employmentEnd = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(employmentEnd)) {
                        matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_END).add(employmentEnd);
                    }
                    break;
                case ORGANISATION_NAME:
                    String organisationName = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(organisationName)) {
                        matched.get(KeyField.ORGANISATION_NAME).add(organisationName);
                    }
                    break;
                case ORGANISATION_REGISTRATION_NO:

                    String registrationNo = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(registrationNo)) {
                        matched.get(KeyField.ORGANISATION_REGISTRATION_NO).add(registrationNo);
                    }

                    break;
                case ORGANISATION_PHONE_NUMBER:

                    String phoneNumber = (String) document.getExtractedInformation().get(expectedField.getField());

                    if (StringUtils.isNotBlank(phoneNumber)) {
                        matched.get(KeyField.ORGANISATION_PHONE_NUMBER).add(phoneNumber);
                    }

                    break;
                case ORGANISATION_PHYSICAL_ADDRESS:
                    String orgPhysicalAddress = (String) document.getExtractedInformation()
                            .get(expectedField.getField());

                    if (StringUtils.isNotBlank(orgPhysicalAddress)) {
                        matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS).add(orgPhysicalAddress);
                    }

                    break;
                case ORGANISATION_POSTAL_ADDRESS:

                    String orgPostalAddress = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(orgPostalAddress)) {
                        matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS).add(orgPostalAddress);
                    }

                    break;
                case ORGANISATION_REGISTRATION_COUNTRY:

                    String registrationCountry = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(registrationCountry)) {
                        matched.get(KeyField.ORGANISATION_REGISTRATION_COUNTRY).add(registrationCountry);
                    }

                    break;
                case ORGANISATION_EMAIL_ADDRESS:

                    String orgEmailAddress = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(orgEmailAddress)) {
                        matched.get(KeyField.ORGANISATION_EMAIL_ADDRESS).add(orgEmailAddress);
                    }

                    break;
                case CONTRACT_START:

                    String contractStart = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(contractStart)) {
                        matched.get(KeyField.CONTRACT_START).add(contractStart);
                    }

                    break;
                case CONTRACT_END:

                    String contractEnd = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(contractEnd)) {
                        matched.get(KeyField.CONTRACT_END).add(contractEnd);
                    }

                    break;
                case EMPLOYMENT_POSITION:

                    String employmentPosition = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(employmentPosition)) {
                        matched.get(KeyField.EMPLOYMENT_POSITION).add(employmentPosition);
                    }

                    break;
                case ORGANISATION_CONTACT:

                    String organisationContact = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(organisationContact)) {
                        matched.get(KeyField.ORGANISATION_CONTACT).add(organisationContact);
                    }

                    break;
                case ORGANISATION_CONTACT_NUMBER:

                    String organisationContactNumber = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(organisationContactNumber)) {
                        matched.get(KeyField.ORGANISATION_CONTACT_NUMBER).add(organisationContactNumber);
                    }

                    break;
                case ORGANISATION_DOMAIN:

                    String organisationDomain = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(organisationDomain)) {
                        matched.get(KeyField.ORGANISATION_DOMAIN).add(organisationDomain);
                    }

                    break;
                case ORGANISATION_CONTACT_EMAIL:

                    String organisationContactEmail = (String) document.getExtractedInformation()
                            .get(expectedField.getField());
                    if (StringUtils.isNotBlank(organisationContactEmail)) {
                        matched.get(KeyField.ORGANISATION_CONTACT_EMAIL).add(organisationContactEmail);
                    }

                    break;
                case INDIVIDUAL_TITLE:

                    String individualTitle = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(individualTitle)) {
                        matched.get(KeyField.INDIVIDUAL_TITLE).add(individualTitle);
                    }

                    break;
                case ORGANISATION_ABBREVIATION:

                    String abbreviation = (String) document.getExtractedInformation().get(expectedField.getField());
                    if (StringUtils.isNotBlank(abbreviation)) {
                        matched.get(KeyField.ORGANISATION_ABBREVIATION).add(abbreviation);
                    }

                    break;
                case DOCUMENT_DATE:
                    break;

            }

        });

        System.out.println(jsonMapper.writeValueAsString(kycRecord));
    }

    private VerificationTagResult getIndividualIdentityVerificationResult(VerificationTag tag, Map<KeyField, List<String>> matched) {
        
        StringBuilder expectedValues = new StringBuilder();
        StringBuilder extractedValues = new StringBuilder();

        VerificationTagResult verificationTagResults = new VerificationTagResult();
        verificationTagResults.setVerificationTag(tag);

        List<String> foundIdentityNos = matched.get(KeyField.INDIVIDUAL_IDENTITY_NO);

        double idSimilarity = 0.0;
                // ? stringMatcher.calculateSimilarity(foundIdentityNos.get(0), foundIdentityNos.get(1))
                // : 0.0;

        if(foundIdentityNos != null && foundIdentityNos.size() == 2) {
            expectedValues.append(foundIdentityNos.get(0));
            extractedValues.append(foundIdentityNos.get(1));

            idSimilarity = stringMatcher.calculateSimilarity(foundIdentityNos.get(0), foundIdentityNos.get(1));

        } else if(foundIdentityNos != null && foundIdentityNos.size() == 1) {
            expectedValues.append(foundIdentityNos.get(0));
        }
        

        List<String> foundIdentityTypes = matched.get(KeyField.INDIVIDUAL_IDENTITY_TYPE);
        double typeSimilarity = 0.0;
                // ? stringMatcher.calculateSimilarity(foundIdentityTypes.get(0),
                //         foundIdentityTypes.get(1))
                // : 0.0;

        if(foundIdentityTypes != null && foundIdentityTypes.size() == 2) {
            expectedValues.append(' ').append(foundIdentityTypes.get(0));
            extractedValues.append(' ').append(foundIdentityTypes.get(1));
            typeSimilarity = stringMatcher.calculateSimilarity(foundIdentityTypes.get(0), foundIdentityTypes.get(1));

        } else if(foundIdentityTypes != null && foundIdentityTypes.size() == 1) {
            expectedValues.append(' ').append(foundIdentityTypes.get(0));
        }

        List<String> foundFirstNames = matched.get(KeyField.INDIVIDUAL_FIRST_NAME);
        double firstNameSimilarity = foundFirstNames != null && foundFirstNames.size() == 2
                ? stringMatcher.calculateSimilarity(foundFirstNames.get(0), foundFirstNames.get(1))
                : 0.0;

        List<String> foundMiddleNames = matched.get(KeyField.INDIVIDUAL_MIDDLE_NAME);
        double middleNameSimilarity = 0.0;
                // ? stringMatcher.calculateSimilarity(foundMiddleNames.get(0), foundMiddleNames.get(1))
                // : 0.0;

        if(foundMiddleNames != null && foundMiddleNames.size() == 2) {
            expectedValues.append(' ').append(foundMiddleNames.get(0));
            extractedValues.append(' ').append(foundMiddleNames.get(1));
            middleNameSimilarity = stringMatcher.calculateSimilarity(foundMiddleNames.get(0), foundMiddleNames.get(1));

        } else if(foundMiddleNames != null && foundMiddleNames.size() == 1) {
            expectedValues.append(' ').append(foundMiddleNames.get(0));
        }

        List<String> foundSurnames = matched.get(KeyField.INDIVIDUAL_SURNAME);
        double surnameSimilarity = 0.0;
                // ? stringMatcher.calculateSimilarity(foundSurnames.get(0), foundSurnames.get(1))
                // : 0.0;

        if(foundSurnames != null && foundSurnames.size() == 2) {
            expectedValues.append(' ').append(foundSurnames.get(0));
            extractedValues.append(' ').append(foundSurnames.get(1));
            surnameSimilarity = stringMatcher.calculateSimilarity(foundSurnames.get(0), foundSurnames.get(1));

        } else if(foundSurnames != null && foundSurnames.size() == 1) {
            expectedValues.append(' ').append(foundSurnames.get(0));
        }

        if (idSimilarity > 0.8 &&  firstNameSimilarity > 0.8 && surnameSimilarity > 0.8) {
            verificationTagResults.setVerificationTagStatus(VerificationTagStatus.SUCCESSFUL);

            

        } else {
            verificationTagResults.setVerificationTagStatus(VerificationTagStatus.FAILED);
        }

        verificationTagResults.setScore((idSimilarity + firstNameSimilarity +  surnameSimilarity)/3);

        verificationTagResults.setValues(List.of(
            expectedValues.toString(), 
            extractedValues.toString()
        ));

        return verificationTagResults;
    }

    VerificationTagResult getEmployemeVerificationTagResult(VerificationTag tag, Map<KeyField, List<String>> matched) {

        StringBuilder expectedValues = new StringBuilder();
        StringBuilder extractedValues = new StringBuilder();

        VerificationTagResult verificationTagResults = new VerificationTagResult();
        verificationTagResults.setVerificationTag(tag);

        verificationTagResults.setVerificationTagStatus(VerificationTagStatus.UNCHECKED);

        double similarity = 0.0;

        List<String> foundEmploymentPositions = matched.get(KeyField.EMPLOYMENT_POSITION);

        if(foundEmploymentPositions != null && foundEmploymentPositions.size() == 1) {  
            extractedValues.append(foundEmploymentPositions.get(0));
            similarity = similarity + 1;
        }

        List<String> foundOrganisationNames = matched.get(KeyField.ORGANISATION_NAME);
        if(foundOrganisationNames != null && foundOrganisationNames.size() == 1) {  
            extractedValues.append(' ').append(foundOrganisationNames.get(0));
            similarity = similarity + 1;
        }

        List<String> foundOrganisationAbbreviations = matched.get(KeyField.ORGANISATION_ABBREVIATION);
        if(foundOrganisationAbbreviations != null && foundOrganisationAbbreviations.size() == 1) {  
            extractedValues.append(' ').append(foundOrganisationAbbreviations.get(0));
            similarity = similarity + 1;
        }

        List<String> foundOrganisationPhysicalAddresses = matched.get(KeyField.ORGANISATION_PHYSICAL_ADDRESS);
        if(foundOrganisationPhysicalAddresses != null && foundOrganisationPhysicalAddresses.size() == 1) {  
            extractedValues.append(' ').append(foundOrganisationPhysicalAddresses.get(0));
            similarity = similarity + 1;
        }

        List<String> foundOrganisationPostalAddresses = matched.get(KeyField.ORGANISATION_POSTAL_ADDRESS);
        if(foundOrganisationPostalAddresses != null && foundOrganisationPostalAddresses.size() == 1) {  
            extractedValues.append(' ').append(foundOrganisationPostalAddresses.get(0));
            similarity = similarity + 1;
        }

        List<String> foundOrganisationContacts = matched.get(KeyField.ORGANISATION_CONTACT);
        if(foundOrganisationContacts != null && foundOrganisationContacts.size() == 1) {  
            extractedValues.append(' ').append(foundOrganisationContacts.get(0));
            similarity = similarity + 1;
        }

        List<String> foundEmploymentStartDates = matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_START);
        if(foundEmploymentStartDates != null && foundEmploymentStartDates.size() == 1) {  
            extractedValues.append(' ').append(foundEmploymentStartDates.get(0));
            similarity = similarity + 1;
        }

        List<String> foundEmploymentEndDates = matched.get(KeyField.INDIVIDUAL_EMPLOYMENT_END);
        if(foundEmploymentEndDates != null && foundEmploymentEndDates.size() == 1) {  
            extractedValues.append(' ').append(foundEmploymentEndDates.get(0));
            similarity = similarity + 1;
        }

        return verificationTagResults;
    }

    private void updateKycRecordWithMatches(KycRecord kycRecord, DocumentDTO document,
            Map<KeyField, List<String>> matched) {

        if(document.getVerificationTagResults() == null) {
            document.setVerificationTagResults(new ArrayList<>());
        }

        document.getVerificationTags().forEach(tag -> {

            switch (tag) {

                case IDENTITY_VERIFICATION:

                    VerificationTagResult identityVerificationResult = this.getIndividualIdentityVerificationResult(tag, matched);
                    document.getVerificationTagResults().add(identityVerificationResult);

                    break;
                case SANCTIONS_DETAILS_VERIFICATION:
                    break;
                case PEP_VERIFICATION:
                    break;
                case SOURCE_OF_FUNDS_VERIFICATION:
                    break;
                case EMPLOYMENT_VERIFICATION:
                    break;
                case ADDRESS_VERIFICATION:
                    break;
                case SANCTIONS_MATCH_VERIFICATION:
                    break;
            }
        });

    }

    private void processIndividualInformation(DocumentDTO document) {

    }

    private void processOrganisationInformation(DocumentDTO document) {

    }
}
