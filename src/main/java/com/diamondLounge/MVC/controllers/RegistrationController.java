package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.exceptions.UsernamePasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/register", method = GET)
    public ModelAndView getRegisterPage(ModelAndView modelAndView) {
        modelAndView.setViewName("publicTemplates/register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = POST)
    public ModelAndView createAccount(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("username") String username,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {

        try {
            userService.createUser(email, password, confirmPassword, username);
            redirectAttributes.addFlashAttribute("redirectionMessage", "New account created!");
            modelAndView.setViewName("redirect:/");
        } catch (UsernamePasswordException ex) {
            handleError(modelAndView, redirectAttributes, ex);
            modelAndView.setViewName("publicTemplates/register");
        }

        return modelAndView;
    }
}
