package com.aaron.vocabularyapi.exception;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import com.aaron.vocabularyapi.response.ResponseError;
import com.aaron.vocabularyapi.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@Order(-2)
public class GlobalErrorHandler implements ErrorWebExceptionHandler
{
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable)
    {
        log.error("handle. Error.", throwable);

        HttpStatus status;
        if(throwable instanceof NotFoundException)
        {
            status = NOT_FOUND;
        }
        else if(throwable instanceof BadRequestException)
        {
            status = BAD_REQUEST;
        }
        else if(throwable instanceof InvalidTokenPasswordException)
        {
            status = UNAUTHORIZED;
        }
        else
        {
            status = INTERNAL_SERVER_ERROR;
        }

        String message = getExceptionMessage(throwable);
        
        ServerHttpResponse response = serverWebExchange.getResponse();
        Mono<DataBuffer> monoBody = errorResponseBody(status, message, response);

        return response.writeWith(monoBody);
    }

    private String getExceptionMessage(Throwable e)
    {
        String message;
        if(e instanceof WebExchangeBindException)
        {
            message = ((WebExchangeBindException) e).getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(joining(" "));
        }
        else
        {
            message = e.getMessage();
        }

        return message;
    }

    private Mono<DataBuffer> errorResponseBody(HttpStatus status, String message, ServerHttpResponse response)
    {
        response.setStatusCode(status);
        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ResponseError body = ResponseError.builder()
                .message(message)
                .build();

        byte[] bodyBytes = JsonUtils.toBytes(body);
        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(bodyBytes);

        return Mono.just(dataBuffer);
    }
}