package com.diamondLounge.MVC.controllers;

import com.diamondLounge.MVC.services.ShopService;
import com.diamondLounge.entity.db.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;

import static com.diamondLounge.MVC.controllers.utils.ErrorHandlerForControllers.handleError;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/shops")
public class ShopController {

    @Autowired
    ShopService shopModel;

    @RequestMapping(value = "/editShopInformation", method = GET)
    public ModelAndView getEditShopPage(@RequestParam(value = "selectedShop", required = false, defaultValue = "-1") int selectedShop,
                                        Model model,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("shopList", shopModel.getAllShops());
            if (selectedShop != -1) {
                Shop shop = shopModel.getShopById(selectedShop);
                model.addAttribute("selectedShop", shop);
            }
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        modelAndView.setViewName("settings/shopInformation");
        return modelAndView;
    }

    @RequestMapping(value = "/addShop", method = POST)
    public String addShop(
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("openingTime") LocalTime openingTime,
            @RequestParam("closingTime") LocalTime closingTime,
            @RequestParam("requiredStaff") int requiredStaff,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            shopModel.addShop(name, location, openingTime, closingTime, requiredStaff);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/shops/editShopInformation";
    }

    @RequestMapping(value = "/editShop", method = POST)
    public String editShop(
            @RequestParam("id") int id,
            @RequestParam("name") String name,
            @RequestParam("location") String location,
            @RequestParam("openingTime") LocalTime openingTime,
            @RequestParam("closingTime") LocalTime closingTime,
            @RequestParam("requiredStaff") int requiredStaff,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            shopModel.editShop(id, name, location, openingTime, closingTime, requiredStaff);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/shops/editShopInformation";
    }

    @RequestMapping(value = "/deleteShop", method = POST)
    public String deleteShop(
            @RequestParam("id") int id,
            ModelAndView modelAndView,
            RedirectAttributes redirectAttributes) {
        try {
            shopModel.deleteShop(id);
        } catch (Exception e) {
            handleError(modelAndView, redirectAttributes, e);
        }

        return "redirect:/shops/editShopInformation";
    }
}