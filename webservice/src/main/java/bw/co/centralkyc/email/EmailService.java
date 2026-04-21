package bw.co.centralkyc.email;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import bw.co.roguesystems.comm.message.CommMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @Value("${app.comm.base-url}")
    private String commUrl;

    @Value("${app.comm.send-message}")
    private String sendMessageUrl;

    private final RestTemplate restTemplate;

    @Async("virtualThreadExecutor")
    public void sendEmail(Collection<CommMessageDTO> messages) {

        if (CollectionUtils.isEmpty(messages)) {
            return;
        }

        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();

        System.out.println(authentication.getToken().getTokenValue());
        String token = authentication.getToken().getTokenValue();
        // restTemplate.
        System.out.println(sendMessageUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<Collection<CommMessageDTO>> entity = new HttpEntity<Collection<CommMessageDTO>>(messages, headers);

        ResponseEntity<List<CommMessageDTO>> response = restTemplate.exchange(sendMessageUrl, HttpMethod.POST,
                entity, new ParameterizedTypeReference<List<CommMessageDTO>>() {

                });

        log.info("Sent {} messages.", response.getBody().size());
    }
}
