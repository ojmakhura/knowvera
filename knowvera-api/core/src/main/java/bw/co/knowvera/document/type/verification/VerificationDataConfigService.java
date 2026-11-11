package bw.co.knowvera.document.type.verification;

import jakarta.validation.Valid;

/**
 * TODO: Model Documentation for
 * bw.co.knowvera.document.type.verification.VerificationDataConfigService
 */
public interface VerificationDataConfigService
{
    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.verification.VerificationDataConfigService.findById
     * @param id TODO: Model Documentation for
bw.co.knowvera.document.type.verification.VerificationDataConfigService.findById(id)
     * @return VerificationDataConfigDTO
     */
    public VerificationDataConfigDTO findById(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.verification.VerificationDataConfigService.save
     * @param verificationDataConfig TODO: Model Documentation for
bw.co.knowvera.document.type.verification.VerificationDataConfigService.save(verificationDataConfig)
     * @return VerificationDataConfigDTO
     */
    public VerificationDataConfigDTO save(@Valid VerificationDataConfigDTO verificationDataConfig) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.document.type.verification.VerificationDataConfigService.remove
     * @param id TODO: Model Documentation for
bw.co.knowvera.document.type.verification.VerificationDataConfigService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

}