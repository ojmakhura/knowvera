package bw.co.knowvera.kyc.fields;

import jakarta.validation.Valid;

/**
 * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService
 */
public interface KycReportSectionService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.findById(id)
     * @return KycReportSectionDTO
     */
    public KycReportSectionDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.save
     * @param kycReportSection TODO: Model Documentation for
bw.co.knowvera.kyc.fields.KycReportSectionService.save(kycReportSection)
     * @return KycReportSectionDTO
     */
    public KycReportSectionDTO save(@Valid KycReportSectionDTO kycReportSection) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue
     * @param fieldValue TODO: Model Documentation for
bw.co.knowvera.kyc.fields.KycReportSectionService.addFieldValue(fieldValue)
     * @return KycReportSectionDTO
     */
    public KycReportSectionDTO addFieldValue(@Valid GroupFieldValueDTO fieldValue) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.kyc.fields.KycReportSectionService.removeFieldValue
     * @param id TODO: Model Documentation for bw.co.knowvera.kyc.fields.KycReportSectionService.removeFieldValue(id)
     * @param fieldValueId TODO: Model Documentation for
bw.co.knowvera.kyc.fields.KycReportSectionService.removeFieldValue(fieldValueId)
     * @return KycReportSectionDTO
     */
    public KycReportSectionDTO removeFieldValue(String id, String fieldValueId) ;

}