package bw.co.knowvera.organisation.branch;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BranchRepository extends JpaRepository<Branch, UUID>, JpaSpecificationExecutor<Branch> {

    @Query(
        value = """
            SELECT b
            FROM Branch b
            JOIN FETCH b.organisation
        """
    )
    java.util.List<bw.co.knowvera.organisation.branch.Branch> findAll();

    @Query(
        value = """
            SELECT b
            FROM Branch b
            JOIN FETCH b.organisation
        """
    )
    Page<Branch> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT b
            FROM Branch b
            JOIN FETCH b.organisation
            WHERE b.id = :id
        """
    )
    Optional<bw.co.knowvera.organisation.branch.Branch> findById(java.util.UUID id);

}
