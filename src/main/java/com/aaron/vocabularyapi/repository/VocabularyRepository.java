package com.aaron.vocabularyapi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aaron.vocabularyapi.entity.ForeignLanguage;
import com.aaron.vocabularyapi.entity.Vocabulary;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long>
{
    long countByAuditTimeLastUpdatedGreaterThan(LocalDateTime lastUpdated);

    List<Vocabulary> findAllByOrderByEnglishWord();

    List<Vocabulary> findAllByForeignLanguageOrderByEnglishWordAsc(ForeignLanguage foreignLanguage);

    List<Vocabulary> findAllByForeignLanguageOrderByAuditTimeLastUpdatedDesc(ForeignLanguage foreignLanguage);

    List<Vocabulary> findAllByForeignLanguageAndEnglishWordStartingWith(ForeignLanguage foreignLanguage, String englishWord);
}
