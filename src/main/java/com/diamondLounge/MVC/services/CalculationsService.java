package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.db.WarePart;
import com.diamondLounge.entity.db.WorkDay;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.models.EmployeeModel;
import com.diamondLounge.entity.models.FinancialReport;
import com.diamondLounge.entity.models.WeekDateRange;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static com.diamondLounge.utility.DateUtils.isSameOrAfter;
import static com.diamondLounge.utility.DateUtils.isSameOrBefore;
import static com.diamondLounge.utility.Logger.logWarning;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalTime.MAX;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Repository
public class CalculationsService extends PersistenceService<Object> {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    private WareService wareService;

    public List<FinancialReport> getFinancialReportList(WeekDateRange forDateRange) {
        List<FinancialReport> employeeSalaries = new ArrayList<>();
        List<EmployeeModel> allEmployees = employeeService.getAllEmployees();

        allEmployees.forEach(employee -> {
            BigDecimal salary = getComputedSalary(employee, forDateRange);
            BigDecimal earnings = getEarnings(employee, forDateRange);
            FinancialReport financialReport = new FinancialReport(employee, salary, earnings);
            employeeSalaries.add(financialReport);
        });

        return employeeSalaries;
    }

    private BigDecimal getEarnings(EmployeeModel employee, WeekDateRange forDateRange) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from WarePart as part where part.dateSold<:rangeEnd and part.dateSold>:rangeStart and part.employee.id=:employeeId");
            query.setParameter("employeeId", employee.getId());
            query.setParameter("rangeStart", forDateRange.getWeekStartWithTime());
            query.setParameter("rangeEnd", forDateRange.getWeekEndWithTimeExclusive());

            List<WarePart> waresSold = query.list();

            return waresSold.stream()
                            .map(WarePart::getPrice)
                            .reduce(ZERO, BigDecimal::add);
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private BigDecimal getComputedSalary(EmployeeModel employee, WeekDateRange forDateRange) {
        BigDecimal salary = ZERO;
        Set<WorkDay> workDaysToConsider = employee.getWorkDays().stream()
                                                  .filter(x -> isSameOrAfter(x.getDate(), forDateRange.getWeekStart()) &&
                                                          isSameOrBefore(x.getDate(), forDateRange.getWeekEndExclusive()))
                                                  .collect(toSet());
        for (WorkDay workDay : workDaysToConsider) {
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
                       .filter(x -> x.getStartDate().isBefore(dayStart) &&
                               (x.getEndDate() == null || x.getEndDate().isAfter(dayEnd)))
                       .findAny()
                       .orElse(calculateAverageWage(employee, dayStart, dayEnd))
                       .getHourlyWage();
    }

    private Wage calculateAverageWage(EmployeeModel employee, LocalDateTime dateStart, LocalDateTime dateEnd) {
        List<BigDecimal> hourlyWageInDateRange = employee.getWages().stream()
                                                         .filter(fallsWithinRange(dateStart, dateEnd))
                                                         .map(Wage::getHourlyWage)
                                                         .collect(toList());

        if (hourlyWageInDateRange.size() == 0) {
            return new Wage(ZERO, dateStart, dateEnd);
        }

        BigDecimal sum = hourlyWageInDateRange.stream()
                                              .map(Objects::requireNonNull)
                                              .reduce(ZERO, BigDecimal::add);

        return new Wage(sum.divide(new BigDecimal(hourlyWageInDateRange.size()), HALF_UP), dateStart, dateEnd);
    }

    private Predicate<Wage> fallsWithinRange(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return x -> x.getStartDate().isBefore(periodStart) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd)) ||
                x.getStartDate().isAfter(periodStart) && (x.getEndDate() == null || x.getEndDate().isBefore(periodEnd)) ||
                x.getStartDate().isBefore(periodEnd) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd));
    }

    public BigDecimal getRemainingWaresValue() {
        return wareService.getAllWares().stream().map(x -> x.getAmount().multiply(x.getPrice())).reduce(ZERO, BigDecimal::add).setScale(2, HALF_UP);
    }
}
