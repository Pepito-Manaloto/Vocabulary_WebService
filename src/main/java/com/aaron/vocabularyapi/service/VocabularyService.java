package com.aaron.vocabularyapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;

import reactor.core.publisher.Mono;

public interface VocabularyService
{
    DateTimeFormatter LAST_UPDATED_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .optionalStart()
            .appendPattern(" HH:mm")
            .optionalEnd()
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .toFormatter();
    LocalDateTime DEFAULT_LAST_UPDATED = LocalDateTime.of(1950, 1, 1, 0, 0);

    long getRecentlyAddedCount(String lastUpdated);

    Mono<Map<Language, List<Vocabulary>>> getVocabulariesGroupedByForeignLanguage();
}
