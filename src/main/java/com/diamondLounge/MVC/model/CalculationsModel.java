package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.db.WorkDay;
import com.diamondLounge.entity.model.EmployeeImpl;
import com.diamondLounge.entity.model.EmployeeSalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.math.BigDecimal.valueOf;
import static java.time.LocalTime.MAX;
import static java.util.stream.Collectors.toList;

@Repository
public class CalculationsModel extends PersistenceModel<Object> {

    @Autowired
    EmployeeModel employeeModel;

    public List<EmployeeSalary> getSalaryList() {
        List<EmployeeSalary> employeeSalaries = new ArrayList<>();
        List<EmployeeImpl> allEmployees = employeeModel.getAllEmployees();

        allEmployees.forEach(employee -> {
            BigDecimal salary = computedSalary(employee);
            EmployeeSalary employeeSalary = new EmployeeSalary(employee, salary);
            employeeSalaries.add(employeeSalary);
        });

        return employeeSalaries;
    }

    private BigDecimal computedSalary(EmployeeImpl employee) {
        BigDecimal salary = new BigDecimal(BigInteger.ZERO);

        for (WorkDay workDay : employee.getWorkDays()) {
            LocalDate date = workDay.getDate();
            BigDecimal dailySalary = getWageForDate(employee, date).multiply(valueOf(workDay.getHoursWorked().toHours()));
            salary = salary.add(dailySalary);
        }

        return salary;
    }

    private BigDecimal getWageForDate(EmployeeImpl employee, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(MAX);

        return employee.getWages().stream()
                .filter(x -> x.getStartDate().isBefore(dayStart) && (x.getEndDate() == null || x.getEndDate().isAfter(dayEnd)))
                .findFirst()
                .orElse(calculateAvg(employee, dayStart, dayEnd))
                .getHourlyWage();
    }

    private Wage calculateAvg(EmployeeImpl empoloyee, LocalDateTime dateStart, LocalDateTime dateEnd) {
        List<BigDecimal> fallsIntoRange = empoloyee.getWages().stream()
                .filter(fallsWithinRange(dateStart, dateEnd))
                .map(Wage::getHourlyWage)
                .collect(toList());

        if (fallsIntoRange.size() == 0) {
            return new Wage(BigDecimal.ZERO, dateStart, dateEnd);
        }

        BigDecimal sum = fallsIntoRange.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Wage(sum.divide(new BigDecimal(fallsIntoRange.size()), RoundingMode.HALF_UP), dateStart, dateEnd);
    }

    private Predicate<Wage> fallsWithinRange(LocalDateTime dateStart, LocalDateTime dateEnd) {
        return x -> x.getStartDate().isBefore(dateStart) && (x.getEndDate() == null || x.getEndDate().isAfter(dateEnd)) ||
                x.getStartDate().isAfter(dateStart) && (x.getEndDate() == null || x.getEndDate().isBefore(dateEnd)) ||
                x.getStartDate().isBefore(dateEnd) && (x.getEndDate() == null || x.getEndDate().isAfter(dateEnd));
    }
}
