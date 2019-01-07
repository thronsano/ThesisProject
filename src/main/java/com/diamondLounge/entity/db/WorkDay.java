package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;

@Entity
@Table(name = "work_days")
public class WorkDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDate date;

    @Column
    private Duration hoursWorked;

    @OneToOne
    private Shop shop;

    public WorkDay() {
    }

    public WorkDay(LocalDate date, Shop shop, Duration hoursWorked) {
        this.date = date;
        this.shop = shop;
        this.hoursWorked = hoursWorked;
    }

    public Duration getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(Duration hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
