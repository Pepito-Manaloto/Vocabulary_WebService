package com.aaron.vocabulary.ws.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * 
 */
public class ForeignLanguage implements Serializable
{
    private static final long serialVersionUID = -8373995646395398966L;

    private int id;
    private String language;
    private Set<Vocabulary> vocabularies;
    
    private ForeignLanguage(final String language)
    {
        this.language = language;
    }
    
    public int getId()
    {
        return this.id;
    }

    public void setId(final int id)
    {
        this.id = id;
    }

    public String getLanguage()
    {
        return this.language;
    }

    public void setLanguage(final String language)
    {
        this.language = language;
    }

    public Set<Vocabulary> getVocabularies()
    {
        return this.vocabularies;
    }

    public void setVocabularies(final Set<Vocabulary> vocabularies)
    {
        this.vocabularies = vocabularies;
    }
}