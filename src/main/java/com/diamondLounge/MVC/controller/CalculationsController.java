package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.controller.Utils.ErrorHandlerForControllers;
import com.diamondLounge.MVC.services.CalculationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CalculationsController {

    @Autowired
    CalculationsService calculationsController;

    @RequestMapping("/getCalculations")
    public ModelAndView handleError(ModelAndView modelAndView,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("salaryList", calculationsController.getSalaryList());
        } catch (Exception e) {
            ErrorHandlerForControllers.handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("/calculations");
        return modelAndView;
    }
}
