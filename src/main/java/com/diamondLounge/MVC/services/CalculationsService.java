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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.diamondLounge.utility.DateUtils.*;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;
import static java.time.LocalTime.MAX;
import static java.time.ZoneOffset.UTC;
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
                       .filter(singleWageMatchingEntirePeriod(dayStart, dayEnd))
                       .findAny()
                       .orElse(calculateFragmentedWage(employee, dayStart, dayEnd))
                       .getHourlyWage();
    }

    private Predicate<Wage> singleWageMatchingEntirePeriod(LocalDateTime dayStart, LocalDateTime dayEnd) {
        return x -> x.getStartDate().isBefore(dayStart) &&
                    (x.getEndDate() == null || x.getEndDate().isAfter(dayEnd));
    }

    private Wage calculateFragmentedWage(EmployeeModel employee, LocalDateTime periodStart, LocalDateTime periodEnd) {
        long totalPeriodLength = periodEnd.toEpochSecond(UTC) - periodStart.toEpochSecond(UTC);
        AtomicInteger amountOfMatchingElements = new AtomicInteger();

        BigDecimal fragmentedWage = employee.getWages().stream()
                                            .filter(fallsWithinPeriodTimeRange(periodStart, periodEnd))
                                            .map(wage -> calculateWeightOfWageFragment(periodStart, periodEnd, totalPeriodLength, amountOfMatchingElements, wage))
                                            .reduce(ZERO, BigDecimal::add)
                                            .divide(valueOf(amountOfMatchingElements.get()), HALF_UP);

        return new Wage(fragmentedWage, periodStart, periodEnd);
    }

    private BigDecimal calculateWeightOfWageFragment(LocalDateTime periodStart, LocalDateTime periodEnd, long totalPeriodLength, AtomicInteger amountOfMatchingElements, Wage wage) {
        amountOfMatchingElements.incrementAndGet();

        LocalDateTime fragmentStart = getLaterDateTime(wage.getStartDate(), periodStart);
        LocalDateTime fragmentEnd = getEarlierDateTime(wage.getEndDate(), periodEnd);
        long periodFragmentLength = fragmentEnd.toEpochSecond(UTC) - fragmentStart.toEpochSecond(UTC);

        return wage.getHourlyWage().multiply(valueOf(periodFragmentLength / totalPeriodLength));
    }

    private Predicate<Wage> fallsWithinPeriodTimeRange(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return x -> x.getStartDate().isBefore(periodStart) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd)) ||
                    x.getStartDate().isAfter(periodStart) && (x.getEndDate() == null || x.getEndDate().isBefore(periodEnd)) ||
                    x.getStartDate().isBefore(periodEnd) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd));
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
            handleError(ex);
        } finally {
            finishSession(session);
        }

        return ZERO;
    }

    public BigDecimal getRemainingWaresValue() {
        return wareService.getAllWares().stream()
                          .map(x -> x.getAmount().multiply(x.getPrice()))
                          .reduce(ZERO, BigDecimal::add)
                          .setScale(2, HALF_UP);
    }
}
