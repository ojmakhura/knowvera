package bw.co.knowvera.auth;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Callers with an owner-scoped role (APPLICANT, ORG_ADMIN) may only invoke the method on
 * records they own; other callers are not affected. Enforced by {@link KycAuthorisationService}
 * after @PreAuthorize has checked the Keycloak permission.
 *
 * <pre>
 * &#64;RequiresOwnership(target = "KYC_RECORD", id = "#id")
 * &#64;RequiresOwnership(target = "#record.target", id = "#record.targetId")
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresOwnership {

	/**
	 * A TargetEntity name, e.g. "KYC_RECORD", or a SpEL expression over the method
	 * parameters (starting with '#') that yields a TargetEntity or its name.
	 */
	String target();

	/** SpEL expression over the method parameters yielding the record id, e.g. "#id". */
	String id();
}
