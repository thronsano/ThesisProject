package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Wage;
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

import static com.diamondLounge.utility.Logger.logWarning;
import static com.google.common.collect.Sets.newHashSet;

@Repository
public class EmployeeModel {

    @Autowired
    SessionFactory sessionFactory;

    public void addEmployee(String name, float timeFactor, String localization, BigDecimal wage) throws DiamondLoungeException {
        Wage currentWage = new Wage(wage, LocalDateTime.now(), null);
        Set<Wage> wages = newHashSet(currentWage);
        Employee employee = new Employee(name, timeFactor, localization, wages);
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(employee);
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public List<Employee> getAllEmployees() throws DiamondLoungeException {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Employees");
            return query.list();
        } catch (Exception ex) {
            logWarning(ex.getMessage());
            throw new DiamondLoungeException(ex.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
