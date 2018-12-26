package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.model.UserModel;
import com.diamondLounge.entity.db.User;
import com.diamondLounge.exceptions.DiamondLoungeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.utility.Logger.logWarning;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    UserModel userModel;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getSettings(ModelAndView modelAndView) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("user", userModel.getUserByEmail(email));
        modelAndView.setViewName("settings/accountSettings");
        return modelAndView;
    }

    @RequestMapping(value = "/editAccountInformation", method = RequestMethod.GET)
    public ModelAndView getEditUser(ModelAndView modelAndView) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userModel.getUserByEmail(email);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("settings/editUser");
        return modelAndView;
    }

    @RequestMapping(value = "/editAccountInformation", method = RequestMethod.POST)
    public ModelAndView postEditUser(@RequestParam("username") String username,
                                     ModelAndView modelAndView,
                                     RedirectAttributes redirectAttributes) {

        try {
            userModel.editUser(username);
            redirectAttributes.addFlashAttribute("userEdited", true);
            modelAndView.setViewName("redirect:/settings");
        } catch (DiamondLoungeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            modelAndView.setViewName("redirect:/settings");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ModelAndView getChangePassword(ModelAndView modelAndView) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userModel.getUserByEmail(email);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("settings/changePassword");
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ModelAndView postChangePassword(@RequestParam("currentPassword") String currentPassword,
                                           @RequestParam("newPassword") String newPassword,
                                           @RequestParam("confirmNewPassword") String confirmNewPassword,
                                           ModelAndView modelAndView,
                                           RedirectAttributes redirectAttributes) {

        try {
            userModel.changePassword(currentPassword, newPassword, confirmNewPassword, passwordEncoder);
            redirectAttributes.addFlashAttribute("passwordChanged", true);
            modelAndView.setViewName("redirect:/settings");
        } catch (Exception e) {
            logWarning(e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            modelAndView.setViewName("redirect:/settings");
        }

        return modelAndView;
    }
}
