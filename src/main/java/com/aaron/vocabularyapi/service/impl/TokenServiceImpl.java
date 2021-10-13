package com.aaron.vocabularyapi.service.impl;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.service.TokenService;
import com.aaron.vocabularyapi.util.JwtUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TokenServiceImpl implements TokenService
{
    private String passwordMD5;
    private JwtUtils jwtUtils;

    public TokenServiceImpl(VocabularyApiProperties properties, JwtUtils jwtUtils)
    {
        this.jwtUtils = jwtUtils;
        this.passwordMD5 = md5DigestAsHex(properties.getAuthenticationPassword().getBytes());

        log.info("init. Initialized. passwordMD5={}", passwordMD5);
    }

    @Override
    public Mono<String> authenticate(String password)
    {
        if(isNotBlank(password))
        {
            boolean isAuthorized = passwordMD5.equals(password);
            if(isAuthorized)
            {
                String username = password;
                // Ideally retrieve from database
                UserDetails user = new User(username, password, emptyList());
                String token = jwtUtils.createToken(user);
                log.info("authenticateLogin. Success. token={}", token);

                return Mono.just(token);
            }
            else
            {
                log.info("authenticateLogin. Failed password incorrect.");
                return Mono.empty();
            }
        }
        else
        {
            log.info("authenticateLogin. Failed password is blank.");
            return Mono.empty();
        }
    }
}
