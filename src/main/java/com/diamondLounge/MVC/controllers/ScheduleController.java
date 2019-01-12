package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.ScheduleService;
import com.diamondLounge.entity.models.WeekDateRange;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.diamondLounge.MVC.controllers.utils.ErrorHandlerForControllers.handleError;
import static com.diamondLounge.utility.ExcelConverter.getHttpExcelHeaders;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleModel;

    @RequestMapping(value = "/home", method = GET)
    public ModelAndView getSchedule(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                    Model model,
                                    ModelAndView modelAndView,
                                    RedirectAttributes redirectAttributes) {
        try {
            WeekDateRange weekDateRange = new WeekDateRange(offset);
            model.addAttribute("scheduleTable", scheduleModel.getScheduleTableForRange(weekDateRange));
            model.addAttribute("startingDate", weekDateRange.getWeekStart());
            model.addAttribute("endingDate", weekDateRange.getWeekEndExclusive());
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return modelAndView;
    }

    @RequestMapping(value = "/eraseSchedule", method = POST)
    public ModelAndView erase(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                              ModelAndView modelAndView,
                              RedirectAttributes redirectAttributes) {
        try {
            WeekDateRange weekDateRange = new WeekDateRange(offset);
            scheduleModel.eraseSchedulesForRange(weekDateRange);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("redirect:/home?offset=" + offset);
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

    @RequestMapping(value = "/exportScheduleToExcel", method = GET)
    @ResponseBody
    public HttpEntity<byte[]> exportScheduleToExcel(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WeekDateRange weekDateRange = new WeekDateRange(offset);
        HttpHeaders headers = getHttpExcelHeaders("schedule_" + weekDateRange.getWeekStart() + "_" + weekDateRange.getWeekEndExclusive());

        Workbook workbook = scheduleModel.createScheduleExcelWorkbook(weekDateRange);
        workbook.write(byteArrayOutputStream);

        return new HttpEntity<>(byteArrayOutputStream.toByteArray(), headers);
    }
}
