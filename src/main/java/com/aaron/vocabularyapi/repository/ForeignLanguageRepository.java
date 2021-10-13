package com.aaron.vocabularyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aaron.vocabularyapi.entity.ForeignLanguage;

public interface ForeignLanguageRepository extends JpaRepository<ForeignLanguage, Integer>
{

}
