package com.aaron.vocabularyapi.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Configuration
public class MessageResourceBundleConfig
{
    private VocabularyApiProperties properties;

    @Bean
    @RefreshScope
    public ReloadableResourceBundleMessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(properties.getMessageResourceBaseName());
        messageSource.setDefaultEncoding(UTF_8.name());
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator()
    {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}