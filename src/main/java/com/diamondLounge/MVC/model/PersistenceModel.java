package com.diamondLounge.MVC.model;

import com.diamondLounge.exceptions.DiamondLoungeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.diamondLounge.utility.Logger.logWarning;

@Repository
public class PersistenceModel {
    @Autowired
    SessionFactory sessionFactory;

    void persistObject(Object obj) throws DiamondLoungeException {
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
}
