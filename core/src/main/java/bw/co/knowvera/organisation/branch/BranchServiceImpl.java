// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::organisation::branch::BranchService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.organisation.branch;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import bw.co.knowvera.organisation.branch.Branch;
import bw.co.knowvera.organisation.branch.BranchDTO;
import bw.co.knowvera.organisation.branch.BranchRepository;
import bw.co.knowvera.organisation.branch.BranchServiceBase;

/**
 * @see bw.co.knowvera.organisation.branch.BranchService
 */
@Service("branchService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class BranchServiceImpl
    extends BranchServiceBase
{
    
    public BranchServiceImpl(BranchRepository branchRepository, BranchMapper branchMapper,
            MessageSource messageSource) {
        super(branchRepository, branchMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#findById(String)
     */
    @Override
    protected BranchDTO handleFindById(String id)
        throws Exception
    {
        Branch branch = branchRepository.getReferenceById(UUID.fromString(id));
        return branchMapper.toBranchDTO(branch);
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#save(BranchDTO)
     */
    @Override
    protected BranchDTO handleSave(BranchDTO branch)
        throws Exception
    {

        Branch branchEntity = branchMapper.branchDTOToEntity(branch);
        branchEntity = branchRepository.save(branchEntity);
        return branchMapper.toBranchDTO(branchEntity);
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {
        branchRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#getAll()
     */
    @Override
    protected List<BranchDTO> handleGetAll()
        throws Exception
    {
        List<Branch> branches = branchRepository.findAll();
        return branchMapper.toBranchDTOCollection(branches);
    }

    private Specification<Branch> createSpecification(String criteria) {
        return (root, query, builder) -> {
            String likeCriteria = "%" + criteria.toLowerCase() + "%";
            return builder.or(
                builder.like(builder.lower(root.get("name")), likeCriteria),
                builder.like(builder.lower(root.get("code")), likeCriteria),
                builder.like(builder.lower(root.get("organisation")), likeCriteria)
            );
        };
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#search(String)
     */
    @Override
    protected List<BranchDTO> handleSearch(String criteria)
        throws Exception
    {
        Specification<Branch> spec = createSpecification(criteria);
        List<Branch> branches = branchRepository.findAll(spec, Sort.by(Direction.ASC, "name"));
        return branchMapper.toBranchDTOCollection(branches);
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#getAll(Integer, Integer)
     */
    @Override
    protected Page<BranchDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Page<Branch> branchPage = branchRepository.findAll(pageable);
        return branchPage.map(branch -> branchMapper.toBranchDTO(branch));
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#search(String, Integer, Integer)
     */
    @Override
    protected Page<BranchDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
        throws Exception
    {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Specification<Branch> spec = createSpecification(criteria);
        Page<Branch> branchPage = branchRepository.findAll(spec, pageable);
        return branchPage.map(branch -> branchMapper.toBranchDTO(branch));
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#findByOrganisation(String)
     */
    @Override
    protected List<BranchDTO> handleFindByOrganisation(String organisationId)
        throws Exception
    {

        Specification<Branch> spec = (root, query, builder) -> 
            builder.equal(root.get("organisation").get("id"), UUID.fromString(organisationId));
        List<Branch> branches = branchRepository.findAll(spec, Sort.by(Direction.ASC, "name"));
        return branchMapper.toBranchDTOCollection(branches);
    }

    /**
     * @see bw.co.knowvera.organisation.branch.BranchService#findByOrganisation(String, Integer, Integer)
     */
    @Override
    protected Page<BranchDTO> handleFindByOrganisation(String organisationId, Integer pageNumber, Integer pageSize)
        throws Exception
    {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Specification<Branch> spec = (root, query, builder) -> 
            builder.equal(root.get("organisation").get("id"), UUID.fromString(organisationId));
        Page<Branch> branchPage = branchRepository.findAll(spec, pageable);
        return branchPage.map(branch -> branchMapper.toBranchDTO(branch));
    }

}