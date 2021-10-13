package com.aaron.vocabularyapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

// https://medium.com/analytics-vidhya/spring-cloud-config-server-and-good-practice-of-refresh-scope-usage-ef65d0fee379
@Data
@Component
@RefreshScope
@ConfigurationProperties
public class VocabularyApiProperties
{
    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.webflux.base-path}")
    private String basePath;

    @Value("${message.resource.basename}")
    private String messageResourceBaseName;

    @Value("${security.authentication.password}")
    private String authenticationPassword;

    @Value("${security.jjwt.secret}")
    private String jwtSecret;

    @Value("${security.jjwt.expiration:30}")
    private long jwtExpirationMinutes;
}
