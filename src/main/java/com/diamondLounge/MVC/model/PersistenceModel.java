package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.diamondLounge.utility.Logger.logWarning;

@Repository
public class PersistenceModel<T> {
    @Autowired
    SessionFactory sessionFactory;

    void persistObject(T obj) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.saveOrUpdate(obj);
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    T getObjectById(String table, int id) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from " + table + " where id=:selectedId");
            query.setParameter("selectedId", id);
            return (T) query.uniqueResult();
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    List<T> getAllObjects(String table) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from " + table);
            return query.list();
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
