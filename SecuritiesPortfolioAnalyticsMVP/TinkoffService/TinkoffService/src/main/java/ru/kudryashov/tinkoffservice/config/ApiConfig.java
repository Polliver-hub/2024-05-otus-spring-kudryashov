package ru.kudryashov.tinkoffservice.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "api")
@RequiredArgsConstructor
@Getter
public class ApiConfig {

    private final boolean isSandBoxMode;

    private final String token;
}
