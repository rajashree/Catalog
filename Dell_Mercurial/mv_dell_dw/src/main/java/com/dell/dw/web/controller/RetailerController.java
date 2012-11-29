package com.dell.dw.web.controller;

import com.dell.dw.managers.RetailerManager;
import com.dell.dw.persistence.domain.Retailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 7/4/12
 * Time: 6:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class RetailerController extends BaseDellDWController{

    @RequestMapping(value = "admin/retailers/list.do")
    public void getAllRetailers(Model model) {
        List<Retailer> retailers = retailerManager.getAllRetailers();
        model.addAttribute("retailers",retailers);
    }

    @RequestMapping(value = "admin/retailers/edit.do", method = RequestMethod.GET)
    public void edit(@RequestParam Long id, Model model) {
        Retailer retailer = retailerManager.getRetailer(id);
        if (retailer != null) {
            model.addAttribute("retailer", retailer);
        }
    }

    @RequestMapping(value = "admin/retailers/edit.do", method = RequestMethod.POST)
    public ModelAndView edit_submit(Model model, @ModelAttribute Retailer bean, BindingResult binding) {
        if (!binding.hasErrors()) {
            Retailer retailer = retailerManager.getRetailer(bean.getId());
            retailer.setEmailAddress(bean.getEmailAddress());
            retailer = retailerManager.updateRetailer(retailer);
            model.addAttribute("retailer", retailer);
            //changed the  returnType from String to ModelAndView
            //return "redirect:/admin/retailers/list.do";
            return new ModelAndView(new RedirectView("/admin/retailers/list.do", true));

        }
        //changed the  returnType from String to ModelAndView
        // return "admin/retailers/edit.do";
        String result = "admin/retailers/edit.do";
        return new ModelAndView().addObject(result);
    }


    @Autowired
    RetailerManager retailerManager;

    public RetailerManager getRetailerManager() {
        return retailerManager;
    }

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
