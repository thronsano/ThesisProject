package com.diamondLounge.MVC.services;


import com.diamondLounge.entity.db.ResetToken;
import com.diamondLounge.entity.exceptions.UsernamePasswordException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static com.diamondLounge.utility.Logger.log;
import static com.diamondLounge.utility.Logger.logError;

@Repository
public class ResetTokenService {

    @Autowired
    private SessionFactory sessionFactory;

    ResetToken getByResetToken(String resetToken) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from ResetToken rt where rt.resetToken =:resetToken");
            query.setParameter("resetToken", resetToken);
            return (ResetToken) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private ResetToken getResetTokenByEmail(String email) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            return sessionFactory.openSession().get(ResetToken.class, email);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    void addResetToken(ResetToken resetToken) throws UsernamePasswordException {
        deleteResetToken(resetToken.getEmail());
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(resetToken);
        } catch (Exception e) {
            logError("Exception during adding new resetToken into database");
            throw new UsernamePasswordException(e.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    void deleteResetToken(String email) throws UsernamePasswordException {
        ResetToken existingToken = getResetTokenByEmail(email);

        if (existingToken != null) {
            Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                session.delete(existingToken);
            } catch (Exception e) {
                logError("Exception during removing existing resetToken");
                throw new UsernamePasswordException(e.getMessage());
            } finally {
                session.getTransaction().commit();
                session.close();
            }
        } else
            log("Reset token doesn't exist");
    }
}
