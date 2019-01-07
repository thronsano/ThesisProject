package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Shop;

import java.time.LocalTime;

public class ShopImpl {

    private int id;
    private String name;
    private String location;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private int requiredStaff;

    public ShopImpl(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.location = shop.getLocation();
        this.openingTime = shop.getOpeningTime();
        this.closingTime = shop.getClosingTime();
        this.requiredStaff = shop.getRequiredStaff();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public int getRequiredStaff() {
        return requiredStaff;
    }
}
