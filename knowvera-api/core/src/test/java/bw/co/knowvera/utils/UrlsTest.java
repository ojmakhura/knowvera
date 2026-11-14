package bw.co.knowvera.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UrlsTest {

    @Test
    void anAddressWithoutASchemeGetsOne() {
        assertEquals("http://localhost:4300", Urls.withScheme("localhost:4300"));
        assertEquals("http://127.0.0.1:4300", Urls.withScheme(" 127.0.0.1:4300/ "));
        assertEquals("http://portal.localhost", Urls.withScheme("portal.localhost"));
        assertEquals("https://kyc.example.com/portal", Urls.withScheme("kyc.example.com/portal/"));
    }

    @Test
    void anAddressWithASchemeIsKept() {
        assertEquals("http://kyc.example.com", Urls.withScheme("http://kyc.example.com"));
        assertEquals("https://kyc.example.com", Urls.withScheme("https://kyc.example.com/"));
    }

    @Test
    void aBlankAddressIsNone() {
        assertNull(Urls.withScheme(null));
        assertNull(Urls.withScheme("  "));
    }
}
