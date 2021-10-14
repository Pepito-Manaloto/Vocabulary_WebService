package com.aaron.vocabularyapi.resource;

import static com.aaron.vocabularyapi.constant.ErrorCode.TOKEN_UNAUTHORIZED;
import static reactor.core.publisher.Mono.*;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aaron.vocabularyapi.request.RequestToken;
import com.aaron.vocabularyapi.response.ResponseToken;
import com.aaron.vocabularyapi.service.MessageResourceService;
import com.aaron.vocabularyapi.service.TokenService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
public class TokenResource
{
    private MessageResourceService msgResourceService;
    private TokenService tokenService;

    @PostMapping(path = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Authenticate to get jwt token", notes = "Authentication token retrieval")
    public Mono<ResponseEntity<ResponseToken>> authenticate(@Valid @RequestBody RequestToken request)
    {
        log.info("login. Start. request={}", request);

        return tokenService.authenticate(request.getPassword())
                .switchIfEmpty(defer(() -> msgResourceService.errorMessage(TOKEN_UNAUTHORIZED)))
                .map(token -> ResponseEntity.ok(new ResponseToken(token)));
    }
}
