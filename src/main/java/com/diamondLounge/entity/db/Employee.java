package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private float timeFactor;

    @Column
    private String localization;

    @Column
    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<Wage> wages = new HashSet<>();

    public Employee() {
    }

    public Employee(String name, float timeFactor, String localization, Set<Wage> wages) {
        this.name = name;
        this.timeFactor = timeFactor;
        this.localization = localization;
        this.wages = wages;
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

    public float getTimeFactor() {
        return timeFactor;
    }

    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }

    public Set<Wage> getWages() {
        return wages;
    }

    public void setWages(Set<Wage> wages) {
        this.wages = wages;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
