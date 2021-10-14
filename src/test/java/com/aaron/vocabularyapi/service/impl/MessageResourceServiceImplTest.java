package com.aaron.vocabularyapi.service.impl;

import static com.aaron.vocabularyapi.constant.ErrorCode.*;
import static java.util.Arrays.*;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import com.aaron.vocabularyapi.config.MessageResourceBundleConfig;
import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.exception.NotFoundException;
import com.aaron.vocabularyapi.service.MessageResourceService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest(classes = { MessageResourceServiceImpl.class, MessageResourceBundleConfig.class, VocabularyApiProperties.class })
public class MessageResourceServiceImplTest
{
    @Autowired
    private MessageResourceService service;

    @Test
    public void errorMessage_nullCode()
    {
        Mono<Throwable> mono = service.errorMessage(null);

        StepVerifier.create(mono)
                .expectError(Exception.class)
                .verify();
    }

    @Test
    public void errorMessage_withCode()
    {
        String param1 = UUID.randomUUID().toString();
        List<String> param2 = asList("hello", "world");
        Mono<Throwable> mono = service.errorMessage(VOCABULARIES_NOT_FOUND, param1, param2);

        String expectedMessage = String.format("Vocabularies not found param1-test=%s param2-test=%s.",
                param1, param2);

        Predicate<Throwable> expectCorrectExceptionAndMessage = e -> NotFoundException.class.equals(e.getClass()) &&
                expectedMessage.equals(e.getMessage());
        StepVerifier.create(mono)
                .expectErrorMatches(expectCorrectExceptionAndMessage)
                .verify();
    }
}
