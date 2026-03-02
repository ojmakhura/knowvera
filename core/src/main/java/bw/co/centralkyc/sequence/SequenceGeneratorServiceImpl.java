// license-header java merge-point
/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 * TEMPLATE:    SpringServiceImpl.vsl in andromda-spring cartridge
 * MODEL CLASS: AndroMDAModel::backend::bw.co.centralkyc::sequence::SequenceGeneratorService
 * STEREOTYPE:  Service
 */
package bw.co.centralkyc.sequence;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @see bw.co.centralkyc.sequence.SequenceGeneratorService
 */
@Service("sequenceGeneratorService")
@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
public class SequenceGeneratorServiceImpl
    extends SequenceGeneratorServiceBase
{
    public SequenceGeneratorServiceImpl(SequenceGeneratorDao sequenceGeneratorDao,
            SequenceGeneratorRepository sequenceGeneratorRepository, SequenceGeneratorMapper sequenceGeneratorMapper,
            MessageSource messageSource) {
        super(sequenceGeneratorDao, sequenceGeneratorRepository, sequenceGeneratorMapper, messageSource);
        //TODO Auto-generated constructor stub
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#findById(String)
     */
    @Override
    protected SequenceGenerator handleFindById(String id)
        throws Exception
    {

        return this.sequenceGeneratorRepository.findById(UUID.fromString(id)).orElse(null);
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#save(SequenceGenerator)
     */
    @Override
    protected SequenceGenerator handleSave(SequenceGenerator sequenceGenerator)
        throws Exception
    {
        return this.sequenceGeneratorRepository.save(sequenceGenerator);
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#remove(String)
     */
    @Override
    protected boolean handleRemove(String id)
        throws Exception
    {

        this.sequenceGeneratorRepository.deleteById(UUID.fromString(id));
        return true;
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#getAll()
     */
    @Override
    protected Collection<SequenceGenerator> handleGetAll()
        throws Exception
    {
        return this.sequenceGeneratorRepository.findAll();
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#search(String)
     */
    @Override
    protected Collection<SequenceGenerator> handleSearch(String name)
        throws Exception
    {

        Specification<SequenceGenerator> specification = (root, query, criteriaBuilder) -> {
            String pattern = name;
            return criteriaBuilder.equal(root.get("name"), pattern);
        };


        return this.sequenceGeneratorRepository.findAll(specification);
    }

    /**
     * @see bw.co.centralkyc.sequence.SequenceGeneratorService#generateNextSequenceValue(String, Boolean)
     */
    @Override
    protected String handleGenerateNextSequenceValue(String name, Boolean createIfAbsent)
        throws Exception
    {

        Specification<SequenceGenerator> specification = (root, query, criteriaBuilder) -> {
            String pattern = name;
            return criteriaBuilder.equal(root.get("name"), pattern);
        };

        Collection<SequenceGenerator> results = this.sequenceGeneratorRepository.findAll(specification);

        if(CollectionUtils.isEmpty(results)) {

            throw new IllegalArgumentException(
                String.format("No SequenceGenerator found with name '%s'", name)
            );
        }

        SequenceGenerator generator = results.iterator().next();

        Collection<SequencePart> sequenceParts = generator.getSequenceParts().stream()
            .sorted(Comparator.comparingInt(SequencePart::getPosition))
            .toList();

        StringBuilder nextValue = new StringBuilder();

        for(SequencePart part : sequenceParts) {

            switch (part.getType()) {
                case COUNTER:

                    String counterValue = part.getInitialValue();
                    Long counter = Long.valueOf(counterValue);
                    counter++;

                    // Format with leading zeros
                    String formattedCounter = String.format("%0" + counterValue.length() + "d", counter);
                    nextValue.append(formattedCounter);

                    part.setInitialValue(String.format("%0" + counterValue.length() + "d", counter));
                    
                    break;
            
                case MONTH:

                    Long month = Long.valueOf(LocalDate.now().getMonthValue());
                    nextValue.append(month.toString());

                    break;

                case YEAR:

                    Long year = Long.valueOf(LocalDate.now().getYear());
                    nextValue.append(year.toString());

                    break;

                case STATIC:
                    nextValue.append(part.getInitialValue());
                    break;
                default:
                    break;
            }
        }

        this.sequenceGeneratorRepository.save(generator);

        return nextValue.toString();
    }

    @Override
    protected SequenceGenerator handleFindByName(String name) throws Exception {
        
        return this.sequenceGeneratorRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException(
            String.format("No SequenceGenerator found with name '%s'", name)
        ));
    }
}