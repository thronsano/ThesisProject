package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.exceptions.UsernamePasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService = new UserService();

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView getRegisterPage(ModelAndView modelAndView) {
        modelAndView.setViewName("publicTemplates/register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createAccount(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String confirmPassword,
            @RequestParam("username") String username,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(email, password, confirmPassword, username, passwordEncoder);
            redirectAttributes.addFlashAttribute("userCreated", true);
            modelAndView.setViewName("redirect:/");
        } catch (UsernamePasswordException ex) {
            modelAndView.addObject("error", ex.getMessage());
            modelAndView.setViewName("publicTemplates/register");
        }

        return modelAndView;
    }
}