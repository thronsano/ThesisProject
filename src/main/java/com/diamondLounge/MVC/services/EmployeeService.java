package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Employee;
import com.diamondLounge.entity.db.Wage;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.models.EmployeeModel;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;
import static java.time.LocalDateTime.now;

@Repository
public class EmployeeService extends PersistenceService<Employee> {

    public void addEmployee(String name, float timeFactor, String location, BigDecimal wage) throws DiamondLoungeException {
        Set<Wage> wages = newHashSet(new Wage(wage, now(), null));
        Employee employee = new Employee(name, timeFactor, location, wages);
        persistObject(employee);
    }

    public List<EmployeeModel> getAllEmployees() throws DiamondLoungeException {
        return convertFromDbObjects(getAllObjects("Employee"));
    }

    private List<EmployeeModel> convertFromDbObjects(List<Employee> employeeList) {
        return employeeList.stream().map(EmployeeModel::new).collect(Collectors.toList());
    }

    public EmployeeModel getEmployeeImplById(int selectedEmployee) throws DiamondLoungeException {
        return new EmployeeModel(getEmployeeById(selectedEmployee));
    }

    Employee getEmployeeById(int selectedEmployee) throws DiamondLoungeException {
        return getObjectById("Employee", selectedEmployee);
    }

    public void editEmployee(int id, String name, float timeFactor, String location, BigDecimal wage) throws DiamondLoungeException {
        Employee employee = getEmployeeById(id);
        employee.setName(name);
        employee.setTimeFactor(timeFactor);
        employee.setLocation(location);

        Wage currentWage = employee.getWages().stream().filter(x -> x.getEndDate() == null).findAny().orElse(null);

        if (currentWage != null && !currentWage.hasTheSameValue(wage)) {
            LocalDateTime now = now();
            currentWage.setEndDate(now);
            employee.getWages().add(new Wage(wage, now, null));
        }

        persistObject(employee);
    }

    public void deleteEmployee(int id) {
        deleteObjectById("Employee", id);
    }
}
