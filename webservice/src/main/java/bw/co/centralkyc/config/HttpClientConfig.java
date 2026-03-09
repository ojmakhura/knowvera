package bw.co.centralkyc.config;

import bw.co.centralkyc.extractor.LmStudioExtractor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices({
        LmStudioExtractor.class,
})
public class HttpClientConfig {
}
