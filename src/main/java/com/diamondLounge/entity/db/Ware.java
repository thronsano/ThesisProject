package com.diamondLounge.entity.db;

import javax.persistence.*;

@Entity
@Table(name = "wares")
public class Ware {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private double amount;

    @Column
    private double price;

    @Column
    private String description;

    public Ware(String name, double amount, double price, String description) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.description = description;
    }

    public Ware() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
