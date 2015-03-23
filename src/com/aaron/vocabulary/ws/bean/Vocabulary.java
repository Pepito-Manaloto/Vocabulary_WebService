package com.aaron.vocabulary.ws.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 */
public final class Vocabulary implements Serializable
{
    private static final long serialVersionUID = -4806944288017550814L;

    private int id;
    private String englishWord;
    private String foreignWord;

    public Vocabulary(final String englishWord, final String foreignWord)
    {
        this.englishWord = englishWord;
        this.foreignWord = foreignWord;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(final int id)
    {
        this.id = id;
    }

    public String getEnglishWord()
    {
        return this.englishWord;
    }

    public void setEnglishWord(final String englishWord)
    {
        this.englishWord = englishWord;
    }

    public String getForeignWord()
    {
        return this.foreignWord;
    }

    public void setForeignWord(final String foreignWord)
    {
        this.foreignWord = foreignWord;
    }

    /**
     * Checks all attribute for equality.
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
                   this.foreignWord.equals(that.getForeignWord());
        }
    }

    /**
     * Returns a unique hash code of the Vocabulary object.
     * @return int
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.englishWord);
        hash = 47 * hash + Objects.hashCode(this.foreignWord);
        return hash;
    }
    
    /**
     * Returns the content of the Vocabulary object in a formatted String.
     * @return String
     */
    @Override
    public String toString()
    {
        return "English: " + this.englishWord + " Foreign: " + this.foreignWord;
    }
}
