package com.diamondLounge.entity.db;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.math.RoundingMode.FLOOR;

@Entity
@Table(name = "wages")
public class Wage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private BigDecimal hourlyWage = new BigDecimal(0);

    @Column
    private LocalDateTime startDate;

    @Column
    private LocalDateTime endDate;

    public Wage() {
    }

    public Wage(BigDecimal hourlyWage, LocalDateTime startDate, LocalDateTime endDate) {
        this.hourlyWage = hourlyWage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getHourlyWage() {
        return hourlyWage;
    }

    public void setHourlyWage(BigDecimal hourlyWage) {
        this.hourlyWage = hourlyWage;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean hasTheSameValue(BigDecimal hourlyWage) {
        return hourlyWage.setScale(2, FLOOR).equals(this.hourlyWage.setScale(2, FLOOR));
    }
}
