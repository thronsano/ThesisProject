package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.db.WarePart;
import com.diamondLounge.entity.db.WorkDay;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.models.FinancialReport;
import com.diamondLounge.entity.models.WeekDateRange;
import com.diamondLounge.entity.models.dbWrappers.EmployeeModel;
import com.diamondLounge.entity.models.tables.SalaryTableModel;
import com.diamondLounge.utility.ExcelConverter;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return employeeService.getAllEmployees().stream()
                              .map(employee -> getFinancialReport(employee, forDateRange))
                              .collect(Collectors.toList());
    }

    private FinancialReport getFinancialReport(EmployeeModel employee, WeekDateRange forDateRange) {
        BigDecimal salary = getComputedSalary(employee, forDateRange).setScale(2, HALF_UP);
        BigDecimal earnings = getGeneratedIncome(employee, forDateRange).setScale(2, HALF_UP);
        return new FinancialReport(employee, salary, earnings);
    }

    private BigDecimal getComputedSalary(EmployeeModel employee, WeekDateRange forDateRange) {
        Set<WorkDay> workDaysToConsider = getWorkDaysInDateRange(employee, forDateRange);
        BigDecimal salary = ZERO;

        for (WorkDay workDay : workDaysToConsider) {
            BigDecimal dailySalary = getSalaryForDay(employee, workDay);
            salary = salary.add(dailySalary);
        }

        return salary;
    }

    private BigDecimal getSalaryForDay(EmployeeModel employee, WorkDay workDay) {
        long secondsWorked = workDay.getHoursWorked().getSeconds();
        LocalDate date = workDay.getDate();

        return getWageForDate(employee, date).multiply(valueOf(secondsWorked).setScale(5, HALF_UP)
                                                                             .divide(valueOf(3600), HALF_UP));
    }

    private Set<WorkDay> getWorkDaysInDateRange(EmployeeModel employee, WeekDateRange forDateRange) {
        return employee.getWorkDays().stream()
                       .filter(x -> isSameOrAfter(x.getDate(), forDateRange.getWeekStart()) &&
                                    isSameOrBefore(x.getDate(), forDateRange.getWeekEndExclusive()))
                       .collect(toSet());
    }

    private BigDecimal getWageForDate(EmployeeModel employee, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(MAX);

        return employee.getWages().stream()
                       .filter(singleWageMatchesEntirePeriod(dayStart, dayEnd))
                       .findAny()
                       .orElseGet(() -> calculateFragmentedWage(employee, dayStart, dayEnd))
                       .getHourlyWage();
    }

    private Wage calculateFragmentedWage(EmployeeModel employee, LocalDateTime periodStart, LocalDateTime periodEnd) {
        long totalPeriodLength = periodEnd.toEpochSecond(UTC) - periodStart.toEpochSecond(UTC);

        BigDecimal calculatedWage = employee.getWages().stream()
                                            .filter(wageFallsWithinPeriodTimeRange(periodStart, periodEnd))
                                            .map(wage -> calculateWageFragment(periodStart, periodEnd, totalPeriodLength, wage))
                                            .reduce(ZERO, BigDecimal::add);

        return new Wage(calculatedWage, periodStart, periodEnd);
    }

    private BigDecimal calculateWageFragment(LocalDateTime periodStart, LocalDateTime periodEnd, long totalPeriodLength, Wage wage) {
        LocalDateTime fragmentStart = getLaterDateTime(wage.getStartDate(), periodStart);
        LocalDateTime fragmentEnd = getEarlierDateTime(wage.getEndDate(), periodEnd);
        double periodFragmentLength = fragmentEnd.toEpochSecond(UTC) - fragmentStart.toEpochSecond(UTC);
        return wage.getHourlyWage().multiply(valueOf(periodFragmentLength / totalPeriodLength));
    }

    private Predicate<Wage> wageFallsWithinPeriodTimeRange(LocalDateTime periodStart, LocalDateTime periodEnd) {
        return x -> (x.getStartDate() == null || x.getStartDate().isBefore(periodStart)) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd)) ||
                    x.getStartDate() != null && x.getStartDate().isAfter(periodStart) && (x.getEndDate() == null || x.getEndDate().isBefore(periodEnd)) ||
                    (x.getStartDate() == null || x.getStartDate().isBefore(periodEnd)) && (x.getEndDate() == null || x.getEndDate().isAfter(periodEnd));
    }

    private Predicate<Wage> singleWageMatchesEntirePeriod(LocalDateTime dayStart, LocalDateTime dayEnd) {
        return x -> (x.getStartDate() == null || x.getStartDate().isBefore(dayStart)) &&
                    (x.getEndDate() == null || x.getEndDate().isAfter(dayEnd));
    }

    private BigDecimal getGeneratedIncome(EmployeeModel employee, WeekDateRange forDateRange) throws DiamondLoungeException {
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

    public Workbook createSalaryExcelWorkbook(WeekDateRange weekDateRange, int employeeId) {
        Map<WorkDay, BigDecimal> daySalaryMap = new HashMap<>();
        EmployeeModel employee = employeeService.getEmployeeModelById(employeeId);
        Set<WorkDay> workDaysToConsider = getWorkDaysInDateRange(employee, weekDateRange);

        for (WorkDay workDay : workDaysToConsider) {
            daySalaryMap.put(workDay, getSalaryForDay(employee, workDay));
        }

        return ExcelConverter.convertToWorkbook(new SalaryTableModel(daySalaryMap), "Salary");
    }
}
