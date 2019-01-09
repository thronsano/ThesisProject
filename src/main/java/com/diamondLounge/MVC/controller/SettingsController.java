package com.diamondLounge.MVC.controller;

import com.diamondLounge.MVC.services.EmployeeService;
import com.diamondLounge.MVC.services.UserService;
import com.diamondLounge.entity.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.MVC.controller.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/settings")
public class SettingsController {

    @Autowired
    UserService userService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/editAccountInformation", method = GET)
    public ModelAndView getEditUserPage(ModelAndView modelAndView) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByEmail(email);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("settings/editUser");

        return modelAndView;
    }


    @RequestMapping(value = "/editAccountInformation", method = POST)
    public ModelAndView editUser(@RequestParam("username") String username,
                                 ModelAndView modelAndView,
                                 RedirectAttributes redirectAttributes) {

        try {
            userService.editUser(username);
            redirectAttributes.addFlashAttribute("userEdited", true);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("redirect:/settings");
        return modelAndView;
    }

    @RequestMapping(value = "/changePassword", method = POST)
    public ModelAndView changePassword(@RequestParam("currentPassword") String currentPassword,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmNewPassword") String confirmNewPassword,
                                       ModelAndView modelAndView,
                                       RedirectAttributes redirectAttributes) {

        try {
            userService.changePassword(currentPassword, newPassword, confirmNewPassword, passwordEncoder);
            redirectAttributes.addFlashAttribute("passwordChanged", true);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("redirect:/settings");
        return modelAndView;
    }
}
