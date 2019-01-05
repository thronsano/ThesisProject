package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Wage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import java.util.Set;

public class EmployeeImpl {

    private int id;
    private String name;
    private float timeFactor;
    private String localization;
    private Set<Wage> wages;
    private Wage currentWage;
    private double currentWageVal;

    public EmployeeImpl(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.timeFactor = employee.getTimeFactor();
        this.localization = employee.getLocalization();
        this.wages = employee.getWages();
        this.currentWage = wages.stream().filter(x -> x.getEndDate() == null).findAny().orElse(new Wage());
        this.currentWageVal = currentWage.getHourlyWage().doubleValue();
    }

    public double getCurrentWageVal() {
        return currentWageVal;
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

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public Set<Wage> getWages() {
        return wages;
    }

    public ImmutableList<Wage> getSortedWages() {
        Ordering<Wage> wageOrdering = Ordering.natural().onResultOf(Wage::getStartDate);
        return ImmutableSortedSet.orderedBy(wageOrdering).addAll(getWages()).build().asList();
    }

    public void setWages(Set<Wage> wages) {
        this.wages = wages;
    }

    public Wage getCurrentWage() {
        return currentWage;
    }

    public void setCurrentWage(Wage currentWage) {
        this.currentWage = currentWage;
    }
}
