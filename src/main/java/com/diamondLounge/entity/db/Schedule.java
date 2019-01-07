package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private LocalDate date;

    @OneToOne
    private Shop shop;

    @Column
    @ManyToMany(fetch = EAGER, cascade = ALL)
    private Set<Employee> employees;

    public Schedule() {
    }

    public Schedule(LocalDate date, Shop shop, Set<Employee> employees) {
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
