package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.model.UserModel;
import com.diamondLounge.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    UserModel userModel;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSettings(ModelAndView modelAndView) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelAndView.addObject("user", userModel.getUserByEmail(auth.getName()));
        modelAndView.setViewName("accountSettings");
        return modelAndView;
    }

    @RequestMapping(value = "/editAccountInformation", method = RequestMethod.GET)
    public ModelAndView getEditUser(@RequestParam("email") String email,
                                    ModelAndView modelAndView) {
        User user = userModel.getUserByEmail(email);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("editUser");
        return modelAndView;
    }

    @RequestMapping(value = "/editAccountInformation", method = RequestMethod.POST)
    public ModelAndView postEditUser(@RequestParam("name") String name,
                                     @RequestParam("surname") String surname,
                                     @RequestParam("hiddenEmail") String hiddenEmail,
                                     ModelAndView modelAndView,
                                     RedirectAttributes redirectAttributes) {

        String result = userModel.editUser(hiddenEmail, name, surname);

        if (result.equals("correct")) {
            redirectAttributes.addFlashAttribute("userEdited", true);
            modelAndView.setViewName("redirect:/settings");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            modelAndView.setViewName("redirect:/settings");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ModelAndView getChangePassword(@RequestParam("email") String email,
                                          ModelAndView modelAndView) {
        User user = userModel.getUserByEmail(email);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("changePassword");
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView postChangePassword(@RequestParam("email") String email,
                                           @RequestParam("currentPassword") String currentPassword,
                                           @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmNewPassword") String confirmNewPassword,
                                           ModelAndView modelAndView,
                                           RedirectAttributes redirectAttributes) {

        String result = userModel.changePassword(email, currentPassword, newPassword, confirmNewPassword, passwordEncoder);

        if (result.equals("correct")) {
            redirectAttributes.addFlashAttribute("passwordChanged", true);
            modelAndView.setViewName("redirect:/settings");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            modelAndView.setViewName("redirect:/settings");
        }
        return modelAndView;
    }
}
