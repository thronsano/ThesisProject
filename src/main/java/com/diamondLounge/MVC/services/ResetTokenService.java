package com.diamondLounge.MVC.services;


import com.diamondLounge.entity.db.ResetToken;
import com.diamondLounge.entity.exceptions.UsernamePasswordException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ResetTokenService extends PersistenceService<ResetToken> {

    @Autowired
    private SessionFactory sessionFactory;

    ResetToken getByResetToken(String resetToken) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from ResetToken rt where rt.resetToken=:resetToken");
            query.setParameter("resetToken", resetToken);
            return (ResetToken) query.uniqueResult();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    void addResetToken(ResetToken resetToken) throws UsernamePasswordException {
        deletePreviousResetToken(resetToken.getEmail());
        persistObject(resetToken);
    }

    void deletePreviousResetToken(String email) throws UsernamePasswordException {
        deleteObjectById("ResetToken", email);
    }
}
