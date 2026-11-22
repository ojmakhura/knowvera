package bw.co.knowvera.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import bw.co.knowvera.organisation.OrganisationServiceException;
import bw.co.knowvera.organisation.client.ClientRequestServiceException;

/** What callers are told when a service fails: never the exception's own text. */
class ServiceErrorsTest {

    private static final String SQL = "could not execute statement [ERROR: duplicate key value violates unique "
            + "constraint \"ukne9fy08l1kc6k1wtx1b75l8s8\"] [insert into client_request (document_fk) values (?)]";

    @Test
    void aDatabaseConflictIsA409InPlainWords() {
        DataIntegrityViolationException conflict = new DataIntegrityViolationException(SQL);
        ClientRequestServiceException wrapped = new ClientRequestServiceException(
                "Error performing 'ClientRequestService.uploadRequests(...)' --> " + conflict, conflict);

        assertEquals(HttpStatus.CONFLICT, ServiceErrors.status(wrapped));
        assertEquals(ServiceErrors.CONFLICT, ServiceErrors.message(wrapped));
        assertFalse(ServiceErrors.message(wrapped).contains("constraint"));
    }

    @Test
    void anUnexpectedFailureIsA500WithoutTheExceptionText() {
        IllegalStateException failure = new IllegalStateException("NullPointerException at Foo.java:12");
        OrganisationServiceException wrapped = new OrganisationServiceException(
                "Error performing 'OrganisationService.save(...)' --> " + failure, failure);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ServiceErrors.status(wrapped));
        assertEquals(ServiceErrors.UNEXPECTED, ServiceErrors.message(wrapped));
    }

    @Test
    void aServicesOwnRefusalReachesTheCaller() {
        OrganisationServiceException refusal = new OrganisationServiceException(
                "Organisation o1 is retained until 2031-11-22 and cannot be removed.");
        OrganisationServiceException wrapped = new OrganisationServiceException(
                "Error performing 'OrganisationService.remove(String id)' --> " + refusal, refusal);

        assertEquals(HttpStatus.BAD_REQUEST, ServiceErrors.status(wrapped));
        assertEquals("Organisation o1 is retained until 2031-11-22 and cannot be removed.",
                ServiceErrors.message(wrapped));
    }
}
