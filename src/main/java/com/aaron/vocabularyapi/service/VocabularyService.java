package com.aaron.vocabularyapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;

import reactor.core.publisher.Mono;

public interface VocabularyService
{
    DateTimeFormatter LAST_UPDATED_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    LocalDateTime DEFAULT_LAST_UPDATED = LocalDateTime.of(1950, 1, 1, 0, 0);

    long getRecentlyAddedCount(String lastUpdated);

    Mono<Map<Language, List<Vocabulary>>> getVocabulariesGroupedByForeignLanguage();
}
