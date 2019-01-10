package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

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
    private BigDecimal amount;

    @Column
    private BigDecimal price;

    @Column
    private String description;

    @OneToMany(fetch = EAGER, cascade = ALL, orphanRemoval = true)
    private Set<WarePart> soldParts;

    public Ware(String name, BigDecimal amount, BigDecimal price, String description, Set<WarePart> soldParts) {
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.description = description;
        this.soldParts = soldParts;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<WarePart> getSoldParts() {
        return soldParts;
    }

    public void setSoldParts(Set<WarePart> soldParts) {
        this.soldParts = soldParts;
    }
}
