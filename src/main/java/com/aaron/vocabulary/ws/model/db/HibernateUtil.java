package com.aaron.vocabulary.ws.model.db;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate database connector class.
 */
public final class HibernateUtil
{
    private static final SessionFactory sessionFactory;
    private static final String HIBERNATE_CONFIG_PATH = "hibernate.cfg.xml";

    /**
     * Initializes the SessionFactory.
     */
    static
    {
        Configuration cfg = new Configuration();

        cfg.configure(HIBERNATE_CONFIG_PATH);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    /**
     * Private constructor, prevents initialization.
     */
    private HibernateUtil()
    {
    }

    /**
     * Converts the query list into a parametized list.
     * @param q Query
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Query q)
    {
        List<T> list = q.list();
        return list;
    }

    /**
     * Converts the criteria list into a parametized list.
     * @param q Criteria
     * @return List<T>
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Criteria c)
    {
        List<T> list = c.list();
        return list;
    }

    /**
     * Opens a session from the SessionFactory.
     */
    public static Session openSession()
    {
        return sessionFactory.openSession();
    }

    /**
     * Closes a session.
     * @param session
     */
    public static void closeSession(final Session session)
    {
        if(session != null && session.isOpen())
        {
            session.close();
        }
    }

    /**
     * Returns the SessionFactory.
     * @return session.close();
     */
    public static SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    /**
     * Closes the SessionFactory.
     */
    public static void closeSessionFactory()
    {
        sessionFactory.close();
    }
}
