package com.aaron.vocabularyapi.service.impl;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aaron.vocabularyapi.annotation.TransactionalRead;
import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.Vocabulary;
import com.aaron.vocabularyapi.repository.VocabularyRepository;
import com.aaron.vocabularyapi.service.VocabularyService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class VocabularyServiceImpl implements VocabularyService
{
    private VocabularyRepository vocabularyRepository;

    @TransactionalRead
    @Override
    public long getRecentlyAddedCount(String lastUpdatedStr)
    {
        LocalDateTime lastUpdated = parseLastUpdated(lastUpdatedStr);

        log.info("getRecentlyAddedCount. Start. lastUpdated={} - 30mins", lastUpdated);

        return vocabularyRepository.countByAuditTimeLastUpdatedGreaterThan(lastUpdated.minusMinutes(30));
    }

    private LocalDateTime parseLastUpdated(String lastUpdatedStr)
    {
        LocalDateTime lastUpdated;
        if(isNotBlank(lastUpdatedStr))
        {
            lastUpdated = LocalDateTime.parse(lastUpdatedStr, LAST_UPDATED_FORMATTER);
        }
        else
        {
            lastUpdated = DEFAULT_LAST_UPDATED;
        }

        return lastUpdated;
    }

    @TransactionalRead
    @Override
    public Mono<Map<Language, List<Vocabulary>>> getVocabulariesGroupedByForeignLanguage()
    {
        Map<Language, List<Vocabulary>> vocabularies = vocabularyRepository.findAllByOrderByEnglishWord().stream()
                .collect(groupingBy(v -> v.getForeignLanguage().getLanguage()));

        log.info("getVocabulariesGroupedByForeignLanguage. Retrieved from database. vocabularies={}",
                vocabularies.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue().size()).collect(joining(", ")));

        return Mono.just(vocabularies);
    }
}
