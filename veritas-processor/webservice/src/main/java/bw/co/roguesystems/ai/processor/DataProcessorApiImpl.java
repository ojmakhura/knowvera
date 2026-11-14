package bw.co.roguesystems.ai.processor;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import bw.co.roguesystems.ai.AuditTracker;
import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import bw.co.roguesystems.ai.logging.Audit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Data Processors", description = "Operations related to data processors.")
public class DataProcessorApiImpl implements DataProcessorApi {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorApi.class);
    private final DataProcessorService dataProcessorService;

    public DataProcessorApiImpl(DataProcessorService dataProcessorService) {
        this.dataProcessorService = dataProcessorService;
    }

    @Override
    @Operation(summary = "Find Data Processor", description = "Get the data processor with the given id")
    public ResponseEntity<DataProcessorDTO> findById(String id) throws Exception {

        logger.debug("Finding data processor with ID: {}", id);
        return ResponseEntity.ok(dataProcessorService.findById(id));

    }

    @Override
    @Operation(summary = "Get All Data Processors", description = "Get all data processors")
    public ResponseEntity<List<DataProcessorDTO>> getAll() throws Exception {

        logger.debug("Retrieving all data processors");
        List<DataProcessorDTO> results = dataProcessorService.getAll();

        if (CollectionUtils.isEmpty(results)) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);

    }

    @Override
    @Operation(summary = "Get All Data Processors Paged", description = "Get all data processors with pagination")
    public ResponseEntity<Page<DataProcessorDTO>> getAllPaged(Integer pageNumber,
            Integer pageSize) throws Exception {

        logger.debug("Retrieving data processors page: {}, size: {}", pageNumber, pageSize);
        Page<DataProcessorDTO> results = dataProcessorService.getAll(pageNumber, pageSize);

        if (CollectionUtils.isEmpty(results.getContent())) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    @Override
    @Operation(summary = "Search Data Processors Paged", description = "Search data processors with pagination")
    public ResponseEntity<Page<DataProcessorDTO>> pagedSearch(String criteria,
            Integer pageNumber,
            Integer pageSize) throws Exception {

        logger.debug("Searching data processors with criteria: {}, page: {}, size: {}", criteria, pageNumber, pageSize);
        Page<DataProcessorDTO> results = dataProcessorService.search(criteria, pageNumber, pageSize);

        if (CollectionUtils.isEmpty(results.getContent())) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);

    }

    @Override
    @Operation(summary = "Remove Data Processor", description = "Remove the data processor with the given id")
    @Audit(entity = "DATA_PROCESSOR", eventLabel = "#id", logData = false)
    public ResponseEntity<Boolean> remove(String id) throws Exception {

        logger.debug("Removing data processor with ID: {}", id);
        return ResponseEntity.ok(dataProcessorService.remove(id));
    }

    @Override
    @Operation(summary = "Save Data Processor", description = "Save the data processor. If the id is not provided, a new data processor will be created.")
    @Audit(entity = "DATA_PROCESSOR", eventLabel = "#dataProcessor.name", logData = false)
    public ResponseEntity<DataProcessorDTO> save(DataProcessorDTO dataProcessor) throws Exception {

        logger.debug("Saving data processor: {}", dataProcessor);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuditTracker.auditTrail(dataProcessor, authentication);

        if (dataProcessor.getExpectedFields() != null) {

            dataProcessor.getExpectedFields().forEach(field -> {
                AuditTracker.auditTrail(field, authentication);
            });
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(dataProcessorService.save(dataProcessor));

    }

    @Override
    @Operation(summary = "Search Data Processors", description = "Search data processors by criteria")
    public ResponseEntity<List<DataProcessorDTO>> search(String criteria) throws Exception {

        logger.debug("Searching data processors with criteria: {}", criteria);
        List<DataProcessorDTO> results = dataProcessorService.search(criteria);

        if (CollectionUtils.isEmpty(results)) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(results);
    }

    @Override
    @Operation(summary = "Add Expected Fields to Data Processor", description = "Add expected fields to the data processor with the given id")
    @Audit(entity = "DATA_PROCESSOR", eventLabel = "#id", logData = true)
    public ResponseEntity<DataProcessorDTO> addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields)
            throws Exception {

        logger.debug("Adding expected fields to data processor with ID: {}", id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        expectedFields.forEach(field -> AuditTracker.auditTrail(field, authentication));

        return ResponseEntity.ok(dataProcessorService.addExpectedField(id, expectedFields));
    }
}
