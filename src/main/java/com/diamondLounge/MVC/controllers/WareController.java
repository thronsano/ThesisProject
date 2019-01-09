package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.WareService;
import com.diamondLounge.entity.db.Ware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.diamondLounge.MVC.controllers.Utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/wares")
public class WareController {

    @Autowired
    WareService wareService;

    @RequestMapping(value = "/editWareInformation", method = GET)
    public ModelAndView getEditWarePage(@RequestParam(value = "selectedWare", required = false, defaultValue = "-1") int selectedWare,
                                        Model model,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("waresList", wareService.getAllWares());
            model.addAttribute("soldWaresList", wareService.getAllWareParts());

            if (selectedWare != -1) {
                Ware ware = wareService.getWareById(selectedWare);
                model.addAttribute("selectedWare", ware);
            }
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("settings/wareInformation");
        return modelAndView;
    }

    @RequestMapping(value = "/addWare", method = POST)
    public String addWare(
            @RequestParam("name") String name,
            @RequestParam("amount") double amount,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.addWare(name, amount, price, description);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/sellWare", method = POST)
    public String sellWare(
            @RequestParam("id") int id,
            @RequestParam("amount") double amount,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.sellWare(id, amount);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/editWare", method = POST)
    public String editWare(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("amount") double amount,
            @RequestParam("price") double price,
            @RequestParam("description") String description,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.editWare(id, name, amount, price, description);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }

    @RequestMapping(value = "/deleteWare", method = POST)
    public String deleteWare(
            @RequestParam("id") int id,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            wareService.deleteWare(id);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/wares/editWareInformation";
    }
}
