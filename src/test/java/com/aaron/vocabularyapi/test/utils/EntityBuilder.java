package com.aaron.vocabularyapi.test.utils;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.aaron.vocabularyapi.annotation.TransactionalWrite;
import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.AuditTime;
import com.aaron.vocabularyapi.entity.ForeignLanguage;
import com.aaron.vocabularyapi.entity.Vocabulary;
import com.aaron.vocabularyapi.repository.ForeignLanguageRepository;
import com.aaron.vocabularyapi.repository.VocabularyRepository;

import lombok.AllArgsConstructor;

@TransactionalWrite
@AllArgsConstructor
@Service
public class EntityBuilder
{
    private ForeignLanguageRepository fRepository;
    private VocabularyRepository vRepository;

    public void deleteAllVocabulary()
    {
        vRepository.deleteAll();
    }

    public Vocabulary newVocabulary()
    {
        return this.newVocabulary(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                Language.values()[RandomUtils.nextInt(0, 3)], LocalDateTime.now());
    }

    public Vocabulary newVocabulary(Language language)
    {
        return this.newVocabulary(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                language, LocalDateTime.now());
    }

    public Vocabulary newVocabulary(LocalDateTime lastUpdated)
    {
        return this.newVocabulary(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                Language.values()[RandomUtils.nextInt(0, 3)], lastUpdated);
    }

    public Vocabulary newVocabulary(Language language, LocalDateTime lastUpdated)
    {
        return this.newVocabulary(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                language, lastUpdated);
    }

    public Vocabulary newVocabulary(String english, String foreign, Language language, LocalDateTime lastUpdated)
    {
        ForeignLanguage foreignLanguage = fRepository.findOneByLanguage(language)
                .orElseThrow();

        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setEnglishWord(english);
        vocabulary.setForeignWord(foreign);
        vocabulary.setForeignLanguage(foreignLanguage);

        AuditTime auditTime = new AuditTime();
        auditTime.setDateIn(LocalDateTime.now());
        auditTime.setLastUpdated(lastUpdated);
        vocabulary.setAuditTime(auditTime);

        return vRepository.save(vocabulary);
    }
}
