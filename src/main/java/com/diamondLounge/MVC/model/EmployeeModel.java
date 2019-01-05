package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.model.EmployeeImpl;
import com.diamondLounge.exceptions.DiamondLoungeException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.diamondLounge.utility.Logger.logWarning;
import static com.google.common.collect.Sets.newHashSet;
import static java.time.LocalDateTime.now;

@Repository
public class EmployeeModel {

    @Autowired
    SessionFactory sessionFactory;

    public void addEmployee(String name, float timeFactor, String localization, BigDecimal wage) throws DiamondLoungeException {
        Set<Wage> wages = newHashSet(new Wage(wage, now(), null));
        Employee employee = new Employee(name, timeFactor, localization, wages);
        persistEmployee(employee);
    }

    public List<EmployeeImpl> getAllEmployees() throws DiamondLoungeException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Employee");
            return convertFromDbObjects(query.list());
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private List<EmployeeImpl> convertFromDbObjects(List<Employee> employeeList) {
        return employeeList.stream().map(EmployeeImpl::new).collect(Collectors.toList());
    }

    public EmployeeImpl getEmployeeImplById(int selectedEmployee) throws DiamondLoungeException {
        return new EmployeeImpl(getEmployeeById(selectedEmployee));
    }

    private Employee getEmployeeById(int selectedEmployee) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Employee where id=:selectedId");
            query.setParameter("selectedId", selectedEmployee);
            return (Employee) query.uniqueResult();
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editEmployee(int id, String name, float timeFactor, String localization, BigDecimal wage) throws DiamondLoungeException {
        Employee employee = getEmployeeById(id);
        employee.setName(name);
        employee.setTimeFactor(timeFactor);
        employee.setLocalization(localization);

        Wage currentWage = employee.getWages().stream().filter(x -> x.getEndDate() == null).findAny().orElse(null);

        if (currentWage != null && !currentWage.hasTheSameValue(wage)) {
            LocalDateTime now = now();
            currentWage.setEndDate(now);
            employee.getWages().add(new Wage(wage, now, null));
        }

        persistEmployee(employee);
    }

    private void persistEmployee(Employee employee) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.saveOrUpdate(employee);
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
