// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::kyc::fields::KycReportSectionService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.kyc.fields;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.knowvera.kyc.fields.KycReportSectionService
 */
@Service("kycReportSectionService")
public class KycReportSectionServiceImpl
        extends KycReportSectionServiceBase {
    public KycReportSectionServiceImpl(
            KycReportSectionRepository kycReportSectionRepository,
            KycReportSectionMapper kycReportSectionMapper,
            GroupFieldValueRepository groupFieldValueRepository,
            GroupFieldValueMapper groupFieldValueMapper) {

        super(
                kycReportSectionRepository,
                kycReportSectionMapper,
                groupFieldValueRepository,
                groupFieldValueMapper);
    }

    /**
     * @see bw.co.knowvera.kyc.fields.KycReportSectionService#findById(String)
     */
    @Override
    protected KycReportSectionDTO handleFindById(String id)
            throws Exception {

        KycReportSection section = kycReportSectionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new KycReportSectionServiceException("KycReportSection not found with id: " + id));

        return kycReportSectionMapper.toKycReportSectionDTO(section);
    }

    /**
     * @see bw.co.knowvera.kyc.fields.KycReportSectionService#save(@Valid
     *      KycReportSectionDTO)
     */
    @Override
    protected KycReportSectionDTO handleSave(@Valid KycReportSectionDTO kycReportSection)
            throws Exception {

        KycReportSection section = kycReportSectionMapper.kycReportSectionDTOToEntity(kycReportSection);
        section = kycReportSectionRepository.save(section);
        
         return kycReportSectionMapper.toKycReportSectionDTO(section);
    }

    /**
     * @see bw.co.knowvera.kyc.fields.KycReportSectionService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        kycReportSectionRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.knowvera.kyc.fields.KycReportSectionService#addFieldValue(@Valid
     *      GroupFieldValueDTO)
     */
    @Override
    protected KycReportSectionDTO handleAddFieldValue(@Valid GroupFieldValueDTO fieldValue)
            throws Exception {

        GroupFieldValue groupFieldValue = groupFieldValueMapper.groupFieldValueDTOToEntity(fieldValue);
        KycReportSection section = kycReportSectionRepository.findById(groupFieldValue.getKycReportSection().getId())
                    .orElseThrow(() -> new KycReportSectionServiceException("Report section not found with id: " + groupFieldValue.getKycReportSection().getId()));
            
        if(groupFieldValue.getId() != null) {

            groupFieldValueRepository.save(groupFieldValue);
            
        } else {

            groupFieldValue.setKycReportSection(section);
            section.getGroupFieldValues().add(groupFieldValue);
            section = kycReportSectionRepository.save(section);
        }

        return kycReportSectionMapper.toKycReportSectionDTO(section);
    }

}