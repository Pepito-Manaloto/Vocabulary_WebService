package com.aaron.vocabularyapi.service.impl;

import static java.util.Objects.*;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.aaron.vocabularyapi.constant.ErrorCode;
import com.aaron.vocabularyapi.service.MessageResourceService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Service
public class MessageResourceServiceImpl implements MessageResourceService
{
    private MessageSource messageSource;

    @Override
    public <T> Mono<T> errorMessage(ErrorCode code, Object... replacements)
    {
        Exception ex;
        if(nonNull(code))
        {
            String msgSourceCode = code.getMessageSourceCode();
            String errMsg = messageSource.getMessage(msgSourceCode, replacements, Locale.US);

            ex = code.getExceptionSupplier()
                    .apply(errMsg);

            log.info("errorMessage. Resolved message. code={} replacements={} exception={}",
                    msgSourceCode, Arrays.toString(replacements), ex.getClass().getSimpleName());
        }
        else
        {
            ex = new Exception();
        }

        return Mono.error(ex);
    }
}
