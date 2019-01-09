package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Schedule;
import com.diamondLounge.entity.db.WorkDay;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.model.EmployeeModel;
import com.diamondLounge.entity.model.ScheduleTableModel;
import com.diamondLounge.entity.model.ShopModel;
import com.diamondLounge.entity.model.WeekRange;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.diamondLounge.utility.Logger.logWarning;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Repository
public class ScheduleService extends PersistenceService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ShopService shopModel;

    @Autowired
    private SessionFactory sessionFactory;

    private List<Schedule> getSchedules(LocalDate fromDate, LocalDate toDate) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Schedule as sched where sched.date>=:fromDate AND sched.date<:toDate");
            query.setParameter("fromDate", fromDate);
            query.setParameter("toDate", toDate);
            return query.list();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void generateSchedule(int offset) throws DiamondLoungeException {
        List<Schedule> schedules = new ArrayList<>();
        WeekRange weekRange = new WeekRange(offset);
        Map<String, List<ShopModel>> shopMap = groupShopsByLocation(shopModel.getAllShops());
        Map<String, List<EmployeeModel>> employeeMap = groupEmployeesByLocation(employeeService.getAllEmployees());

        shopMap.forEach(
                (location, shops) -> {
                    List<EmployeeModel> availableEmployees = employeeMap.get(location);

                    shops.forEach(
                            shop -> weekRange.getLastMonday().datesUntil(weekRange.getNextMonday()).forEach(
                                    date -> schedules.add(new Schedule(date, shopModel.getShopById(shop.getId()), generateEmployeeList(date, shop, availableEmployees))))
                    );
                }
        );

        schedules.forEach(this::persistObject);
    }

    private Set<Employee> generateEmployeeList(LocalDate date, ShopModel shop, List<EmployeeModel> availableEmployees) {
        if (availableEmployees == null) {
            return null;
        }

        if (shop.getRequiredStaff() > availableEmployees.size()) {
            logWarning("Not enough staff for " + shop.getName() + "! " + availableEmployees.size() + "/" + shop.getRequiredStaff());
        }

        return availableEmployees.stream()
                .filter(x -> x.getWorkDays().stream().noneMatch(y -> y.getDate().isEqual(date)))
                .sorted(comparing(EmployeeModel::getTimeInSecondsWorked))
                .limit(shop.getRequiredStaff())
                .map(x -> {
                    WorkDay workDay = new WorkDay(date, shopModel.getShopById(shop.getId()), Duration.between(shop.getOpeningTime(), shop.getClosingTime()));
                    Employee employee = employeeService.getEmployeeById(x.getId());
                    availableEmployees.get(availableEmployees.indexOf(x)).getWorkDays().add(workDay);
                    x.getWorkDays().add(workDay);
                    employee.getWorkDays().add(workDay);
                    return employee;
                })
                .collect(toSet());
    }

    private Map<String, List<EmployeeModel>> groupEmployeesByLocation(List<EmployeeModel> employees) {
        return employees.stream().collect(groupingBy(EmployeeModel::getLocation));
    }

    private Map<String, List<ShopModel>> groupShopsByLocation(List<ShopModel> shops) {
        return shops.stream().collect(groupingBy(ShopModel::getLocation));
    }

    public void eraseThisWeekSchedule(int offset) {
        WeekRange weekRange = new WeekRange(offset);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();

            Query scheduleQuery = session.createQuery("delete from Schedule as sched where sched.date>=:fromDate AND sched.date<:toDate");
            Query workDayQuery = session.createQuery("delete from WorkDay as workday where workday.date>=:fromDate AND workday.date<:toDate");

            scheduleQuery.setParameter("fromDate", weekRange.getLastMonday());
            scheduleQuery.setParameter("toDate", weekRange.getNextMonday());
            workDayQuery.setParameter("fromDate", weekRange.getLastMonday());
            workDayQuery.setParameter("toDate", weekRange.getNextMonday());

            scheduleQuery.executeUpdate();
            workDayQuery.executeUpdate();
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public ScheduleTableModel getScheduleTable(int offset) {
        WeekRange weekRange = new WeekRange(offset);
        List<Schedule> schedules = getSchedules(weekRange.getLastMonday(), weekRange.getNextMonday());

        if (schedules.size() == 0) {
            return null;
        }

        return new ScheduleTableModel(schedules, weekRange.getDateRange());
    }
}