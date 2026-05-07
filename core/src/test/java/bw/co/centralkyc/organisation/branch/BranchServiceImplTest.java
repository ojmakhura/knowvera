package bw.co.centralkyc.organisation.branch;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private BranchDao branchDao;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BranchMapper branchMapper;
    @Mock
    private MessageSource messageSource;

    private BranchServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BranchServiceImpl(branchDao, branchRepository, branchMapper, messageSource);
    }

    @Test
    void handleSaveMapsPersistsAndReturnsDto() throws Exception {
        BranchDTO input = new BranchDTO();
        Branch branch = Branch.Factory.newInstance();
        Branch savedBranch = Branch.Factory.newInstance();
        BranchDTO expected = new BranchDTO();

        when(branchDao.branchDTOToEntity(input)).thenReturn(branch);
        when(branchRepository.save(branch)).thenReturn(savedBranch);
        when(branchDao.toBranchDTO(savedBranch)).thenReturn(expected);

        BranchDTO actual = service.handleSave(input);

        assertSame(expected, actual);
        verify(branchRepository).save(branch);
    }

    @Test
    void handleRemoveDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.handleRemove(id.toString());

        assertTrue(removed);
        verify(branchRepository).deleteById(id);
    }

    @Test
    void handleGetAllDelegatesToMapper() throws Exception {
        List<Branch> branches = List.of(Branch.Factory.newInstance());
        List<BranchDTO> expected = List.of(new BranchDTO());

        when(branchRepository.findAll()).thenReturn(branches);
        when(branchMapper.toBranchDTOCollection(branches)).thenReturn(expected);

        List<BranchDTO> actual = service.handleGetAll();

        assertSame(expected, actual);
    }
}