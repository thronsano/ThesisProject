package com.diamondLounge.entity.model;

import java.math.BigDecimal;

public class EmployeeSalary {
    private EmployeeModel employee;
    private BigDecimal salary;

    public EmployeeSalary(EmployeeModel employee, BigDecimal salary) {
        this.employee = employee;
        this.salary = salary;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeModel employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
