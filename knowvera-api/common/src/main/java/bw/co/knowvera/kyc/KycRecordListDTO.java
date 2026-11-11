package bw.co.knowvera.kyc;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//KycRecordListDTO
public record KycRecordListDTO (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.id
     */
    java.lang.String id,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.ref
     */
    java.lang.String ref,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.identityNo
     */
    java.lang.String identityNo,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.name
     */
    java.lang.String name,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.kycStatus
     */
    bw.co.knowvera.kyc.KycComplianceStatus kycStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.expiryDate
     */
    java.time.LocalDate expiryDate
) implements Serializable {
    /**
     * Constructor taking only required properties
     * @param idIn java.lang.String TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.id
     * @param kycStatusIn bw.co.knowvera.kyc.KycComplianceStatus TODO: Model Documentation for bw.co.knowvera.kyc.KycRecordListDTO.kycStatus
     */
    public KycRecordListDTO(final java.lang.String idIn, final bw.co.knowvera.kyc.KycComplianceStatus kycStatusIn)
    {
        this(
            idIn,  // id: required property - use constructor argument
            null,  // ref: optional non-primitive property - use null
            null,  // identityNo: optional non-primitive property - use null
            null,  // name: optional non-primitive property - use null
            kycStatusIn,  // kycStatus: required property - use constructor argument
            null // expiryDate: optional non-primitive property - use null
        );
    }

}
