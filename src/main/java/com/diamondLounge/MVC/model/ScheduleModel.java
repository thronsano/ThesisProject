package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Shop;
import com.diamondLounge.entity.model.ShopWorkDay;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScheduleModel {

    @Autowired
    private SessionFactory sessionFactory;

    public List<ShopWorkDay> getShopWorkDays() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from ShopWorkDay");
            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void generateSchedule() {

    }

    public List<Shop> getShopList() {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Shop");
            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
