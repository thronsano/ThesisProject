package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "shops")
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String localization;

    @Column
    private LocalTime openingTime;

    @Column
    private LocalTime closingTime;

    @Column
    private int requiredStaff;

    public Shop() {
    }

    public Shop(String name, String localization, LocalTime openingTime, LocalTime closingTime, int requiredStaff) {
        this.name = name;
        this.localization = localization;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.requiredStaff = requiredStaff;
    }

    public int getRequiredStaff() {
        return requiredStaff;
    }

    public void setRequiredStaff(int requiredStaff) {
        this.requiredStaff = requiredStaff;
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

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
