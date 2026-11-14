package bw.co.knowvera.utils;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * Links that go out in emails. An address without a scheme ("localhost:4300", "kyc.example.com")
 * is not a link to an email client: a button pointing at it does nothing.
 */
public final class Urls {

    private Urls() {
    }

    /**
     * The address with a scheme: as given when it has one, else http:// for a local address
     * (localhost, 127.0.0.1) and https:// for any other; null for a blank one. Trailing slashes go.
     */
    public static String withScheme(String address) {
        String trimmed = StringUtils.trimToNull(address);
        if (trimmed == null) {
            return null;
        }
        trimmed = StringUtils.stripEnd(trimmed, "/");
        if (trimmed.matches("(?i)^[a-z][a-z0-9+.-]*://.*")) {
            return trimmed;
        }
        String host = trimmed.toLowerCase(Locale.ROOT);
        boolean local = host.startsWith("localhost") || host.startsWith("127.") || host.endsWith(".localhost")
                || host.contains(".localhost:") || host.startsWith("[::1]");
        return (local ? "http://" : "https://") + trimmed;
    }
}
