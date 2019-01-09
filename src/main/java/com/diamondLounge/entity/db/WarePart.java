package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sold_wares")
public class WarePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private double amount;

    @Column
    private double price;

    @Column
    private LocalDateTime dateSold;

    public WarePart() {
    }

    public WarePart(double amount, double price, LocalDateTime dateSold) {
        this.amount = amount;
        this.price = price;
        this.dateSold = dateSold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }
}
