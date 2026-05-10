// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::document::type::verification::VerificationDataConfigService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.document.type.verification;

import bw.co.centralkyc.document.type.DocumentType;
import bw.co.centralkyc.document.type.DocumentTypeRepository;
import bw.co.centralkyc.document.type.field.ExpectedField;
import bw.co.centralkyc.document.type.field.ExpectedFieldRepository;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.centralkyc.document.type.verification.VerificationDataConfigService
 */
@Service("verificationDataConfigService")
public class VerificationDataConfigServiceImpl
    extends VerificationDataConfigServiceBase
{
    private final DocumentTypeRepository documentTypeRepository;
    private final ExpectedFieldRepository expectedFieldRepository;
    private final JdbcTemplate jdbcTemplate;
    public VerificationDataConfigServiceImpl(
        VerificationDataConfigRepository verificationDataConfigRepository,
        VerificationDataConfigMapper verificationDataConfigMapper,
        DocumentTypeRepository typeRepository,
        ExpectedFieldRepository expectedFieldRepository,
        JdbcTemplate jdbcTemplate,
        MessageSource messageSource
    ) {
        
        super(
            verificationDataConfigRepository,
            verificationDataConfigMapper,
            messageSource
        );

        this.documentTypeRepository = typeRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.expectedFieldRepository = expectedFieldRepository;
    }

    /**
     * @see bw.co.centralkyc.document.type.verification.VerificationDataConfigService#findById(String)
     */
    @Override
    protected VerificationDataConfigDTO handleFindById(String id)
        throws Exception
    {
        VerificationDataConfig verificationDataConfig = this.verificationDataConfigRepository.findById(UUID.fromString(id))
            .orElseThrow(() -> new RuntimeException("VerificationDataConfig not found for id: " + id));

        return this.verificationDataConfigMapper.toVerificationDataConfigDTO(verificationDataConfig);
    }

    /**
     * @see bw.co.centralkyc.document.type.verification.VerificationDataConfigService#save(@Valid VerificationDataConfigDTO)
     */
    @Override
    protected VerificationDataConfigDTO handleSave(@Valid VerificationDataConfigDTO verificationDataConfig)
        throws Exception
    {

        VerificationDataConfig verificationDataConfigEntity = this.verificationDataConfigMapper.verificationDataConfigDTOToEntity(verificationDataConfig);
        VerificationDataConfig savedVerificationDataConfig = this.verificationDataConfigRepository.save(verificationDataConfigEntity);
        return this.verificationDataConfigMapper.toVerificationDataConfigDTO(savedVerificationDataConfig);
    }

    /**
     * @see bw.co.centralkyc.document.type.verification.VerificationDataConfigService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        UUID configId = UUID.fromString(id);
        VerificationDataConfig config = verificationDataConfigRepository.getReferenceById(configId);

        this.verificationDataConfigRepository.delete(config);
        return true;
    }

}