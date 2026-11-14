package bw.co.knowvera.organisation;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//OrganisationListDTO
public record OrganisationListDTO (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.id
     */
    java.lang.String id,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.code
     */
    java.lang.String code,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.name
     */
    java.lang.String name,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.registrationNo
     */
    java.lang.String registrationNo,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.status
     */
    bw.co.knowvera.GeneralStatus status,    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.OrganisationListDTO.contactEmailAddress
     */
    java.lang.String contactEmailAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.kycStatus
     */
    bw.co.knowvera.kyc.KycComplianceStatus kycStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.isClient
     */
    java.lang.Boolean isClient,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.keycloakId
     */
    java.lang.String keycloakId,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.postalAddress
     */
    java.lang.String postalAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.physicalAddress
     */
    java.lang.String physicalAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.closedAt
     */
    java.time.LocalDateTime closedAt
) implements Serializable {
    /**
     * Constructor taking only required properties
     * @param codeIn java.lang.String TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.code
     * @param nameIn java.lang.String TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.name
     * @param registrationNoIn java.lang.String TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.registrationNo
     * @param statusIn bw.co.knowvera.GeneralStatus TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.status
     * @param kycStatusIn bw.co.knowvera.kyc.KycComplianceStatus TODO: Model Documentation for bw.co.knowvera.organisation.OrganisationListDTO.kycStatus
     */
    public OrganisationListDTO(final java.lang.String codeIn, final java.lang.String nameIn, final java.lang.String registrationNoIn, final bw.co.knowvera.GeneralStatus statusIn, final bw.co.knowvera.kyc.KycComplianceStatus kycStatusIn)
    {
        this(
            null,  // id: optional non-primitive property - use null
            codeIn,  // code: required property - use constructor argument
            nameIn,  // name: required property - use constructor argument
            registrationNoIn,  // registrationNo: required property - use constructor argument
            statusIn,  // status: required property - use constructor argument
            null,  // contactEmailAddress: optional non-primitive property - use null
            kycStatusIn,  // kycStatus: required property - use constructor argument
            null,  // isClient: optional non-primitive property - use null
            null,  // keycloakId: optional non-primitive property - use null
            null,  // postalAddress: optional non-primitive property - use null
            null,  // physicalAddress: optional non-primitive property - use null
            null // closedAt: optional non-primitive property - use null
        );
    }

}
