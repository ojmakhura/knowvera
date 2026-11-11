package bw.co.knowvera.individual.employment;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.individual.employment.EmploymentRecordService
 */
public interface EmploymentRecordService
{
    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.findById
     * @param id TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.findById(id)
     * @return EmploymentRecordDTO
     */
    public EmploymentRecordDTO findById(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.save
     * @param employmentRecord TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.save(employmentRecord)
     * @return EmploymentRecordDTO
     */
    public EmploymentRecordDTO save(@Valid EmploymentRecordDTO employmentRecord) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.remove
     * @param id TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.getAll
     * @return List<EmploymentRecordDTO>
     */
    public List<EmploymentRecordDTO> getAll() ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.search(criteria)
     * @return List<EmploymentRecordDTO>
     */
    public List<EmploymentRecordDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.getAll
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.getAll(pageSize)
     * @return Page<EmploymentRecordDTO>
     */
    public Page<EmploymentRecordDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.search
     * @param criteria TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.search(criteria)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.search(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.search(pageSize)
     * @return Page<EmploymentRecordDTO>
     */
    public Page<EmploymentRecordDTO> search(String criteria, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.individual.employment.EmploymentRecordService.findByIndividual
     * @param individualId TODO: Model Documentation for
bw.co.knowvera.individual.employment.EmploymentRecordService.findByIndividual(individualId)
     * @return List<EmploymentRecordDTO>
     */
    public List<EmploymentRecordDTO> findByIndividual(String individualId) ;

}