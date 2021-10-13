package com.aaron.vocabularyapi.constant;

import java.util.function.Function;

import com.aaron.vocabularyapi.exception.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode
{
    VOCABULARIES_NOT_FOUND("error.vocabularies.not_found", (msg) -> new NotFoundException(msg)),
    TOKEN_UNAUTHORIZED("error.token.unauthorized", (msg) -> new InvalidTokenPasswordException(msg));

    private String messageSourceCode;
    private Function<String, ? extends Exception> exceptionSupplier;
}
