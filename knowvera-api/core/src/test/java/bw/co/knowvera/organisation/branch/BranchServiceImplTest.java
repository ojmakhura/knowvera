package bw.co.knowvera.organisation.branch;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BranchServiceImplTest {

    @Mock
    private BranchRepository branchRepository;
    @Mock
    private BranchMapper branchMapper;

    private BranchServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BranchServiceImpl(branchRepository, branchMapper);
    }

    @Test
    void saveMapsPersistsAndReturnsDto() throws Exception {
        BranchDTO input = new BranchDTO();
        input.setCode("BR-1");
        input.setName("Main Branch");
        input.setOrganisationId(UUID.randomUUID().toString());
        input.setOrganisation("ORG");
        Branch branch = Branch.Factory.newInstance();
        Branch savedBranch = Branch.Factory.newInstance();
        BranchDTO expected = new BranchDTO();

        when(branchMapper.branchDTOToEntity(input)).thenReturn(branch);
        when(branchRepository.save(branch)).thenReturn(savedBranch);
        when(branchMapper.toBranchDTO(savedBranch)).thenReturn(expected);

        BranchDTO actual = service.save(input);

        assertSame(expected, actual);
        verify(branchRepository).save(branch);
    }

    @Test
    void removeDeletesById() throws Exception {
        UUID id = UUID.randomUUID();

        boolean removed = service.remove(id.toString());

        assertTrue(removed);
        verify(branchRepository).deleteById(id);
    }

    @Test
    void getAllDelegatesToMapper() throws Exception {
        List<Branch> branches = List.of(Branch.Factory.newInstance());
        List<BranchDTO> expected = List.of(new BranchDTO());

        when(branchRepository.findAll()).thenReturn(branches);
        when(branchMapper.toBranchDTOCollection(branches)).thenReturn(expected);

        List<BranchDTO> actual = service.getAll();

        assertSame(expected, actual);
    }

    @Test
    void findByIdMapsEntity() throws Exception {
        UUID id = UUID.randomUUID();
        Branch entity = Branch.Factory.newInstance();
        BranchDTO expected = new BranchDTO();

        when(branchRepository.getReferenceById(id)).thenReturn(entity);
        when(branchMapper.toBranchDTO(entity)).thenReturn(expected);

        BranchDTO actual = service.findById(id.toString());

        assertSame(expected, actual);
    }

    @Test
    void searchAndFindByOrganisationPathsMapResults() throws Exception {
        List<Branch> entities = List.of(Branch.Factory.newInstance());
        List<BranchDTO> expected = List.of(new BranchDTO());

        when(branchRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Branch>>any(), any(Sort.class)))
                .thenReturn(entities);
        when(branchMapper.toBranchDTOCollection(entities)).thenReturn(expected);

        List<BranchDTO> searchResult = service.search("main");
        List<BranchDTO> byOrgResult = service.findByOrganisation(UUID.randomUUID().toString());

        assertSame(expected, searchResult);
        assertSame(expected, byOrgResult);
    }

    @Test
    void pagedSearchPathsMapResults() throws Exception {
        Branch entity = Branch.Factory.newInstance();
        BranchDTO dto = new BranchDTO();
        Page<Branch> page = new PageImpl<>(List.of(entity));

        when(branchRepository.findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "name")))).thenReturn(page);
        when(branchRepository.findAll(org.mockito.ArgumentMatchers.<Specification<Branch>>any(), any(PageRequest.class)))
                .thenReturn(page);
        when(branchMapper.toBranchDTO(entity)).thenReturn(dto);

        Page<BranchDTO> allPage = service.getAll(0, 5);
        Page<BranchDTO> searchPage = service.search("main", 0, 5);
        Page<BranchDTO> byOrgPage = service.findByOrganisation(UUID.randomUUID().toString(), 0, 5);

        assertEquals(1, allPage.getContent().size());
        assertSame(dto, allPage.getContent().get(0));
        assertEquals(1, searchPage.getContent().size());
        assertEquals(1, byOrgPage.getContent().size());
    }

    @Test
    void serviceBaseGuardsRejectInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
        assertThrows(IllegalArgumentException.class, () -> service.findById(" "));
        assertThrows(IllegalArgumentException.class, () -> service.save(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove(null));
        assertThrows(IllegalArgumentException.class, () -> service.remove("\n"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation("\t"));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(null, 0, 10));
        assertThrows(IllegalArgumentException.class, () -> service.findByOrganisation(" ", 0, 10));
    }
}