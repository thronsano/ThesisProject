package com.diamondLounge.MVC.controllers.Utils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.utility.Logger.logWarning;

public class ErrorHandlerForControllers {

    public static void handleError(ModelAndView modelAndView, RedirectAttributes redirectAttributes, Exception exception) {
        exception.printStackTrace();
        logWarning(exception.getMessage());
        redirectAttributes.addFlashAttribute("error", exception.getMessage());
        modelAndView.addObject("error", exception.getMessage());
    }
}
