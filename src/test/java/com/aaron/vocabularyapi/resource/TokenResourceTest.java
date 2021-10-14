package com.aaron.vocabularyapi.resource;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.aaron.vocabularyapi.config.MessageResourceBundleConfig;
import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.request.RequestToken;
import com.aaron.vocabularyapi.security.AuthenticationManager;
import com.aaron.vocabularyapi.security.SecurityContextRepository;
import com.aaron.vocabularyapi.security.WebSecurityConfig;
import com.aaron.vocabularyapi.service.impl.MessageResourceServiceImpl;
import com.aaron.vocabularyapi.service.impl.TokenServiceImpl;
import com.aaron.vocabularyapi.util.JwtUtils;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@Import({ TokenServiceImpl.class, MessageResourceBundleConfig.class, MessageResourceServiceImpl.class, VocabularyApiProperties.class,
        JwtUtils.class, WebSecurityConfig.class, AuthenticationManager.class, SecurityContextRepository.class })
@WebFluxTest(controllers = TokenResource.class)
public class TokenResourceTest
{
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private VocabularyApiProperties properties;

    @Test
    public void authenticate_failed()
    {
        RequestToken request = new RequestToken();
        request.setPassword(UUID.randomUUID().toString());

        webClient//.mutate().responseTimeout(Duration.ofHours(1)).build() // for debugging
                .post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isEqualTo(UNAUTHORIZED)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Request token password is not valid.");
    }

    @Test
    public void authenticate_success()
    {
        String password = md5DigestAsHex(properties.getAuthenticationPassword().getBytes());
        RequestToken request = new RequestToken();
        request.setPassword(password);

        webClient.post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").isNotEmpty();
    }
}
