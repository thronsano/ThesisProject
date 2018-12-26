package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.model.UserModel;
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
    private UserModel userModel;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Display forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public ModelAndView displayForgotPasswordPage() {
        return new ModelAndView("forgotPassword");
    }

    // Process form submission from forgotPassword page
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    public ModelAndView processForgotPasswordForm(ModelAndView modelAndView, @RequestParam("email") String userEmail, HttpServletRequest request) {

        String result = userModel.sendResetToken(userEmail, request);

        if (result.equals("correct")) {
            modelAndView.addObject("successEmail", true);
        } else {
            modelAndView.addObject("error", result);
        }
        modelAndView.setViewName("forgotPassword");

        return modelAndView;

    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ModelAndView displayResetPasswordPage(ModelAndView modelAndView) {

        modelAndView.setViewName("resetPassword");
        return modelAndView;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public ModelAndView setNewPassword(ModelAndView modelAndView,
                                       @RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirm") String confirmPassword,
                                       RedirectAttributes redir) {

        String result = userModel.resetPasswordFromToken(token, password, confirmPassword, passwordEncoder);
        if (result.equals("correct")) {
            redir.addFlashAttribute("passwordChanged", true);
            modelAndView.setViewName("redirect:/");
        } else {
            modelAndView.addObject("error", result);
            modelAndView.setViewName("resetPassword");
        }
        return modelAndView;
    }

    // Going to reset page without a token redirects to login page
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParams(MissingServletRequestParameterException ex) {
        return new ModelAndView("redirect:login");
    }
}

