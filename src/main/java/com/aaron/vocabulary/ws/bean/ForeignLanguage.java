package com.aaron.vocabulary.ws.bean;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * Bean class and Hibernate annotation mapping for ForeignLanguage.
 */
@Entity
@Table(name = "foreign_language")
public class ForeignLanguage implements Serializable
{
    /** serialVersionUID */
    private static final long serialVersionUID = -4790166699128361596L;

    /**
     * Enum of available foreign languages.
     */
    public enum Language
    {
        Hokkien,
        Japanese,
        Mandarin,
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @OneToMany(mappedBy = "foreignLanguage")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE})
    private Set<Vocabulary> vocabularies;

    /**
     * Default empty constructor for hibernate.
     */
    public ForeignLanguage()
    {
    }

    /**
     * Constructor that initializes language.
     * @param language foreign language
     */
    public ForeignLanguage(final Language language)
    {
        this.language = language;
    }

    /**
     * Gets the id.
     * @return int
     */
    public int getId()
    {
        return this.id;
    }

    /**
     * Sets the id.
     * @param id
     */
    public void setId(final int id)
    {
        this.id = id;
    }

    /**
     * Gets the language.
     * @return language
     */
    public Language getLanguage()
    {
        return this.language;
    }

    /**
     * Sets the language.
     * @param language
     */
    public void setLanguage(final Language language)
    {
        this.language = language;
    }

    /**
     * Gets the vocabularies of this foreign language.
     * @return Set<Vocabulary>
     */
    public Set<Vocabulary> getVocabularies()
    {
        return this.vocabularies;
    }

    /**
     * Sets the vocabularies of this foreign language.
     * @param vocabularies
     */
    public void setVocabularies(final Set<Vocabulary> vocabularies)
    {
        this.vocabularies = vocabularies;
    }

    /**
     * Checks if the language of this object and the given object is equal.
     * @param o ForeignLanguage to compare
     * @return boolean true if equal, else false
     */
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof ForeignLanguage))
        {
            return false;
        }
        else if(this == o)
        {
            return true;
        }
        else
        {
            ForeignLanguage that = (ForeignLanguage) o;
            
            return this.language.equals(that.getLanguage());
        }
    }

    /**
     * Returns the hashcode of the object. Derived from language.
     * @return int
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.language);

        return hash;
    }

    /**
     * Returns a string representation of ForeignLanguage.
     * @return String
     */
    @Override
    public String toString()
    {
        return "Foreignlanguage: " + this.language;
    }
}