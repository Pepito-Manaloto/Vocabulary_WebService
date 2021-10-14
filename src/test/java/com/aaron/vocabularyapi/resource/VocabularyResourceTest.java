package com.aaron.vocabularyapi.resource;

import static com.aaron.vocabularyapi.constant.Language.Hokkien;
import static com.aaron.vocabularyapi.constant.Language.Japanese;
import static com.aaron.vocabularyapi.constant.Language.Mandarin;
import static java.util.stream.LongStream.rangeClosed;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.aaron.vocabularyapi.config.VocabularyApiProperties;
import com.aaron.vocabularyapi.test.utils.AbstractContainerBaseTest;
import com.aaron.vocabularyapi.test.utils.EntityBuilder;
import com.aaron.vocabularyapi.test.utils.JwtAuthHelper;

@Testcontainers
@AutoConfigureWebTestClient
@AutoConfigureTestDatabase(replace = NONE) // Prevent replacing with h2
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@SpringBootTest
public class VocabularyResourceTest extends AbstractContainerBaseTest
{
    @Autowired
    private EntityBuilder entityBuilder;

    @Autowired
    private VocabularyApiProperties properties;

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private JwtAuthHelper jwtAuthHelper;

    @BeforeEach
    public void cleanup()
    {
        entityBuilder.deleteAllVocabulary();
    }

    @Test
    public void getVocabularies_unauthorized()
    {
        webClient.get()
                .uri("/vocabularies")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void getVocabularies_notFound()
    {
        webClient.get()
                .uri("/vocabularies")
                .header(AUTHORIZATION, jwtAuthHelper.getAuthHeader(properties, webClient))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Vocabularies not found param1-test={0} param2-test={1}.");
    }

    @Test
    public void getVocabularies_defaultLastUpdated()
    {
        int totalHokkien = 4;
        rangeClosed(1, totalHokkien).forEach(i -> entityBuilder.newVocabulary(Hokkien));
        int totalJapanese = 3;
        rangeClosed(1, totalJapanese).forEach(i -> entityBuilder.newVocabulary(Japanese));
        int totalMandarin = 2;
        rangeClosed(1, totalMandarin).forEach(i -> entityBuilder.newVocabulary(Mandarin));

        long expectedCount = totalHokkien + totalJapanese + totalMandarin;
        webClient.get()
                .uri("/vocabularies")
                .header(AUTHORIZATION, jwtAuthHelper.getAuthHeader(properties, webClient))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recently_added_count").isEqualTo(expectedCount)
                .jsonPath("$.languages").isMap()
                .jsonPath("$.languages.Hokkien").value(Matchers.hasSize(totalHokkien))
                .jsonPath("$.languages.Japanese").value(Matchers.hasSize(totalJapanese))
                .jsonPath("$.languages.Mandarin").value(Matchers.hasSize(totalMandarin));
    }

    @Test
    public void getVocabularies_withLastUpdated()
    {
        int year = 2021;
        int month = 9;
        int day = 20;
        LocalDateTime lastUpdated = LocalDateTime.of(year, month, day, 0, 0);
        int totalHokkienNotRecentlyAdded = 4;
        rangeClosed(1, totalHokkienNotRecentlyAdded).forEach(i -> entityBuilder.newVocabulary(Hokkien, lastUpdated.minusHours(5)));
        int totalJapaneseNotRecentlyAdded = 3;
        rangeClosed(1, totalJapaneseNotRecentlyAdded).forEach(i -> entityBuilder.newVocabulary(Japanese, lastUpdated.minusDays(3)));
        int totalMandarinNotRecentlyAdded = 2;
        rangeClosed(1, totalMandarinNotRecentlyAdded).forEach(i -> entityBuilder.newVocabulary(Mandarin, lastUpdated.minusMonths(2)));

        int totalHokkien = 4;
        rangeClosed(1, totalHokkien).forEach(i -> entityBuilder.newVocabulary(Hokkien, lastUpdated));
        int totalJapanese = 3;
        rangeClosed(1, totalJapanese).forEach(i -> entityBuilder.newVocabulary(Japanese, lastUpdated));
        int totalMandarin = 2;
        rangeClosed(1, totalMandarin).forEach(i -> entityBuilder.newVocabulary(Mandarin, lastUpdated));

        long recentlyAddedCount = totalHokkien + totalJapanese + totalMandarin;
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/vocabularies")
                        .queryParam("last_updated", String.format("%s-%02d-%s", year, month, day))
                        .build())
                .header(AUTHORIZATION, jwtAuthHelper.getAuthHeader(properties, webClient))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.recently_added_count").isEqualTo(recentlyAddedCount)
                .jsonPath("$.languages").isMap()
                .jsonPath("$.languages.Hokkien").value(Matchers.hasSize(totalHokkien + totalHokkienNotRecentlyAdded))
                .jsonPath("$.languages.Japanese").value(Matchers.hasSize(totalJapanese + totalJapaneseNotRecentlyAdded))
                .jsonPath("$.languages.Mandarin").value(Matchers.hasSize(totalMandarin + totalMandarinNotRecentlyAdded));
    }
}
