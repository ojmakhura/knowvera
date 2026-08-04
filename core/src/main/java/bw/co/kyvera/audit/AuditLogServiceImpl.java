// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.kyvera::audit::AuditLogService
 * STEREOTYPE:  Service
 */
package bw.co.kyvera.audit;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.kyvera.audit.AuditLogService
 */
@Service("auditLogService")
public class AuditLogServiceImpl
    extends AuditLogServiceBase
{
    public AuditLogServiceImpl(
        AuditLogRepository auditLogRepository,
        AuditLogMapper auditLogMapper,
        MessageSource messageSource
    ) {
        
        super(
            auditLogRepository,
            auditLogMapper,
            messageSource
        );
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#findById(String)
     */
    @Override
    protected AuditLogDTO handleFindById(String id)
        throws Exception
    {
        AuditLog log = this.auditLogRepository.findById(UUID.fromString(id)).orElseThrow(() -> new AuditLogServiceException(messageSource.getMessage("auditLog.notFound", new Object[]{id}, null)));
        return this.auditLogMapper.toAuditLogDTO(log);
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#save(@Valid AuditLogDTO)
     */
    @Override
    protected AuditLogDTO handleSave(@Valid AuditLogDTO auditLog)
        throws Exception
    {

        AuditLog log = this.auditLogMapper.auditLogDTOToEntity(auditLog);
        log = this.auditLogRepository.save(log);
        return this.auditLogMapper.toAuditLogDTO(log);
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        this.auditLogRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#getAll()
     */
    @Override
    protected List<AuditLogDTO> handleGetAll()
        throws Exception
    {

        List<AuditLog> logs = this.auditLogRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
        return this.auditLogMapper.toAuditLogDTOCollection(logs);
    }

    private Specification<AuditLog> buildSpecification(AuditLogCriteria criteria) {

        Specification<AuditLog> spec = (root, query, cb) -> cb.conjunction();

        return spec;

    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#search(@Valid AuditLogCriteria)
     */
    @Override
    protected List<AuditLogDTO> handleSearch(@Valid AuditLogCriteria criteria)
        throws Exception
    {

        Specification<AuditLog> spec = buildSpecification(criteria);

        List<AuditLog> logs = this.auditLogRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "timestamp"));
        return this.auditLogMapper.toAuditLogDTOCollection(logs);
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#getAll(Integer, Integer)
     */
    @Override
    protected Page<AuditLogDTO> handleGetAll(Integer pageNumber, Integer pageSize)
        throws Exception
    {

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLog> logs = this.auditLogRepository.findAll(pageRequest);
        return logs.map(log -> this.auditLogMapper.toAuditLogDTO(log));
    }

    /**
     * @see bw.co.kyvera.audit.AuditLogService#search(@Valid AuditLogCriteria, Integer, Integer)
     */
    @Override
    protected Page<AuditLogDTO> handleSearch(@Valid AuditLogCriteria criteria, Integer pageNumber, Integer pageSize)
        throws Exception
    {
        Specification<AuditLog> spec = buildSpecification(criteria);
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<AuditLog> logs = this.auditLogRepository.findAll(spec, pageRequest);
        return logs.map(log -> this.auditLogMapper.toAuditLogDTO(log));
    }

}