package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Shop;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.model.ShopImpl;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.diamondLounge.utility.Logger.logWarning;

@Repository
public class ShopModel extends PersistenceModel {

    public List<ShopImpl> getAllShops() throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Shop");
            return convertFromDbObjects(query.list());
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private List<ShopImpl> convertFromDbObjects(List<Shop> employeeList) {
        return employeeList.stream().map(ShopImpl::new).collect(Collectors.toList());
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

    public void addShop(String name, String location, LocalTime openingTime, LocalTime closingTime, int requiredStaff) throws DiamondLoungeException {
        Shop shop = new Shop(name, location, openingTime, closingTime, requiredStaff);
        persistObject(shop);
    }

    public void editShop(int id, String name, String location, LocalTime openingTime, LocalTime closingTime, int requiredStaff) throws DiamondLoungeException {
        Shop shop = getShopById(id);
        shop.setName(name);
        shop.setLocation(location);
        shop.setOpeningTime(openingTime);
        shop.setClosingTime(closingTime);
        shop.setRequiredStaff(requiredStaff);

        persistObject(shop);
    }
}
