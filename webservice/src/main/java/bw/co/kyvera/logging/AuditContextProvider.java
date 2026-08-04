package bw.co.kyvera.logging;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import bw.co.kyvera.audit.AuditContext;

@Component
public class AuditContextProvider {

    public AuditContext getContext() {

        AuditContext context = new AuditContext();

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {

            HttpServletRequest request = attributes.getRequest();

            context.setIpAddress(
                    IpAddressUtils.getClientIpAddress(request));

            context.setUserAgent(
                    request.getHeader("User-Agent"));

            context.setMethod(
                    request.getMethod());

            context.setUri(
                    request.getRequestURI());
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            context.setUsername(authentication.getName());
            Jwt jwt = (Jwt) authentication.getPrincipal();
            context.setUserId(jwt.getSubject());
        }

        SpanContext spanContext =
                Span.current().getSpanContext();

        if (spanContext.isValid()) {

            context.setTraceId(
                    spanContext.getTraceId());

            context.setSpanId(
                    spanContext.getSpanId());
        }

        return context;
    }
}
