package com.diamondLounge.MVC.controllers;


import com.diamondLounge.MVC.services.EmployeeService;
import com.diamondLounge.entity.models.EmployeeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

import static com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "/editEmployeeInformation", method = GET)
    public ModelAndView getEditEmployeePage(@RequestParam(value = "selectedEmployee", required = false, defaultValue = "-1") int selectedEmployee,
                                            Model model,
                                            ModelAndView modelAndView,
                                            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("employeeList", employeeService.getAllEmployees());
            if (selectedEmployee != -1) {
                EmployeeModel employee = employeeService.getEmployeeImplById(selectedEmployee);
                model.addAttribute("selectedEmployee", employee);
            }
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("settings/employeeInformation");
        return modelAndView;
    }

    @RequestMapping(value = "/addEmployee", method = POST)
    public String addEmployee(
            @RequestParam("name") String name,
            @RequestParam("timeFactor") float timeFactor,
            @RequestParam("location") String location,
            @RequestParam("wage") BigDecimal wage,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            employeeService.addEmployee(name, timeFactor, location, wage);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/employees/editEmployeeInformation";
    }

    @RequestMapping(value = "/editEmployee", method = POST)
    public String editEmployee(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("timeFactor") float timeFactor,
            @RequestParam("location") String location,
            @RequestParam("wage") BigDecimal wage,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            employeeService.editEmployee(id, name, timeFactor, location, wage);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/employees/editEmployeeInformation";
    }

    @RequestMapping(value = "/deleteEmployee", method = POST)
    public String deleteEmployee(
            @RequestParam("id") int id,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/employees/editEmployeeInformation";
    }
}
