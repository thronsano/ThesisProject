package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Ware;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WareModel extends PersistenceModel<Ware> {
    public List<Ware> getAllWares() {
        return getAllObjects("Ware");
    }

    public Ware getWareById(int selectedWare) {
        return getObjectById("Ware", selectedWare);
    }

    public void addWare(String name, double amount, double price, String description) {
        Ware ware = new Ware(name, amount, price, description);
        persistObject(ware);
    }

    public void editWare(int id, String name, double amount, double price, String description) {
        Ware ware = getWareById(id);

        ware.setName(name);
        ware.setAmount(amount);
        ware.setPrice(price);
        ware.setDescription(description);

        persistObject(ware);
    }
}
