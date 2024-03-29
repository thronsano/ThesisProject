package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Shop;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.models.dbWrappers.ShopModel;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ShopService extends PersistenceService<Shop> {

    public List<ShopModel> getAllShops() throws DiamondLoungeException {
        return convertFromDbObjects(getAllObjects("Shop"));
    }

    private List<ShopModel> convertFromDbObjects(List<Shop> employeeList) {
        return employeeList.stream().map(ShopModel::new).collect(Collectors.toList());
    }

    public Shop getShopById(int selectedShop) throws DiamondLoungeException {
        return getObjectById("Shop", selectedShop);
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

    public void deleteShop(int id) {
        deleteObjectById("Shop", id);
    }
}
