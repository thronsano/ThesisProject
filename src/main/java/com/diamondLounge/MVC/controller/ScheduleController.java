package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleModel scheduleModel;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView listAppointments(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         Model model,
                                         ModelAndView modelAndView) {
        model.addAttribute("schedule", scheduleModel.getShopWorkDays());
        return modelAndView;
    }

    @RequestMapping(value = "/generateSchedule", method = RequestMethod.POST)
    public ModelAndView generateSchedule(ModelAndView modelAndView) {
        scheduleModel.generateSchedule();
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
