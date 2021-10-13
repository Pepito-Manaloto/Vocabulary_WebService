package com.aaron.vocabularyapi.config;

import static java.util.Arrays.asList;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@AllArgsConstructor
@Configuration
@EnableSwagger2
public class SwaggerConfiguration
{
    private static final String JWT = "JWT";

    private VocabularyApiProperties properties;

    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiEndpointsInfo())
                .enable(true)
                .securityContexts(asList(securityContext()))
                .securitySchemes(asList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiEndpointsInfo()
    {
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title(properties.getAppName());

        return builder.build();
    }

    private ApiKey apiKey()
    {
        return new ApiKey(JWT, HttpHeaders.AUTHORIZATION, "header");
    }

    private SecurityContext securityContext()
    {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScope };
        return asList(new SecurityReference(JWT, authorizationScopes));
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunction()
    {
        return route(GET("/"), req -> ServerResponse.temporaryRedirect(URI.create(properties.getBasePath() + "/swagger-ui/"))
                .build());
    }
}