package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Ware;
import com.diamondLounge.entity.db.WarePart;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.model.WarePartModel;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;

@Repository
public class WareService extends PersistenceService<Ware> {

    public List<Ware> getAllWares() {
        return getAllObjects("Ware");
    }

    public Ware getWareById(int selectedWare) {
        return getObjectById("Ware", selectedWare);
    }

    public void addWare(String name, double amount, double price, String description) {
        Ware ware = new Ware(name, amount, price, description, emptySet());
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

    public void deleteWare(int id) {
        deleteObjectById("Ware", id);
    }

    public void sellWare(int id, double amount) throws DiamondLoungeException {
        Ware ware = getObjectById("Ware", id);

        if (ware.getAmount() < amount) {
            throw new DiamondLoungeException("Amount to sell is too large!");
        }

        WarePart warePart = new WarePart(amount, ware.getPrice() * amount, now());

        ware.setAmount(ware.getAmount() - amount);
        ware.getSoldParts().add(warePart);

        persistObject(ware);
    }

    public List<WarePartModel> getAllWareParts() {
        return getAllWares().stream()
                            .filter(x -> x.getSoldParts().size() != 0)
                            .flatMap(ware -> ware.getSoldParts().stream()
                                                 .map(part -> new WarePartModel(part, ware)))
                            .collect(toList());

    }
}
