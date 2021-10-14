package com.aaron.vocabularyapi.service.impl;

import static com.aaron.vocabularyapi.constant.Language.Hokkien;
import static com.aaron.vocabularyapi.constant.Language.Japanese;
import static com.aaron.vocabularyapi.constant.Language.Mandarin;
import static java.util.stream.LongStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;
import com.aaron.vocabularyapi.service.VocabularyService;
import com.aaron.vocabularyapi.test.utils.AbstractContainerBaseTest;
import com.aaron.vocabularyapi.test.utils.EntityBuilder;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Testcontainers
@Import({ EntityBuilder.class, VocabularyServiceImpl.class })
@DataJpaTest//(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { EntityBuilder.class, VocabularyServiceImpl.class }))
@AutoConfigureTestDatabase(replace = NONE) // Prevent replacing with h2
public class VocabularyServiceImplTest extends AbstractContainerBaseTest
{
    @Autowired
    private EntityBuilder entityBuilder;

    @Autowired
    private VocabularyService vocabularyService;

    @BeforeEach
    public void cleanup()
    {
        entityBuilder.deleteAllVocabulary();
    }

    @Test
    public void getRecentlyAddedCount_nullLastUpdateDefault()
    {
        long totalVocabulary = 8;
        rangeClosed(1, totalVocabulary).forEach(i -> entityBuilder.newVocabulary());

        long count = vocabularyService.getRecentlyAddedCount(null);

        assertEquals(totalVocabulary, count);
    }

    @Test
    public void getRecentlyAddedCount_blankLastUpdateDefault()
    {
        long totalVocabulary = 8;
        rangeClosed(1, totalVocabulary).forEach(i -> entityBuilder.newVocabulary());

        long count = vocabularyService.getRecentlyAddedCount("");

        assertEquals(totalVocabulary, count);
    }

    @Test
    public void getRecentlyAddedCount_withLastUpdate()
    {
        int year = 2021;
        int month = 9;
        int day = 20;
        LocalDateTime lastUpdated = LocalDateTime.of(year, month, day, 0, 0);

        // Exclusive
        entityBuilder.newVocabulary(lastUpdated.minusMonths(1));
        entityBuilder.newVocabulary(lastUpdated.minusMonths(2));
        entityBuilder.newVocabulary(lastUpdated.minusDays(1));
        entityBuilder.newVocabulary(lastUpdated.minusDays(2));
        // Inclusive
        entityBuilder.newVocabulary(lastUpdated.plusMonths(3));
        entityBuilder.newVocabulary(lastUpdated.plusHours(1));
        entityBuilder.newVocabulary(lastUpdated);
        entityBuilder.newVocabulary(lastUpdated.minusMinutes(29));

        long count = vocabularyService.getRecentlyAddedCount(String.format("%s-%02d-%s", year, month, day));

        assertEquals(4, count);
    }

    @Test
    public void getVocabulariesGroupedByForeignLanguage_empty()
    {
        Mono<Map<Language, List<Vocabulary>>> mono = vocabularyService.getVocabulariesGroupedByForeignLanguage();

        StepVerifier.create(mono)
                .expectNextMatches(Map::isEmpty)
                .expectComplete();
    }

    @Test
    public void getVocabulariesGroupedByForeignLanguage_withResult()
    {
        long totalHokkien = 9;
        rangeClosed(1, totalHokkien).forEach(i -> entityBuilder.newVocabulary(Hokkien));
        long totalJapanese = 8;
        rangeClosed(1, totalJapanese).forEach(i -> entityBuilder.newVocabulary(Japanese));
        long totalMandarin = 7;
        rangeClosed(1, totalMandarin).forEach(i -> entityBuilder.newVocabulary(Mandarin));

        Mono<Map<Language, List<Vocabulary>>> mono = vocabularyService.getVocabulariesGroupedByForeignLanguage();

        Predicate<Map<Language, List<Vocabulary>>> expectLanguageSizeCorrect = m -> m.size() == Language.values().length &&
                m.get(Hokkien).size() == totalHokkien && m.get(Japanese).size() == totalJapanese &&
                m.get(Mandarin).size() == totalMandarin;

        StepVerifier.create(mono)
                .expectNextMatches(expectLanguageSizeCorrect)
                .verifyComplete();
    }
}
