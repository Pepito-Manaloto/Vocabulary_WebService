package com.aaron.vocabularyapi.service;

import reactor.core.publisher.Mono;

public interface TokenService
{
    Mono<String> authenticate(String password);
}
