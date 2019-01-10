package com.diamondLounge.entity.models;

import java.math.BigDecimal;

public class FinancialReport {
    private EmployeeModel employee;
    private BigDecimal salary;
    private BigDecimal earnings;

    public FinancialReport(EmployeeModel employee, BigDecimal salary, BigDecimal earnings) {
        this.employee = employee;
        this.salary = salary;
        this.earnings = earnings;
    }

    public EmployeeModel getEmployee() {
        return employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public BigDecimal getEarnings() {
        return earnings;
    }

    public BigDecimal getResult() {
        return earnings.subtract(salary);
    }
}
