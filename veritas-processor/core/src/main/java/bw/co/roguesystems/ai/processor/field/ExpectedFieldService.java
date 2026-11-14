package bw.co.roguesystems.ai.processor.field;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldService
 */
public interface ExpectedFieldService
{
    /**
     * TODO: Model Documentation for
     * bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findById
     * @param id TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findById(id)
     * @return ExpectedFieldDTO
     */
    public ExpectedFieldDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldService.save
     * @param expectedField TODO: Model Documentation for
bw.co.roguesystems.ai.processor.field.ExpectedFieldService.save(expectedField)
     * @return ExpectedFieldDTO
     */
    public ExpectedFieldDTO save(@Valid ExpectedFieldDTO expectedField) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldService.remove
     * @param id TODO: Model Documentation for bw.co.roguesystems.ai.processor.field.ExpectedFieldService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for
     * bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor
     * @param dataProcessorIds TODO: Model Documentation for
bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor(dataProcessorIds)
     * @param pageNumber TODO: Model Documentation for
bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor(pageNumber)
     * @param pageSize TODO: Model Documentation for
bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor(pageSize)
     * @return Page<ExpectedFieldDTO>
     */
    public Page<ExpectedFieldDTO> findByDataProcessor(List<String> dataProcessorIds, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor
     * @param dataProcessorIds TODO: Model Documentation for
bw.co.roguesystems.ai.processor.field.ExpectedFieldService.findByDataProcessor(dataProcessorIds)
     * @return List<ExpectedFieldDTO>
     */
    public List<ExpectedFieldDTO> findByDataProcessor(List<String> dataProcessorIds) ;

}