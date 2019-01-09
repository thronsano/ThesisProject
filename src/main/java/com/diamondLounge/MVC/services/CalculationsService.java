package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.db.WorkDay;
import com.diamondLounge.entity.model.EmployeeModel;
import com.diamondLounge.entity.model.EmployeeSalary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.time.LocalTime.MAX;
import static java.util.stream.Collectors.toList;

@Repository
public class CalculationsService extends PersistenceService<Object> {

    @Autowired
    EmployeeService employeeService;

    public List<EmployeeSalary> getSalaryList() {
        List<EmployeeSalary> employeeSalaries = new ArrayList<>();
        List<EmployeeModel> allEmployees = employeeService.getAllEmployees();

        allEmployees.forEach(employee -> {
            BigDecimal salary = computedSalary(employee);
            EmployeeSalary employeeSalary = new EmployeeSalary(employee, salary);
            employeeSalaries.add(employeeSalary);
        });

        return employeeSalaries;
    }

    private BigDecimal computedSalary(EmployeeModel employee) {
        BigDecimal salary = ZERO;

        for (WorkDay workDay : employee.getWorkDays()) {
            LocalDate date = workDay.getDate();
            BigDecimal dailySalary = getWageForDate(employee, date).multiply(valueOf(workDay.getHoursWorked().toHours()));
            salary = salary.add(dailySalary);
        }

        return salary;
    }

    private BigDecimal getWageForDate(EmployeeModel employee, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(MAX);

        return employee.getWages().stream()
                .filter(x -> x.getStartDate().isBefore(dayStart) && (x.getEndDate() == null || x.getEndDate().isAfter(dayEnd)))
                .findFirst()
                .orElse(calculateAvg(employee, dayStart, dayEnd))
                .getHourlyWage();
    }

    private Wage calculateAvg(EmployeeModel empoloyee, LocalDateTime dateStart, LocalDateTime dateEnd) {
        List<BigDecimal> hourlyWageInDateRange = empoloyee.getWages().stream()
                .filter(fallsWithinRange(dateStart, dateEnd))
                .map(Wage::getHourlyWage)
                .collect(toList());

        if (hourlyWageInDateRange.size() == 0) {
            return new Wage(ZERO, dateStart, dateEnd);
        }

        BigDecimal sum = hourlyWageInDateRange.stream()
                .map(Objects::requireNonNull)
                .reduce(ZERO, BigDecimal::add);

        return new Wage(sum.divide(new BigDecimal(hourlyWageInDateRange.size()), RoundingMode.HALF_UP), dateStart, dateEnd);
    }

    private Predicate<Wage> fallsWithinRange(LocalDateTime dateStart, LocalDateTime dateEnd) {
        return x -> x.getStartDate().isBefore(dateStart) && (x.getEndDate() == null || x.getEndDate().isAfter(dateEnd)) ||
                x.getStartDate().isAfter(dateStart) && (x.getEndDate() == null || x.getEndDate().isBefore(dateEnd)) ||
                x.getStartDate().isBefore(dateEnd) && (x.getEndDate() == null || x.getEndDate().isAfter(dateEnd));
    }
}
