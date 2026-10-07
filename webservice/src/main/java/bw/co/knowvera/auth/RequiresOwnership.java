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
}
