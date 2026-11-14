package bw.co.knowvera.individual;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//IndividualListDTO
public record IndividualListDTO (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.id
     */
    java.lang.String id,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.name
     */
    java.lang.String name,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.identityNo
     */
    java.lang.String identityNo,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.identityType
     */
    bw.co.knowvera.individual.IndividualIdentityType identityType,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.emailAddress
     */
    java.lang.String emailAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.kycStatus
     */
    bw.co.knowvera.kyc.KycComplianceStatus kycStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.sex
     */
    bw.co.knowvera.individual.Sex sex,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.pepStatus
     */
    bw.co.knowvera.individual.PepStatus pepStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.userCreated
     */
    java.lang.Boolean userCreated,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.physicalAddress
     */
    java.lang.String physicalAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.postalAddress
     */
    java.lang.String postalAddress,    /**
     * TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.closedAt
     */
    java.time.LocalDateTime closedAt
) implements Serializable {
    /**
     * Constructor taking only required properties
     * @param nameIn java.lang.String TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.name
     * @param identityNoIn java.lang.String TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.identityNo
     * @param identityTypeIn bw.co.knowvera.individual.IndividualIdentityType TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.identityType
     * @param kycStatusIn bw.co.knowvera.kyc.KycComplianceStatus TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.kycStatus
     * @param sexIn bw.co.knowvera.individual.Sex TODO: Model Documentation for bw.co.knowvera.individual.IndividualListDTO.sex
     */
    public IndividualListDTO(final java.lang.String nameIn, final java.lang.String identityNoIn, final bw.co.knowvera.individual.IndividualIdentityType identityTypeIn, final bw.co.knowvera.kyc.KycComplianceStatus kycStatusIn, final bw.co.knowvera.individual.Sex sexIn)
    {
        this(
            null,  // id: optional non-primitive property - use null
            nameIn,  // name: required property - use constructor argument
            identityNoIn,  // identityNo: required property - use constructor argument
            identityTypeIn,  // identityType: required property - use constructor argument
            null,  // emailAddress: optional non-primitive property - use null
            kycStatusIn,  // kycStatus: required property - use constructor argument
            sexIn,  // sex: required property - use constructor argument
            null,  // pepStatus: optional non-primitive property - use null
            null,  // userCreated: optional non-primitive property - use null
            null,  // physicalAddress: optional non-primitive property - use null
            null,  // postalAddress: optional non-primitive property - use null
            null // closedAt: optional non-primitive property - use null
        );
    }

}
