package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Shop;
import com.diamondLounge.exceptions.DiamondLoungeException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

import static com.diamondLounge.utility.Logger.logWarning;

@Repository
public class ShopModel extends PersistenceModel {

    public List<Shop> getAllShops() {
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

    public Shop getShopById(int selectedShop) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Shop where id=:selectedId");
            query.setParameter("selectedId", selectedShop);
            return (Shop) query.uniqueResult();
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void addShop(String name, String localization, LocalTime openingTime, LocalTime closingTime, int requiredStaff) throws DiamondLoungeException {
        Shop shop = new Shop(name, localization, openingTime, closingTime, requiredStaff);
        persistObject(shop);
    }

    public void editShop(int id, String name, String localization, LocalTime openingTime, LocalTime closingTime, int requiredStaff) throws DiamondLoungeException {
        Shop shop = getShopById(id);
        shop.setName(name);
        shop.setLocalization(localization);
        shop.setOpeningTime(openingTime);
        shop.setClosingTime(closingTime);
        shop.setRequiredStaff(requiredStaff);

        persistObject(shop);
    }
}
