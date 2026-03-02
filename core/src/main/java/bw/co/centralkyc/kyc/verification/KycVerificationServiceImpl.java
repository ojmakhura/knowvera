// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::kyc::verification::KycVerificationService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.kyc.verification;

import bw.co.centralkyc.SearchObject;
import bw.co.centralkyc.kyc.KycRecordSearchCriteria;
import java.util.Collection;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.centralkyc.kyc.verification.KycVerificationService
 */
@Service("kycVerificationService")
public class KycVerificationServiceImpl
    extends KycVerificationServiceBase
{
    

    public KycVerificationServiceImpl(KycVerificationDao kycVerificationDao,
            KycVerificationRepository kycVerificationRepository, KycVerificationMapper kycVerificationMapper,
            MessageSource messageSource) {
        super(kycVerificationDao, kycVerificationRepository, kycVerificationMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#findById(String)
     */
    @Override
    protected KycVerificationDTO handleFindById(String id)
        throws Exception
    {

        KycVerification entity = this.kycVerificationRepository.findById(java.util.UUID.fromString(id))
            .orElseThrow(() -> new KycVerificationServiceException("Entity not found for id: " + id));

        return this.kycVerificationMapper.toKycVerificationDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#save(KycVerificationDTO)
     */
    @Override
    protected KycVerificationDTO handleSave(KycVerificationDTO kycVerification)
        throws Exception
    {

        KycVerification entity = this.kycVerificationMapper.kycVerificationDTOToEntity(kycVerification);
        entity = this.kycVerificationRepository.save(entity);
        return this.kycVerificationMapper.toKycVerificationDTO(entity);

    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        if (!this.kycVerificationRepository.existsById(java.util.UUID.fromString(id))) {
            throw new KycVerificationServiceException("Entity not found for id: " + id);
        }
        this.kycVerificationRepository.deleteById(java.util.UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#getAll()
     */
    @Override
    protected Collection<KycVerificationDTO> handleGetAll()
        throws Exception
    {
        // TODO implement protected  Collection<KycVerificationDTO> handleGetAll()
        throw new UnsupportedOperationException("bw.co.centralkyc.kyc.verification.KycVerificationService.handleGetAll() Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#search(KycRecordSearchCriteria)
     */
    @Override
    protected Collection<KycVerificationDTO> handleSearch(KycVerificationSearchCriteria criteria)
        throws Exception
    {
        // TODO implement protected  Collection<KycVerificationDTO> handleSearch(KycRecordSearchCriteria criteria)
        throw new UnsupportedOperationException("bw.co.centralkyc.kyc.verification.KycVerificationService.handleSearch(KycRecordSearchCriteria criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#getAll(Integer, Integer)
     */
    @Override
    protected Page<KycVerificationDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<KycVerificationDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.kyc.verification.KycVerificationService.handleGetAll(Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.kyc.verification.KycVerificationService#search(SearchObject<KycRecordSearchCriteria>)
     */
    @Override
    protected Page<KycVerificationDTO> handleSearch(SearchObject<KycRecordSearchCriteria> criteria)
        throws Exception
    {
        // TODO implement protected  Page<KycVerificationDTO> handleSearch(SearchObject<KycRecordSearchCriteria> criteria)
        throw new UnsupportedOperationException("bw.co.centralkyc.kyc.verification.KycVerificationService.handleSearch(SearchObject<KycRecordSearchCriteria> criteria) Not implemented!");
    }

    @Override
    protected KycVerificationDTO handleFindByRecord(String recordId) throws Exception {
        KycVerification entity = this.kycVerificationRepository.findByKycRecordId(java.util.UUID.fromString(recordId))
            .orElseThrow(() -> new KycVerificationServiceException("Entity not found for recordId: " + recordId));

        return this.kycVerificationMapper.toKycVerificationDTO(entity);
    }

}