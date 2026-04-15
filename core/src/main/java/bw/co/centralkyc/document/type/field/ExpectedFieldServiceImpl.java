// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::document::type::field::ExpectedFieldService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.document.type.field;

import jakarta.validation.Valid;

import java.util.Collection;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.centralkyc.document.type.field.ExpectedFieldService
 */
@Service("expectedFieldService")
public class ExpectedFieldServiceImpl
        extends ExpectedFieldServiceBase {
    public ExpectedFieldServiceImpl(
            ExpectedFieldDao expectedFieldDao,
            ExpectedFieldRepository expectedFieldRepository,
            ExpectedFieldMapper expectedFieldMapper,
            MessageSource messageSource) {

        super(
                expectedFieldDao,
                expectedFieldRepository,
                expectedFieldMapper,
                messageSource);
    }

    /**
     * @see bw.co.centralkyc.document.type.field.ExpectedFieldService#findById(String)
     */
    @Override
    protected ExpectedFieldDTO handleFindById(String id)
            throws Exception {

        ExpectedField expectedField = this.expectedFieldRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("ExpectedField not found for id: " + id));

        return this.expectedFieldMapper.toExpectedFieldDTO(expectedField);
    }

    /**
     * @see bw.co.centralkyc.document.type.field.ExpectedFieldService#save(@Valid
     *      ExpectedFieldDTO)
     */
    @Override
    protected ExpectedFieldDTO handleSave(@Valid ExpectedFieldDTO expectedField)
            throws Exception {

        ExpectedField expectedFieldEntity = this.expectedFieldMapper.expectedFieldDTOToEntity(expectedField);
        ExpectedField savedExpectedField = this.expectedFieldRepository.save(expectedFieldEntity);
        return this.expectedFieldMapper.toExpectedFieldDTO(savedExpectedField);
    }

    /**
     * @see bw.co.centralkyc.document.type.field.ExpectedFieldService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        this.expectedFieldRepository.deleteById(UUID.fromString(id));
        return true;
    }

    @Override
    protected Page<ExpectedFieldDTO> handleFindByDocumentType(String documentTypeId, Integer pageNumber,
            Integer pageSize) throws Exception {

        Specification<ExpectedField> specification = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("documentType").get("id"), UUID.fromString(documentTypeId));

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ExpectedField> expectedFieldsPage = this.expectedFieldRepository.findAll(specification, pageRequest);
        return expectedFieldsPage.map(this.expectedFieldMapper::toExpectedFieldDTO);
    }

    @Override
    protected Collection<ExpectedFieldDTO> handleFindByDocumentType(String documentTypeId) throws Exception {
        Specification<ExpectedField> specification = (root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("documentType").get("id"), UUID.fromString(documentTypeId));

        return this.expectedFieldRepository.findAll(specification)
                .stream()
                .map(this.expectedFieldMapper::toExpectedFieldDTO)
                .toList();
    }

}