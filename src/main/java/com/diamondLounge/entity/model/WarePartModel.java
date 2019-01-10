package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Ware;
import com.diamondLounge.entity.db.WarePart;

import java.time.LocalDateTime;

public class WarePartModel {
    private int wareId;
    private int warePartId;
    private String name;
    private double amount;
    private double price;
    private String description;
    private String employeeName;
    private LocalDateTime dateSold;

    public WarePartModel(WarePart warePart, Ware ware) {
        this.wareId = ware.getId();
        this.name = ware.getName();
        this.description = ware.getDescription();

        this.warePartId = warePart.getId();
        this.amount = warePart.getAmount().doubleValue();
        this.price = warePart.getPrice().doubleValue();
        this.dateSold = warePart.getDateSold();
        this.employeeName = warePart.getEmployee().getName();
    }

    public int getWareId() {
        return wareId;
    }

    public int getWarePartId() {
        return warePartId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public String getEmployeeName() {
        return employeeName;
    }
}
