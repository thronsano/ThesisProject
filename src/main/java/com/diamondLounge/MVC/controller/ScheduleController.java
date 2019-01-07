package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.MVC.controller.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleModel scheduleModel;

    @RequestMapping(value = "/home", method = GET)
    public ModelAndView getSchedule(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                    Model model,
                                    ModelAndView modelAndView,
                                    RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("scheduleTable", scheduleModel.getScheduleTable(offset));
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/eraseSchedule", method = POST)
    public ModelAndView erase(ModelAndView modelAndView,
                              RedirectAttributes redirectAttributes) {
        try {
            scheduleModel.eraseAllSchedules();
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("redirect:/home");
        return modelAndView;
    }

    @RequestMapping(value = "/generateSchedule", method = POST)
    public ModelAndView generateSchedule(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                         ModelAndView modelAndView,
                                         RedirectAttributes redirectAttributes) {
        try {
            scheduleModel.generateSchedule(offset);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("redirect:/home?offset=" + offset);
        return modelAndView;
    }
}
