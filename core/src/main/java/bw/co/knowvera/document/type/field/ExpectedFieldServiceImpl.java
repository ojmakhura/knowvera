// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.knowvera::document::type::field::ExpectedFieldService
 * STEREOTYPE:  Service
 */
package bw.co.knowvera.document.type.field;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @see bw.co.knowvera.document.type.field.ExpectedFieldService
 */
@Service("expectedFieldService")
public class ExpectedFieldServiceImpl
        extends ExpectedFieldServiceBase {
    public ExpectedFieldServiceImpl(
            ExpectedFieldRepository expectedFieldRepository,
            ExpectedFieldMapper expectedFieldMapper) {

        super(
                expectedFieldRepository,
                expectedFieldMapper);
    }

    /**
     * @see bw.co.knowvera.document.type.field.ExpectedFieldService#findById(String)
     */
    @Override
    protected ExpectedFieldDTO handleFindById(String id)
            throws Exception {

        ExpectedField expectedField = this.expectedFieldRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("ExpectedField not found for id: " + id));

        return this.expectedFieldMapper.toExpectedFieldDTO(expectedField);
    }

    /**
     * @see bw.co.knowvera.document.type.field.ExpectedFieldService#save(@Valid
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
     * @see bw.co.knowvera.document.type.field.ExpectedFieldService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
            throws Exception {

        this.expectedFieldRepository.deleteById(UUID.fromString(id));
        return true;
    }

    @Override
    protected Page<ExpectedFieldDTO> handleFindByDocumentType(List<String> documentTypeIds, Integer pageNumber,
            Integer pageSize) throws Exception {

        List<UUID> documentTypeUUIDs = documentTypeIds.stream().map(UUID::fromString).toList();
        return this.expectedFieldRepository.findDtoByDocumentTypeId(documentTypeUUIDs, PageRequest.of(pageNumber, pageSize));
    }

    @Override
    protected List<ExpectedFieldDTO> handleFindByDocumentType(List<String> documentTypeIds) throws Exception {

        List<UUID> documentTypeUUIDs = documentTypeIds.stream().map(UUID::fromString).toList();

        return this.expectedFieldRepository.findDtoByDocumentTypeId(documentTypeUUIDs);
    }

}