package bw.co.knowvera.kyc;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//VerificationSummaryEntry
public record VerificationSummaryEntry (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.VerificationSummaryEntry.verificationStatus
     */
    bw.co.knowvera.document.DataVerificationStatus verificationStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.VerificationSummaryEntry.verifiedBy
     */
    java.lang.String verifiedBy,    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.VerificationSummaryEntry.verificationReport
     */
    java.lang.String verificationReport,    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.kyc.VerificationSummaryEntry.verificationParameter
     */
    java.lang.String verificationParameter
) implements Serializable {
    /**
     * Constructor taking only required properties
     * @param verificationStatusIn bw.co.knowvera.document.DataVerificationStatus TODO: Model Documentation for bw.co.knowvera.kyc.VerificationSummaryEntry.verificationStatus
     * @param verificationParameterIn java.lang.String TODO: Model Documentation for bw.co.knowvera.kyc.VerificationSummaryEntry.verificationParameter
     */
    public VerificationSummaryEntry(final bw.co.knowvera.document.DataVerificationStatus verificationStatusIn, final java.lang.String verificationParameterIn)
    {
        this(
            verificationStatusIn,  // verificationStatus: required property - use constructor argument
            null,  // verifiedBy: optional non-primitive property - use null
            null,  // verificationReport: optional non-primitive property - use null
            verificationParameterIn // verificationParameter: required property - use constructor argument
        );
    }

}
