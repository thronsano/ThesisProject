package com.diamondLounge.entity.model;

import java.math.BigDecimal;

public class EmployeeSalary {
    private EmployeeImpl employee;
    private BigDecimal salary;

    public EmployeeSalary(EmployeeImpl employee, BigDecimal salary) {
        this.employee = employee;
        this.salary = salary;
    }

    public EmployeeImpl getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeImpl employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
