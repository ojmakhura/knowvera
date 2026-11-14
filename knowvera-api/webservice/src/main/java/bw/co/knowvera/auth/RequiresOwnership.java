package bw.co.knowvera.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Callers holding the Keycloak permission {@code <scope>} may invoke the method on any record;
 * callers holding only {@code <scope>-own} (owner-scoped roles such as APPLICANT and ORG_ADMIN)
 * must own the record. Enforced by {@link KycAuthorisationService} after @PreAuthorize.
 *
 * <pre>
 * &#64;RequiresOwnership(scope = "kyc-records:view", target = "KYC_RECORD", id = "#id")
 * &#64;RequiresOwnership(scope = "kyc-records:submit", target = "#record.target", id = "#record.targetId")
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresOwnership {

	/** The Keycloak permission as {@code <resource>:<scope>}, e.g. "kyc-records:view". */
	String scope();

	/**
	 * A TargetEntity name, e.g. "KYC_RECORD", or a SpEL expression over the method
	 * parameters (starting with '#') that yields a TargetEntity or its name.
	 */
	String target();

	/** SpEL expression over the method parameters yielding the record id, e.g. "#id". */
	String id();

	/**
	 * For a save whose body names its owner (target / id above): the stored record's type, e.g.
	 * "KYC_RECORD". When {@link #recordId()} yields an id, the caller must also own the stored
	 * record, so an update cannot take over another owner's record by naming itself as owner.
	 */
	String record() default "";

	/** SpEL expression yielding the id of the record being updated, e.g. "#kycRecord.id". */
	String recordId() default "";
}
