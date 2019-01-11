package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("publicTemplates/forgotPassword");
    }

    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView,
                                                  @RequestParam("email") String userEmail,
                                                  HttpServletRequest request) {
        try {
            userService.sendResetToken(userEmail, request);
            modelAndView.addObject("successEmail", true);
        } catch (DiamondLoungeException e) {
            modelAndView.addObject("error", e.getMessage());
        }

        modelAndView.setViewName("publicTemplates/forgotPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView) {

        modelAndView.setViewName("publicTemplates/resetPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView,
                                       @RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirm") String confirmPassword,
                                       RedirectAttributes redir) {

        try {
            userService.resetPasswordWithToken(token, password, confirmPassword, passwordEncoder);
            redir.addFlashAttribute("redirectionMessage", "You have successfully reset your password");
            modelAndView.setViewName("redirect:/");
        } catch (DiamondLoungeException e) {
            redir.addFlashAttribute("error", e.getMessage());
            modelAndView.setViewName("publicTemplates/resetPassword");
        }
        return modelAndView;
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:publicTemplates/login");
    }
}

