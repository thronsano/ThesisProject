package com.diamondLounge.MVC.controller;


import com.diamondLounge.MVC.model.EmployeeModel;
import com.diamondLounge.entity.model.EmployeeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

import static com.diamondLounge.MVC.controller.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/employees")
public class EmployeeController {

    @Autowired
    EmployeeModel employeeModel;

    @RequestMapping(value = "/editEmployeeInformation", method = GET)
    public ModelAndView getEditEmployeePage(@RequestParam(value = "selectedEmployee", required = false, defaultValue = "-1") int selectedEmployee,
                                            Model model,
                                            ModelAndView modelAndView,
                                            RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("employeeList", employeeModel.getAllEmployees());
            if (selectedEmployee != -1) {
                EmployeeImpl employee = employeeModel.getEmployeeImplById(selectedEmployee);
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
            @RequestParam("wage") double wage,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            employeeModel.addEmployee(name, timeFactor, location, BigDecimal.valueOf(wage));
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
            @RequestParam("wage") double wage,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            employeeModel.editEmployee(id, name, timeFactor, location, BigDecimal.valueOf(wage));
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
            employeeModel.deleteEmployee(id);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/employees/editEmployeeInformation";
    }
}
