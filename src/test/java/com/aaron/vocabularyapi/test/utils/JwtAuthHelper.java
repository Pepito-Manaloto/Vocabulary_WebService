package com.aaron.vocabularyapi.test.utils;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.util.DigestUtils.md5DigestAsHex;
import static org.apache.commons.lang3.StringUtils.*;

import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.request.RequestToken;
import com.aaron.vocabularyapi.response.ResponseToken;

@Service
public class JwtAuthHelper
{
    private String jwtAuthHeader;

    public String getAuthHeader(VocabularyApiProperties properties, WebTestClient webClient)
    {
        // No expiration checks needed as tests are expected to finish before 60mins expiry time
        if(isBlank(jwtAuthHeader))
        {
            String password = md5DigestAsHex(properties.getAuthenticationPassword().getBytes());
            RequestToken request = new RequestToken();
            request.setPassword(password);

            ResponseToken token = webClient.post()
                    .uri("/token")
                    .contentType(APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .exchange()
                    .returnResult(ResponseToken.class)
                    .getResponseBody()
                    .blockFirst();

            jwtAuthHeader = "Bearer " + token.getToken();
        }

        return jwtAuthHeader;
    }
}
