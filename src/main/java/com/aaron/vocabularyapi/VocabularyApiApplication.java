package com.aaron.vocabularyapi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.UrlResource;

@SpringBootApplication
public class VocabularyApiApplication
{
    public static void main(String[] args) throws IOException
    {
        if(args.length >= 1)
        {
            System.setProperty("jasypt.encryptor.password", args[0]);
            setUpTrustStoreForApplication();
            SpringApplication.run(VocabularyApiApplication.class, args);
        }
        else
        {
            System.err.println("Jasypt password required as java args[0]");
        }
    }

    // Needed to connect to spring cloud https
    private static void setUpTrustStoreForApplication() throws IOException
    {
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> applicationYamlPropertySource = loader.load(
                "config-application-properties", new UrlResource("file:config/application.yml"));
        Map<String, Object> source = ((MapPropertySource) applicationYamlPropertySource.get(0)).getSource();
        System.setProperty("javax.net.ssl.trustStore", source.get("spring.cloud.config.client.ssl.trust-store").toString());
        System.setProperty("javax.net.ssl.trustStorePassword", source.get("spring.cloud.config.client.ssl.trust-store-password").toString());
        System.setProperty("javax.net.ssl.trustStoreType", source.get("spring.cloud.config.client.ssl.trust-store-type").toString());
    }
}
