package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.controllers.utils.ErrorHandlerForControllers;
import com.diamondLounge.MVC.services.CalculationsService;
import com.diamondLounge.MVC.services.EmployeeService;
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

import static com.diamondLounge.utility.ExcelConverter.getHttpExcelHeaders;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class CalculationsController {

    @Autowired
    CalculationsService calculationsController;

    @Autowired
    EmployeeService employeeService;

    @RequestMapping("/getCalculations")
    public ModelAndView handleError(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                    ModelAndView modelAndView,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            WeekDateRange forDateRange = new WeekDateRange(offset);
            model.addAttribute("startingDate", forDateRange.getWeekStart());
            model.addAttribute("endingDate", forDateRange.getWeekEndExclusive());
            model.addAttribute("waresValue", calculationsController.getRemainingWaresValue());
            model.addAttribute("reportList", calculationsController.getFinancialReportList(forDateRange));
            model.addAttribute("employeeList", employeeService.getAllEmployees());
        } catch (Exception e) {
            ErrorHandlerForControllers.handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("/calculations");
        return modelAndView;
    }

    @RequestMapping(value = "/exportSalaryToExcel", method = GET)
    @ResponseBody
    public HttpEntity<byte[]> exportSalaryToExcel(@RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                  @RequestParam("employeeId") int employeeId) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        WeekDateRange weekDateRange = new WeekDateRange(offset);
        HttpHeaders headers = getHttpExcelHeaders("salary" + weekDateRange.getWeekStart() + "_" + weekDateRange.getWeekEndExclusive());

        Workbook workbook = calculationsController.createSalaryExcelWorkbook(weekDateRange, employeeId);
        workbook.write(byteArrayOutputStream);

        return new HttpEntity<>(byteArrayOutputStream.toByteArray(), headers);
    }
}
