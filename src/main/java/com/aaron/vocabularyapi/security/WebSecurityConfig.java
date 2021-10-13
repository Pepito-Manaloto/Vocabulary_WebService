package com.aaron.vocabularyapi.security;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;

import com.aaron.vocabularyapi.response.ResponseError;
import com.aaron.vocabularyapi.util.JsonUtils;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity - used for method authorization if role is implemented @PreAuthorize("hasRole('ADMIN')")
public class WebSecurityConfig
{
    private static final String[] AUTH_WHITELIST = {
            "",
            "/",
            "/token",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/actuator/**",
    };

    private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)
    {
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> handleException(swe, UNAUTHORIZED, e.getMessage()))
                .accessDeniedHandler((swe, e) -> handleException(swe, FORBIDDEN, e.getMessage()))
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager) // This is where jwt checking is done
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated().and()
                .build();
    }

    private Mono<Void> handleException(ServerWebExchange swe, HttpStatus status, String message)
    {
        ServerHttpResponse response = swe.getResponse();
        response.setStatusCode(status);
        response.getHeaders()
                .setContentType(MediaType.APPLICATION_JSON);

        ResponseError body = ResponseError.builder()
                .message(message)
                .build();

        byte[] bodyBytes = JsonUtils.toBytes(body);
        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(bodyBytes);

        return response.writeWith(Mono.just(dataBuffer));
    }
}
