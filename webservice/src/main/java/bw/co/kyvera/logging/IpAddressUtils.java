package bw.co.kyvera.logging;

import jakarta.servlet.http.HttpServletRequest;

public final class IpAddressUtils {

    private IpAddressUtils() {
    }

    public static String getClientIpAddress(HttpServletRequest request) {

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "Forwarded"
        };

        for (String header : headers) {

            String value = request.getHeader(header);

            if (value != null
                    && !value.isBlank()
                    && !"unknown".equalsIgnoreCase(value)) {

                return value.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
