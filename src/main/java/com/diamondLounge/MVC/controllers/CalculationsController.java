package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers;
import com.diamondLounge.MVC.services.CalculationsService;
import com.diamondLounge.entity.model.WeekDateRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CalculationsController {

    @Autowired
    CalculationsService calculationsController;

    @RequestMapping("/getCalculations")
    public ModelAndView handleError(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                    ModelAndView modelAndView,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            WeekDateRange forDateRange = new WeekDateRange(offset);
            model.addAttribute("salaryList", calculationsController.getSalaryList(forDateRange));
            model.addAttribute("startingDate", forDateRange.getWeekStart());
            model.addAttribute("endingDate", forDateRange.getWeekEndExclusive());
        } catch (Exception e) {
            ErrorHandlerForControllers.handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("/calculations");
        return modelAndView;
    }
}
