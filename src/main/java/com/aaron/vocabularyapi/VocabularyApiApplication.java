package com.aaron.vocabularyapi;

import static java.util.Objects.nonNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VocabularyApiApplication
{
    private static final String JASYPT_PASSWORD_PROPERTY = "jasypt.encryptor.password";
    // private static final String PROFILES_ACTIVE_PROPERTY = "spring.profiles.active"; NOSONAR

    public static void main(String[] args)
    {
        if(nonNull(System.getProperty(JASYPT_PASSWORD_PROPERTY)))
        {
            // setUpTrustStoreForApplication(); NOSONAR
            SpringApplication.run(VocabularyApiApplication.class, args);
        }
        else
        {
            System.err.println(JASYPT_PASSWORD_PROPERTY + " must be set"); //NOSONAR
        }
    }

    // Needed to connect to spring cloud https
    //    private static void setUpTrustStoreForApplication() throws IOException
    //    {
    //        String profile = getActiveProfile();
    //        String configFile = String.format("file:config/application-%s.yml", profile);
    //
    //        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
    //        List<PropertySource<?>> applicationYamlPropertySource = loader.load(
    //                "config-application-properties", new UrlResource(configFile));
    //        Map<String, Object> source = ((MapPropertySource) applicationYamlPropertySource.get(0)).getSource();
    //        System.setProperty("javax.net.ssl.trustStore", source.get("spring.cloud.config.client.ssl.trust-store").toString());
    //        System.setProperty("javax.net.ssl.trustStorePassword", source.get("spring.cloud.config.client.ssl.trust-store-password").toString());
    //    }
    //
    //    private static String getActiveProfile()
    //    {
    //        return System.getProperty(PROFILES_ACTIVE_PROPERTY, "dev");
    //    }
}
