package com.aaron.vocabulary.ws.model.db;

import com.aaron.vocabulary.ws.bean.ForeignLanguage;
import com.aaron.vocabulary.ws.bean.Vocabulary;
import com.aaron.vocabulary.ws.bean.ForeignLanguage.Language;

import java.util.Date;
import java.util.HashSet;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static com.aaron.vocabulary.ws.model.db.VocabularyDb.LAST_UPDATED_QUERY;

/**
 * Unit tests for VocabularyDbTest class.
 */
public class VocabularyDbTest
{
    private VocabularyDb vocabDb;

    // Mock objects.
    private SessionFactory sessionFactory;
    private Session session;
    private Criteria criteria;
    private Query query;

    // Expected results objects
    private static final long RECENTLY_ADDED_COUNT = 5;
    private static final ForeignLanguage expectedForeignLanguage = new ForeignLanguage(Language.Hokkien);
    private static final Criterion expectedLanguageCriterion = Restrictions.eq("language", Language.Hokkien);

    @BeforeClass
    public static void initializeOnce()
    {
        HashSet<Vocabulary> set = new HashSet<>(2);
        set.add(new Vocabulary("english", "foreign", expectedForeignLanguage));
        set.add(new Vocabulary("qwerty", "zxc", expectedForeignLanguage));

        expectedForeignLanguage.setVocabularies(set);
    }
    
    @Before
    public void initialization()
    {
        this.sessionFactory = mock(SessionFactory.class);
        this.session = mock(Session.class);
        this.criteria = mock(Criteria.class);
        this.query = mock(Query.class);
        this.vocabDb = new VocabularyDb(this.sessionFactory);
    }

    @Test
    public void testGetVocabulariesValid()
    {
        when(this.sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.createCriteria(ForeignLanguage.class)).thenReturn(this.criteria);
        when(this.criteria.uniqueResult()).thenReturn(expectedForeignLanguage);

        assertEquals(expectedForeignLanguage.getVocabularies(), this.vocabDb.getVocabularies(Language.Hokkien)); 

        ArgumentCaptor<Criterion> captor = ArgumentCaptor.forClass(Criterion.class);
        // ensure a call to criteria.add and record the argument the method call had
        verify(this.criteria).add(captor.capture());

        assertEquals(expectedLanguageCriterion.toString(), captor.getValue().toString());
    }

    @Test
    public void testGetVocabulariesInvalid()
    {
        assertEquals(new HashSet<>(0), this.vocabDb.getVocabularies(null));
    }

    @Test
    public void testGetRecentlyAddedCountValid()
    {
        when(this.sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.createCriteria(Vocabulary.class)).thenReturn(this.criteria);
        when(this.criteria.uniqueResult()).thenReturn(RECENTLY_ADDED_COUNT);

        assertEquals(RECENTLY_ADDED_COUNT, this.vocabDb.getRecentlyAddedCount(new Date()));

        when(this.sessionFactory.openSession()).thenReturn(this.session);
        when(this.session.createQuery(LAST_UPDATED_QUERY)).thenReturn(this.query);
        when(this.query.uniqueResult()).thenReturn(RECENTLY_ADDED_COUNT);

        assertEquals(RECENTLY_ADDED_COUNT, this.vocabDb.getRecentlyAddedCount(new Date(), Language.Mandarin));
        
    }

    @Test
    public void testGetRecentlyAddedCountInvalid()
    {
        assertEquals(0, this.vocabDb.getRecentlyAddedCount(null));
        assertEquals(0, this.vocabDb.getRecentlyAddedCount(null, null));
    }
}
