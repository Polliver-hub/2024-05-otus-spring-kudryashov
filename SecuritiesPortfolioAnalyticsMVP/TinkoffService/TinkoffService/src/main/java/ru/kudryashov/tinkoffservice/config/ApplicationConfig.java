package ru.kudryashov.tinkoffservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.piapi.core.InvestApi;

@Configuration
@EnableConfigurationProperties(ApiConfig.class)
@RequiredArgsConstructor
public class ApplicationConfig {

    private final ApiConfig apiConfig;

    @Bean
    public InvestApi investApi() {
        return apiConfig.isSandBoxMode()
                ?
                InvestApi.createSandbox(apiConfig.getToken())
                :
                InvestApi.create(apiConfig.getToken());
    }
}
