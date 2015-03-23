package com.aaron.vocabulary.ws.model.db;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.aaron.vocabulary.ws.bean.ForeignLanguage;
import com.aaron.vocabulary.ws.bean.Vocabulary;
import com.aaron.vocabulary.ws.model.others.ForeignLanguageEnum;

import static com.aaron.vocabulary.ws.model.db.VocabularyColumn.*;
import static com.aaron.vocabulary.ws.model.db.ForeignLanguageColumn.*;

public class VocabularyDb
{
    public VocabularyDb()
    {}
    
    public List<Vocabulary> getVocabularies(final ForeignLanguageEnum foreignLanguage)
    {
        Session session = HibernateUtil.getInstance().openSession();
        Criteria criteria = session.createCriteria(ForeignLanguage.class);
        criteria.add(Restrictions.eq(language.name(), foreignLanguage.name()));

        return HibernateUtil.<Vocabulary>listAndCast(criteria);
    }
    
    public int getRecentlyAddedCount(final String lastUpdated)
    {
        Session session = HibernateUtil.getInstance().openSession();
        Criteria criteria = session.createCriteria(Vocabulary.class);
        criteria.add(Restrictions.gt(last_updated.name(), lastUpdated));
        criteria.setProjection(Projections.rowCount());

        int result = ((Integer) criteria.uniqueResult()).intValue();

        return result;
    }
}
