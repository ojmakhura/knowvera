package bw.co.roguesystems.ai.processor;

import bw.co.roguesystems.ai.processor.field.ExpectedFieldDTO;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;

/**
 * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService
 */
public interface DataProcessorService
{
    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.findById
     * @param id TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.findById(id)
     * @return DataProcessorDTO
     */
    public DataProcessorDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.save
     * @param dataProcessor TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.save(dataProcessor)
     * @return DataProcessorDTO
     */
    public DataProcessorDTO save(@Valid DataProcessorDTO dataProcessor) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.remove
     * @param id TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.getAll
     * @return List<DataProcessorDTO>
     */
    public List<DataProcessorDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search
     * @param criteria TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search(criteria)
     * @return List<DataProcessorDTO>
     */
    public List<DataProcessorDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.getAll
     * @param pageNumber TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.getAll(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.getAll(pageSize)
     * @return Page<DataProcessorDTO>
     */
    public Page<DataProcessorDTO> getAll(Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search
     * @param criteria TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search(criteria)
     * @param pageNumber TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search(pageNumber)
     * @param pageSize TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.search(pageSize)
     * @return Page<DataProcessorDTO>
     */
    public Page<DataProcessorDTO> search(String criteria, Integer pageNumber, Integer pageSize) ;

    /**
     * TODO: Model Documentation for
     * bw.co.roguesystems.ai.processor.DataProcessorService.addExpectedField
     * @param id TODO: Model Documentation for bw.co.roguesystems.ai.processor.DataProcessorService.addExpectedField(id)
     * @param expectedFields TODO: Model Documentation for
bw.co.roguesystems.ai.processor.DataProcessorService.addExpectedField(expectedFields)
     * @return DataProcessorDTO
     */
    public DataProcessorDTO addExpectedField(String id, @Valid Set<ExpectedFieldDTO> expectedFields) ;

}