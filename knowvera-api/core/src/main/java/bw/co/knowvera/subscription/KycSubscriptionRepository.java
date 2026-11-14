package bw.co.knowvera.subscription;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface KycSubscriptionRepository extends JpaRepository<KycSubscription, UUID>, JpaSpecificationExecutor<KycSubscription> {

    Optional<java.lang.Long> countByStatus(bw.co.knowvera.subscription.KycSubsciptionStatus status);

    @Query(
        value = """
            SELECT s
FROM KycSubscription s
JOIN FETCH s.organisation o
        """
    )
    java.util.List<bw.co.knowvera.subscription.KycSubscription> findAll();

    @Query(
        value = """
            SELECT s
FROM KycSubscription s
JOIN FETCH s.organisation o
        """
    )
    Page<KycSubscription> findAll(Pageable pageable);

    @Query(
        value = """
            SELECT s
FROM KycSubscription s
JOIN FETCH s.organisation o
WHERE s.id = :id
        """
    )
    Optional<bw.co.knowvera.subscription.KycSubscription> findById(java.util.UUID id);

}
