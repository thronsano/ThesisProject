package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers.handleError;

@Controller
public class PasswordController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView getForgotPasswordPage() {
        return new ModelAndView("publicTemplates/forgotPassword");
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView,
                                                  RedirectAttributes redirectAttributes,
                                                  @RequestParam("email") String email,
                                                  HttpServletRequest request) {
        try {
            userService.sendResetToken(email, request);
            modelAndView.addObject("statusParam", "Email sent!");
        } catch (Exception ex) {
            handleError(modelAndView, redirectAttributes, ex);
        }

        modelAndView.setViewName("publicTemplates/forgotPassword");

        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView getResetPasswordPage(ModelAndView modelAndView) {
        modelAndView.setViewName("publicTemplates/resetPassword");

        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView,
                                       @RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirm") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {
        try {
            userService.resetPasswordWithToken(token, password, confirmPassword);
            redirectAttributes.addFlashAttribute("redirectionMessage", "You have successfully reset your password");
            modelAndView.setViewName("redirect:/");
        } catch (DiamondLoungeException ex) {
            handleError(modelAndView, redirectAttributes, ex);
            modelAndView.setViewName("publicTemplates/resetPassword");
        }

        return modelAndView;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:publicTemplates/login");
    }
}

