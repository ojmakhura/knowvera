// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::individual::employment::EmploymentRecordService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.individual.employment;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.centralkyc.individual.employment.EmploymentRecordService
 */
@Service("employmentRecordService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class EmploymentRecordServiceImpl
    extends EmploymentRecordServiceBase
{
    

    public EmploymentRecordServiceImpl(EmploymentRecordDao employmentRecordDao,
            EmploymentRecordRepository employmentRecordRepository, EmploymentRecordMapper employmentRecordMapper,
            MessageSource messageSource) {
        super(employmentRecordDao, employmentRecordRepository, employmentRecordMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#findById(String)
     */
    @Override
    protected EmploymentRecordDTO handleFindById(String id)
        throws Exception
    {

        EmploymentRecord employmentRecord = this.employmentRecordRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new Exception("EmploymentRecord not found for id: " + id));
        
        return this.employmentRecordMapper.toEmploymentRecordDTO(employmentRecord);
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#save(EmploymentRecordDTO)
     */
    @Override
    protected EmploymentRecordDTO handleSave(EmploymentRecordDTO employmentRecord)
        throws Exception
    {

        EmploymentRecord entity = this.employmentRecordDao.employmentRecordDTOToEntity(employmentRecord);
        entity = this.employmentRecordRepository.save(entity);
        return this.employmentRecordDao.toEmploymentRecordDTO(entity);
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        if(!this.employmentRecordRepository.existsById(UUID.fromString(id))) {
            throw new EmploymentRecordServiceException("EmploymentRecord not found for id: " + id);
        }

        this.employmentRecordRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#getAll()
     */
    @Override
    protected List<EmploymentRecordDTO> handleGetAll()
        throws Exception
    {

        Collection<EmploymentRecord> employmentRecords = this.employmentRecordRepository.findAll();
        return this.employmentRecordMapper.toEmploymentRecordDTOCollection(employmentRecords);
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#search(String)
     */
    @Override
    protected List<EmploymentRecordDTO> handleSearch(String criteria)
        throws Exception
    {
        throw new UnsupportedOperationException("bw.co.centralkyc.individual.employment.EmploymentRecordService.handleSearch(String criteria) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#getAll(Integer, Integer)
     */
    @Override
    protected Page<EmploymentRecordDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<EmploymentRecord> employmentRecords = this.employmentRecordRepository.findAll(pageRequest);

        return employmentRecords.map(employmentRecord -> this.employmentRecordDao.toEmploymentRecordDTO(employmentRecord));

    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#search(String, Integer, Integer)
     */
    @Override
    protected Page<EmploymentRecordDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        // TODO implement protected  Page<EmploymentRecordDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throw new UnsupportedOperationException("bw.co.centralkyc.individual.employment.EmploymentRecordService.handleSearch(String criteria, Integer pageNumber, Integer pageSize) Not implemented!");
    }

    /**
     * @see bw.co.centralkyc.individual.employment.EmploymentRecordService#findByIndividual(String)
     */
    @Override
    protected List<EmploymentRecordDTO> handleFindByIndividual(String individualId)
        throws Exception
    {

        Specification<EmploymentRecord> specification = (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("individual").get("id"), UUID.fromString(individualId));

        Collection<EmploymentRecord> employmentRecords = this.employmentRecordRepository.findAll(specification);
        return this.employmentRecordMapper.toEmploymentRecordDTOCollection(employmentRecords);
    }

}