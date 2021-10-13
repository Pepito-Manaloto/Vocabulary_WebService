package com.aaron.vocabularyapi.security;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.aaron.vocabularyapi.util.JwtUtils;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager
{
    private JwtUtils jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication)
    {
        log.info("authenticate. authentication={}", authentication);
        // See @SecurityContextRepository both credentials and principal contains the token
        String authToken = authentication.getCredentials().toString();

        try
        {
            Claims claims = jwtUtil.parseJwtToken(authToken);
            if(jwtUtil.isTokenNotExpired(claims))
            {
                String username = jwtUtil.getUsernameFromToken(claims);
                List<String> authorities = jwtUtil.getAuthoritiesFromToken(claims);
                log.info("authenticate. Token valid. username={} authorities={}", username, authorities);
                // can add validation on username in database, and also get authorities from there
                return Mono.just(new UsernamePasswordAuthenticationToken(username, null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(toList())));
            }
            else
            {
                log.info("authenticate. Token is already expired. token={}", authToken);
            }
        }
        catch(Exception e)
        {
            log.error("authenticate. Error in authenticating jwt. msg={}", e.getMessage());
        }

        return Mono.empty();
    }
}
