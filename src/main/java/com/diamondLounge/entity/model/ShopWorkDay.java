package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Shop;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "shop_work_days")
public class ShopWorkDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDate date;

    @OneToOne
    private Shop shop;

    @Column
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Employee> employees;

    public ShopWorkDay() {
    }

    public ShopWorkDay(LocalDate date, Shop shop, Set<Employee> employees) {
        this.date = date;
        this.shop = shop;
        this.employees = employees;
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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
