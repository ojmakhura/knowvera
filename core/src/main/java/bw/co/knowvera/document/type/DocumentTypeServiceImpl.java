// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::document::type::DocumentTypeService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.document.type;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
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

import bw.co.knowvera.document.type.field.ExpectedField;
import bw.co.knowvera.document.type.field.ExpectedFieldDTO;
import bw.co.knowvera.document.type.field.ExpectedFieldMapper;
import bw.co.knowvera.document.type.field.ExpectedFieldRepository;
import bw.co.knowvera.document.type.verification.VerificationDataConfig;
import bw.co.knowvera.document.type.verification.VerificationDataConfigMapper;
import bw.co.knowvera.document.type.verification.VerificationDataConfigRepository;
import bw.co.knowvera.document.type.DocumentType;
import bw.co.knowvera.document.type.DocumentTypeDTO;
import bw.co.knowvera.document.type.DocumentTypeRepository;
import bw.co.knowvera.document.type.DocumentTypeServiceBase;
import jakarta.validation.Valid;

/**
 * @see bw.co.knowvera.document.type.DocumentTypeService
 */
@Service("documentTypeService")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class DocumentTypeServiceImpl
        extends DocumentTypeServiceBase {

    private final ExpectedFieldRepository expectedFieldRepository;
    private final VerificationDataConfigRepository verificationDataConfigRepository;
    private final VerificationDataConfigMapper verificationDataConfigMapper;
    private final ExpectedFieldMapper expectedFieldMapper;

    public DocumentTypeServiceImpl(DocumentTypeRepository documentTypeRepository,
            DocumentTypeMapper documentTypeMapper, ExpectedFieldRepository expectedFieldRepository,
            VerificationDataConfigRepository verificationDataConfigRepository, ExpectedFieldMapper expectedFieldMapper,
            VerificationDataConfigMapper verificationDataConfigMapper) {
        super(documentTypeRepository, documentTypeMapper);
        // TODO Auto-generated constructor stub

        this.expectedFieldRepository = expectedFieldRepository;
        this.verificationDataConfigRepository = verificationDataConfigRepository;
        this.verificationDataConfigMapper = verificationDataConfigMapper;
        this.expectedFieldMapper = expectedFieldMapper;
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#findById(String)
     */
    @Override
    protected DocumentTypeDTO handleFindById(String id)
            throws Exception {

        DocumentType entity = documentTypeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("DocumentType not found for id: " + id));
        DocumentTypeDTO dto = documentTypeMapper.toDocumentTypeDTO(entity);

        // dto.setExpectedFields(expectedFieldRepository.findDtoByDocumentTypeId(List.of(entity.getId())));

        List<VerificationDataConfig> verificationDataConfigs = this.verificationDataConfigRepository.findByDocumentTypeId(entity.getId());
        dto.setVerificationDataConfigs(verificationDataConfigMapper.toVerificationDataConfigDTOCollection(verificationDataConfigs));
        return dto;
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#save(DocumentTypeDTO)
     */
    @Override
    protected DocumentTypeDTO handleSave(DocumentTypeDTO documentType)
            throws Exception {

        DocumentType doc = documentTypeMapper.documentTypeDTOToEntity(documentType);
        doc = documentTypeRepository.save(doc);
        DocumentTypeDTO dto = documentTypeMapper.toDocumentTypeDTO(doc);

        // dto.setExpectedFields(expectedFieldRepository.findDtoByDocumentTypeId(doc.getId()));

        List<VerificationDataConfig> verificationDataConfigs = this.verificationDataConfigRepository.findByDocumentTypeId(doc.getId());
        dto.setVerificationDataConfigs(verificationDataConfigMapper.toVerificationDataConfigDTOCollection(verificationDataConfigs));

        return dto;
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        documentTypeRepository.deleteById(UUID.fromString(id));

        return true;
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#getAll()
     */
    @Override
    protected List<DocumentTypeDTO> handleGetAll()
            throws Exception {

        List<DocumentType> types = documentTypeRepository.findAll();

        return documentTypeMapper.toDocumentTypeDTOCollection(types);
    }

    private Specification<DocumentType> createSpecification(String criteria) {

        if (StringUtils.isBlank(criteria)) {
            return null;
        }

        return (root, cq, cb) -> {
            return cb.or(
                    cb.like(cb.upper(root.get("code")), "%" + criteria.toUpperCase() + "%"),
                    cb.like(cb.upper(root.get("name")), "%" + criteria.toUpperCase() + "%"));
        };

    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#search(String)
     */
    @Override
    protected List<DocumentTypeDTO> handleSearch(String criteria)
            throws Exception {
        // TODO implement protected List<DocumentTypeDTO> handleSearch(String
        // criteria)

        Specification<DocumentType> spec = this.createSpecification(criteria);

        List<DocumentType> types = spec == null ? documentTypeRepository.findAll(Sort.by(Direction.ASC, "name"))
                : documentTypeRepository.findAll(spec, Sort.by(Direction.ASC, "name"));

        return documentTypeMapper.toDocumentTypeDTOCollection(types);
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#getAll(Integer,
     *      Integer)
     */
    @Override
    protected Page<DocumentTypeDTO> handleGetAll(Integer pageNumber, Integer pageSize)
            throws Exception {
        // TODO implement protected Page<DocumentTypeDTO> handleGetAll(Integer
        // pageNumber, Integer pageSize)

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Page<DocumentType> types = documentTypeRepository.findAll(pageable);

        return types.map(type -> documentTypeMapper.toDocumentTypeDTO(type));
    }

    /**
     * @see bw.co.knowvera.document.type.DocumentTypeService#search(String,
     *      Integer, Integer)
     */
    @Override
    protected Page<DocumentTypeDTO> handleSearch(String criteria, Integer pageNumber, Integer pageSize)
            throws Exception {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, "name"));
        Specification<DocumentType> spec = this.createSpecification(criteria);
        Page<DocumentType> types = spec == null ? documentTypeRepository.findAll(pageable)
                : documentTypeRepository.findAll(spec, pageable);

        return types.map(type -> documentTypeMapper.toDocumentTypeDTO(type));

    }

    @Override
    protected DocumentTypeDTO handleAddExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)
            throws Exception {

        DocumentType documentType = documentTypeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new Exception("DocumentType not found for id: " + id));

        
        for (ExpectedFieldDTO expectedFieldDTO : expectedFields) {

            ExpectedField expectedField = expectedFieldMapper.expectedFieldDTOToEntity(expectedFieldDTO);
            if(StringUtils.isNotBlank(expectedFieldDTO.getId())) {
                ExpectedField f = documentType.getExpectedFields().stream()
                        .filter(ef -> ef.getId().toString().equals(expectedFieldDTO.getId()))
                        .findFirst()
                        .orElse(null);

                if(f != null) {
                    f.setExactMatch(expectedField.getExactMatch());
                    f.setField(expectedField.getField());
                    f.setFieldLabel(expectedField.getFieldLabel());
                    f.setFieldType(expectedField.getFieldType());
                    f.setFormat(expectedField.getFormat());
                    f.setMandatory(expectedField.getMandatory());
                    f.setMany(expectedField.getMany());
                    f.setMatchTo(expectedField.getMatchTo());
                    f.setTargetType(expectedField.getTargetType());
                    f.setVerificationDataConfigs(expectedField.getVerificationDataConfigs());

                } else {
                    documentType.getExpectedFields().add(expectedField);
                }
            } else {

                documentType.getExpectedFields().add(expectedField);
            }

            expectedField.setDocumentType(documentType);
            // expectedFieldRepository.save(expectedField);
        }

        documentType = documentTypeRepository.save(documentType);

        return documentTypeMapper.toDocumentTypeDTO(documentType);
    }

}