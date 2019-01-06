package com.diamondLounge.MVC.controller.Utils;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.utility.Logger.logWarning;

public class ErrorHandlerForControllers {

    public static void handleError(ModelAndView modelAndView, RedirectAttributes redirectAttributes, String message) {
        logWarning(message);
        redirectAttributes.addFlashAttribute("error", message);
        modelAndView.addObject("error", message);
    }
}
