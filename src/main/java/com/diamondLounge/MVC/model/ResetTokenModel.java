package com.diamondLounge.MVC.model;


import com.diamondLounge.entity.ResetToken;
import com.diamondLounge.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResetTokenModel {

    @Autowired
    private SessionFactory sessionFactory;

    public ResetToken getByResetToken(String resetToken) {
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

    ResetToken getResetTokenByEmail(String email) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            ResetToken resetToken = sessionFactory.openSession().get(ResetToken.class, email);
            return resetToken;
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public boolean addResetToken(ResetToken resetToken) {
        deleteResetToken(resetToken.getEmail());
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(resetToken);
        } catch (Exception e) {
            Logger.logError("Exception during adding new resetToken into database");
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }

        return true;
    }

    public boolean deleteResetToken(String email) {
        ResetToken existingToken = getResetTokenByEmail(email);

        if (existingToken != null) {
            Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                session.delete(existingToken);
            } catch (Exception e) {
                Logger.logError("Exception during removing existing resetToken");
                return false;
            } finally {
                session.getTransaction().commit();
                session.close();
            }
        } else
            Logger.log("Reset token doesn't exist");

        return true;
    }
}
