package com.aaron.vocabularyapi.service.impl;

import static org.springframework.util.DigestUtils.md5DigestAsHex;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.service.TokenService;
import com.aaron.vocabularyapi.util.JwtUtils;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest(classes = { TokenServiceImpl.class, JwtUtils.class, VocabularyApiProperties.class })
public class TokenServiceImplTest
{
    @Autowired
    private TokenService service;

    @Autowired
    private VocabularyApiProperties properties;

    @Test
    public void authenticate_nullPassword()
    {
        Mono<String> mono = service.authenticate(null);

        StepVerifier.create(mono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void authenticate_blankPassword()
    {
        Mono<String> mono = service.authenticate("");

        StepVerifier.create(mono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void authenticate_invalidPassword()
    {
        Mono<String> mono = service.authenticate(UUID.randomUUID().toString());

        StepVerifier.create(mono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void authenticate_correctPassword()
    {
        String password = md5DigestAsHex(properties.getAuthenticationPassword().getBytes());
        Mono<String> mono = service.authenticate(password);

        StepVerifier.create(mono)
                .expectNextCount(1)
                .verifyComplete();
    }
}
