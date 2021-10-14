package com.aaron.vocabularyapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aaron.vocabularyapi.constant.Language;
import com.aaron.vocabularyapi.entity.ForeignLanguage;

public interface ForeignLanguageRepository extends JpaRepository<ForeignLanguage, Integer>
{
    Optional<ForeignLanguage> findOneByLanguage(Language language);
}
