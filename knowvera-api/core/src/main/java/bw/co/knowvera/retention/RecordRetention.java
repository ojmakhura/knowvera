package bw.co.knowvera.retention;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import bw.co.knowvera.TargetEntity;
import bw.co.knowvera.kyc.KycRecord;
import bw.co.knowvera.kyc.KycRecordRepository;

/**
 * How long customer due diligence records are kept after a relationship ends. The Financial
 * Intelligence Act 2022 (s.32) requires 20 years from the end of the business relationship, so
 * removing an individual, organisation or KYC record first closes it; it can only be deleted
 * once its retention period has passed.
 */
@Component
public class RecordRetention {

    private final int retentionYears;
    private final KycRecordRepository kycRecordRepository;

    public RecordRetention(@Value("${app.retention.years:20}") int retentionYears,
            KycRecordRepository kycRecordRepository) {
        this.retentionYears = retentionYears;
        this.kycRecordRepository = kycRecordRepository;
    }

    /** The date until which a record closed at {@code closedAt} must be kept. */
    public LocalDate retainUntil(LocalDateTime closedAt) {
        return closedAt.toLocalDate().plusYears(retentionYears);
    }

    /** Whether a closed record has been kept for its full retention period. */
    public boolean mayDelete(LocalDate retainUntil, LocalDate today) {
        return retainUntil != null && !today.isBefore(retainUntil);
    }

    /** Why a closed record cannot be deleted yet. */
    public String retainedMessage(String record, LocalDate retainUntil) {
        return record + " is closed and must be kept until " + retainUntil + " (Financial Intelligence Act s.32).";
    }

    /** Closes the open KYC records of an individual or organisation whose relationship has ended. */
    public void closeKycRecords(TargetEntity target, String targetId, LocalDateTime closedAt) {
        setKycRecordsClosed(target, targetId, closedAt);
    }

    /** Reopens the KYC records of a returning customer. */
    public void reopenKycRecords(TargetEntity target, String targetId) {
        setKycRecordsClosed(target, targetId, null);
    }

    private void setKycRecordsClosed(TargetEntity target, String targetId, LocalDateTime closedAt) {

        List<KycRecord> records = kycRecordRepository.findAll((root, query, builder) -> builder.and(
                builder.equal(root.get("target"), target),
                builder.equal(root.get("targetId"), targetId)));

        for (KycRecord record : records) {
            if ((closedAt == null) == (record.getClosedAt() == null)) {
                continue;
            }
            record.setClosedAt(closedAt);
            record.setRetainUntil(closedAt == null ? null : retainUntil(closedAt));
        }

        kycRecordRepository.saveAll(records);
    }
}
