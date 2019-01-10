package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.EmployeeService;
import com.diamondLounge.MVC.services.WareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers.handleError;
import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/wares")
public class WareController {

    @Autowired
    WareService wareService;

    @Autowired
    EmployeeService employeeService;

    @RequestMapping(value = "/editWareInformation", method = GET)
    public ModelAndView getEditWarePage(@RequestParam(value = "selectedWare", required = false, defaultValue = "-1") int selectedWare,
                                        Model model,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("waresList", wareService.getAllWares());
            model.addAttribute("soldWaresList", wareService.getAllWareParts());
            model.addAttribute("employeeList", employeeService.getAllEmployees());

            if (selectedWare != -1) {
                model.addAttribute("selectedWare", wareService.getWareById(selectedWare));
            }
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("settings/wareInformation");
        return modelAndView;
    }

    @RequestMapping(value = "/addWare", method = POST)
    public String addWare(@RequestParam("name") String name,
                          @RequestParam("amount") BigDecimal amount,
                          @RequestParam("price") BigDecimal price,
                          @RequestParam("description") String description,
                          ModelAndView modelAndView,
                          RedirectAttributes redirectAttributes) {
        try {
            wareService.addWare(name, amount, price, description);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/sellWare", method = POST)
    public String sellWare(
            @RequestParam("id") int id,
            @RequestParam("employeeId") int employeeId,
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(value = "time", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestParam("amount") BigDecimal amount,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime saleTime = date == null || time == null ? now() : of(date, time);
            wareService.sellWare(id, amount, employeeId, saleTime);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/editWare", method = POST)
    public String editWare(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.editWare(id, name, amount, price, description);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/deleteWare", method = POST)
    public String deleteWare(
            @RequestParam("id") int id,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.deleteWare(id);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }
}
