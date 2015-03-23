package com.aaron.vocabulary.ws.model.db;

import java.util.List;

import javax.servlet.ServletContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Database utility class.
 */
public final class HibernateUtil
{
    private static final SessionFactory sessionFactory;
    private static final String HIBERNATE_CONFIG_PATH = "/WEB-INF/hibernate/hibernate.cfg.xml";

    static
    {
        final Configuration cfg = new Configuration();
        cfg.configure(ServletContext.class.getResource(HIBERNATE_CONFIG_PATH));

        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(cfg.getProperties());

        sessionFactory = cfg.buildSessionFactory(builder.build());
    }

    private HibernateUtil()
    {
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Query q)
    {
        List<T> list = q.list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> listAndCast(final Criteria c)
    {
        List<T> list = c.list();
        return list;
    }

    public static SessionFactory getInstance()
    {
        return sessionFactory;
    }

    public static void close()
    {
        sessionFactory.close();
    }
}
