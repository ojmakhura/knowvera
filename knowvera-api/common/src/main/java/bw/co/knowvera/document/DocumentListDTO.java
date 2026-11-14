package bw.co.knowvera.document;

import org.jspecify.annotations.Nullable;

import java.io.Serializable;


//DocumentListDTO
public record DocumentListDTO (
    // Record attributes
    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.id
     */
    java.lang.String id,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.target
     */
    bw.co.knowvera.TargetEntity target,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.targetId
     */
    java.lang.String targetId,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.targetLabel
     */
    java.lang.String targetLabel,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.fileName
     */
    java.lang.String fileName,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.documentTypeId
     */
    java.lang.String documentTypeId,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.documentType
     */
    java.lang.String documentType,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.analyticsStatus
     */
    bw.co.knowvera.document.DocumentAnalyticsStatus analyticsStatus,    /**
     * TODO: Model Documentation for bw.co.knowvera.document.DocumentListDTO.verificationStatus
     */
    bw.co.knowvera.document.DocumentVerificationStatus verificationStatus
) implements Serializable {
}
