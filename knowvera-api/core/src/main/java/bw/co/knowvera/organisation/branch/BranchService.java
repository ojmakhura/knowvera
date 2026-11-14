package bw.co.knowvera.organisation.branch;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService
 */
public interface BranchService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.findById(id)
     * @return BranchDTO
     */
    public BranchDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.save
     * @param branch TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.save(branch)
     * @return BranchDTO
     */
    public BranchDTO save(@Valid BranchDTO branch) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.getAll
     * @return List<BranchDTO>
     */
    public List<BranchDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search(criteria)
     * @return List<BranchDTO>
     */
    public List<BranchDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.getAll(pageSize)
     * @return Page<BranchDTO>
     */
    public Page<BranchDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search(criteria)
     * @param pageNumber TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.knowvera.organisation.branch.BranchService.search(pageSize)
     * @return Page<BranchDTO>
     */
    public Page<BranchDTO> search(String criteria, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.branch.BranchService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(organisationId)
     * @return List<BranchDTO>
     */
    public List<BranchDTO> findByOrganisation(String organisationId) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.organisation.branch.BranchService.findByOrganisation
     * @param organisationId TODO: Model Documentation for
bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(organisationId)
     * @param pageNumber TODO: Model Documentation for
bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.knowvera.organisation.branch.BranchService.findByOrganisation(pageSize)
     * @return Page<BranchDTO>
     */
    public Page<BranchDTO> findByOrganisation(String organisationId, Integer pageNumber, Integer pageSize) ;

}