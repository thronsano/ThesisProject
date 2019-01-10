package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sold_wares")
public class WarePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal price;

    @Column
    private LocalDateTime dateSold;

    @OneToOne
    private Employee employee;

    public WarePart() {
    }

    public WarePart(BigDecimal amount, BigDecimal price, Employee employee, LocalDateTime dateSold) {
        this.amount = amount;
        this.price = price;
        this.employee = employee;
        this.dateSold = dateSold;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getDateSold() {
        return dateSold;
    }

    public void setDateSold(LocalDateTime dateSold) {
        this.dateSold = dateSold;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
