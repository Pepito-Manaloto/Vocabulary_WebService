package com.aaron.vocabulary.ws.model.db;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.aaron.vocabulary.ws.bean.ForeignLanguage;
import com.aaron.vocabulary.ws.bean.ForeignLanguage.Language;
import com.aaron.vocabulary.ws.bean.Vocabulary;

/**
 * Database utility class in accessing the Vocabularies.
 */
public class VocabularyDb
{
    private SessionFactory sessionFactory;
    public static final String LAST_UPDATED_QUERY = "SELECT COUNT(*) FROM Vocabulary WHERE last_updated > :last_updated AND foreign_id = :foreign_id";

    /**
     * Default constructor.
     */
    public VocabularyDb(final SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Gets the vocabularies of the given foreign language.
     * @param language the foreign language
     * @return Set<Vocabulary> set contains vocabularies, empty set if language is null
     */
    public Set<Vocabulary> getVocabularies(final Language language)
    {
        if(language == null)
        {
            return Collections.emptySet();
        }

        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(ForeignLanguage.class);
        criteria.add(Restrictions.eq("language", language)); // Variable name in ForeignLanguage class

        ForeignLanguage foreignLanguage = (ForeignLanguage) criteria.uniqueResult();
        Set<Vocabulary> result = new HashSet<>(foreignLanguage.getVocabularies()); // Loads data in new HashSet because the object can't be accessed once the session is closed

        session.close();

        return result;
    }

    /**
     * Gets the recently added vocabularies with last_updated greater than the given.
     * @param lastUpdated Date when the vocabulary was last updated
     * @return int total recently added vocabularies, 0 if lastUpdated is null
     */
    public int getRecentlyAddedCount(final Date lastUpdated)
    {
        if(lastUpdated == null)
        {
            return 0;
        }

        Session session = this.sessionFactory.openSession();
        Criteria criteria = session.createCriteria(Vocabulary.class);
        criteria.add(Restrictions.gt("lastUpdated", lastUpdated)); // Variable name in Vocabulary class
        criteria.setProjection(Projections.rowCount());

        int result = ((Long) criteria.uniqueResult()).intValue();

        session.close();

        return result;
    }

    /**
     * Gets the recently added vocabularies of the given language with last_updated greater than the given.
     * @param lastUpdated Date when the vocabulary was last updated
     * @param language selected language
     * @return int total recently added vocabularies of the given language, 0 if lastUpdated is null
     */
    public int getRecentlyAddedCount(final Date lastUpdated, final Language language)
    {
        if(lastUpdated == null || language == null)
        {
            return 0;
        }

        Session session = this.sessionFactory.openSession();

        Query query = session.createQuery(LAST_UPDATED_QUERY);
        query.setParameter("last_updated", lastUpdated);
        query.setParameter("foreign_id", language.ordinal() + 1);
        int result = ((Long) query.uniqueResult()).intValue();
        
        session.close();

        return result;
    }
}
