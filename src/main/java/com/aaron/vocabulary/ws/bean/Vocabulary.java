package com.aaron.vocabulary.ws.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Bean class and Hibernate annotation mapping for Vocabulary.
 */
@Entity
@Table(name = "vocabulary",
       uniqueConstraints = @UniqueConstraint(columnNames = {"english_word", "foreign_word"}))
public final class Vocabulary implements Serializable
{
    /** serialVersionUID */
    private static final long serialVersionUID = -6714085037306403710L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    @Column(name = "english_word", length = 70, nullable = false)
    private String englishWord;
    
    @Column(name = "foreign_word", length = 100, nullable = false)
    private String foreignWord;

    @Column(name = "datein", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateIn;

    @Column(name = "last_updated", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToOne
    @JoinColumn(name = "foreign_id", nullable = false, referencedColumnName = "id")
    private ForeignLanguage foreignLanguage;

    /**
     * Default constructor for hibernate.
     */
    public Vocabulary()
    {
        this.dateIn = new Date();
        this.lastUpdated = new Date();
    }

    /**
     * Constructor that initializes english word, foreign word, and foreign language. 
     */
    public Vocabulary(final String englishWord, final String foreignWord, final ForeignLanguage foreignLanguage)
    {
        super();
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
        this.foreignLanguage = foreignLanguage;
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
     * Gets the english word.
     * @return String
     */
    public String getEnglishWord()
    {
        return this.englishWord;
    }

    /**
     * Sets the english word.
     * @param englishWord
     */
    public void setEnglishWord(final String englishWord)
    {
        this.englishWord = englishWord;
    }

    /**
     * Gets the foreign word.
     * @return String
     */
    public String getForeignWord()
    {
        return this.foreignWord;
    }

    /**
     * Sets the foreign word.
     * @param englishWord
     */
    public void setForeignWord(final String foreignWord)
    {
        this.foreignWord = foreignWord;
    }

    /**
     * Gets the date in.
     * @return Date
     */
    public Date getDateIn() 
    {
        return this.dateIn;
    }

    /**
     * Sets the date in.
     * @param dateIn
     */
    public void setDateIn(final Date dateIn) 
    {
        this.dateIn = (Date) dateIn.clone();
    }

    /**
     * Gets the last updated.
     * @return Date
     */
    public Date getLastUpdated()
    {
        return this.lastUpdated;
    }

    /**
     * Sets the last updated.
     * @param lastUpdated
     */
    public void setLastUpdated(final Date lastUpdated) 
    {
        this.lastUpdated = (Date) lastUpdated.clone();
    }

    /**
     * Sets the foreign language.
     * @param foreignLanguage
     */
    public void setForeignLanguage(final ForeignLanguage foreignLanguage) 
    {
        this.foreignLanguage = foreignLanguage;
    }

    /**
     * Gets the foreign language.
     * @return ForeignLanguage
     */
    public ForeignLanguage getForeignLanguage()
    {
        return this.foreignLanguage;
    }

    /**
     * Checks if english word, foreign word, and foreign language of this object is equal to the given object.
     * @param o Vocabulary to compare
     * @return true if equals, else false
     */
    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Vocabulary)) // object being compared is not Vocabulary
        {
            return false;
        }
        else if(o == this) // the reference of the objects being compared is equal to each other
        {
            return true;
        }
        else
        {
            Vocabulary that = (Vocabulary) o;
            
            return this.englishWord.equals(that.getEnglishWord()) && 
                   this.foreignWord.equals(that.getForeignWord()) &&
                   this.foreignLanguage.equals(that.getForeignLanguage());
        }
    }

    /**
     * Returns a unique hash code of the Vocabulary object. Derived from english word, foreign word, and foreign language.
     * @return int
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.englishWord);
        hash = 47 * hash + Objects.hashCode(this.foreignWord);
        hash = 47 * hash + this.foreignLanguage.hashCode();

        return hash;
    }
    
    /**
     * Returns the content of the Vocabulary object in a formatted String.
     * @return String
     */
    @Override
    public String toString()
    {
        return "English: " + this.englishWord + " " + this.foreignLanguage.getLanguage() + ": " + this.foreignWord;
    }
}
