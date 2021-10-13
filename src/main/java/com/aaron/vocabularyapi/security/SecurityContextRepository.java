package com.aaron.vocabularyapi.security;

import static org.springframework.http.HttpHeaders.*;
import static org.apache.commons.lang3.StringUtils.*;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository
{
    private static final String BEARER_HEADER = "Bearer ";

    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc)
    {
        log.info("save");
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange)
    {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders()
                .getFirst(AUTHORIZATION);

        log.info("load. authHeader={}", authHeader);
        if(isNotBlank(authHeader) && authHeader.startsWith(BEARER_HEADER))
        {
            String authToken = authHeader.substring(BEARER_HEADER.length());
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);

            return this.authenticationManager.authenticate(auth)
                    .map(SecurityContextImpl::new);
        }
        else
        {
            return Mono.empty();
        }
    }
}
