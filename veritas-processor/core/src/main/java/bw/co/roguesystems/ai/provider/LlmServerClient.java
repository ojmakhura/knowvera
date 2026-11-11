package bw.co.roguesystems.ai.provider;

import java.net.http.HttpClient;

import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import bw.co.roguesystems.ai.config.VeritasProperties;

/**
 * The HTTP client for a local model server (LM Studio, Ollama), with the server's timeouts: a
 * model that hangs must not hold a job forever.
 */
public final class LlmServerClient {

    private LlmServerClient() {
    }

    public static RestClient of(RestClient.Builder builder, VeritasProperties.LlmServer server) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(server.getConnectTimeout())
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(server.getReadTimeout());
        return builder.clone().baseUrl(server.getBaseUrl()).requestFactory(requestFactory).build();
    }

    /** A failed call: worth another try when the server was unreachable, timed out or failed itself. */
    public static ProviderException failure(String server, RestClientException e) {
        boolean retryable = e instanceof ResourceAccessException || e instanceof HttpServerErrorException;
        return new ProviderException(server + " call failed: " + e.getMessage(), e, retryable);
    }
}
