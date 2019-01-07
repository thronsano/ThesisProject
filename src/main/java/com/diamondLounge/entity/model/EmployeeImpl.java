package com.diamondLounge.entity.model;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.db.WorkDay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Ordering;

import java.time.Duration;
import java.util.Set;

public class EmployeeImpl {

    private int id;
    private String name;
    private float timeFactor;
    private String location;
    private Set<Wage> wages;
    private Set<WorkDay> workDays;
    private Wage currentWage;
    private double currentWageVal;

    public EmployeeImpl(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.timeFactor = employee.getTimeFactor();
        this.location = employee.getLocation();
        this.wages = employee.getWages();
        this.workDays = employee.getWorkDays();

        currentWage = wages.stream().filter(x -> x.getEndDate() == null).findAny().orElse(new Wage());
        currentWageVal = currentWage.getHourlyWage().doubleValue();
    }

    public double getTimeInSecondsWorked() {
        return workDays.stream().map(WorkDay::getHoursWorked).mapToLong(Duration::getSeconds).sum();
    }

    public double getCurrentWageVal() {
        return currentWageVal;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getTimeFactor() {
        return timeFactor;
    }

    public String getLocation() {
        return location;
    }

    public Set<Wage> getWages() {
        return wages;
    }

    public ImmutableList<Wage> getSortedWages() {
        Ordering<Wage> wageOrdering = Ordering.natural().reverse().onResultOf(Wage::getStartDate);
        return ImmutableSortedSet.orderedBy(wageOrdering).addAll(getWages()).build().asList();
    }

    public Wage getCurrentWage() {
        return currentWage;
    }

    public Set<WorkDay> getWorkDays() {
        return workDays;
    }
}
