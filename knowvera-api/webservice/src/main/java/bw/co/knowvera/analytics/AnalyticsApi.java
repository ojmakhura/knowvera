package bw.co.knowvera.analytics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * TODO: Model Documentation for bw.co.knowvera.analytics.AnalyticsApi
 */
@RequestMapping("/analytics")
public interface AnalyticsApi {

    @GetMapping(
        value = "/count"
    )
    public ResponseEntity<CountDTO> countAnalytics() throws Exception;

    @GetMapping(
        value = "/count/by-organisation/{organisationId}"
    )
    public ResponseEntity<CountDTO> organisationCountAnalytics(@PathVariable(name = "organisationId", required = true) String organisationId) throws Exception;
}