package bw.co.knowvera.sequence;

import jakarta.validation.Valid;
import java.util.List;

/**
 * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService
 */
public interface SequenceGeneratorService
{
    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.findById
     * @param id TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.findById(id)
     * @return SequenceGeneratorDTO
     */
    public SequenceGeneratorDTO findById(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.save
     * @param sequenceGenerator TODO: Model Documentation for
bw.co.knowvera.sequence.SequenceGeneratorService.save(sequenceGenerator)
     * @return SequenceGeneratorDTO
     */
    public SequenceGeneratorDTO save(@Valid SequenceGeneratorDTO sequenceGenerator) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.remove
     * @param id TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.remove(id)
     * @return boolean
     */
    public boolean remove(String id) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.getAll
     * @return List<SequenceGeneratorDTO>
     */
    public List<SequenceGeneratorDTO> getAll() ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.search
     * @param criteria TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.search(criteria)
     * @return List<SequenceGeneratorDTO>
     */
    public List<SequenceGeneratorDTO> search(String criteria) ;

    /**
     * TODO: Model Documentation for
     * bw.co.knowvera.sequence.SequenceGeneratorService.generateNextSequenceValue
     * @param name TODO: Model Documentation for
bw.co.knowvera.sequence.SequenceGeneratorService.generateNextSequenceValue(name)
     * @param createIfAbsent TODO: Model Documentation for
bw.co.knowvera.sequence.SequenceGeneratorService.generateNextSequenceValue(createIfAbsent)
     * @return String
     */
    public String generateNextSequenceValue(String name, Boolean createIfAbsent) ;

    /**
     * TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.findByName
     * @param name TODO: Model Documentation for bw.co.knowvera.sequence.SequenceGeneratorService.findByName(name)
     * @return SequenceGeneratorDTO
     */
    public SequenceGeneratorDTO findByName(String name) ;

}