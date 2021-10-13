package com.aaron.vocabularyapi.service;

import com.aaron.vocabularyapi.constant.ErrorCode;

import reactor.core.publisher.Mono;

public interface MessageResourceService
{
    <T> Mono<T> errorMessage(ErrorCode code, Object... replacements);
}
