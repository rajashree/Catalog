/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.web.controller.BaseDellController;
import com.dell.acs.web.controller.formbeans.FormBeanConverter;
import com.dell.acs.web.controller.formbeans.RetailerBean;
import com.dell.acs.web.crumbs.AdminCrumb;
import com.sourcen.core.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

/**
 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: ashish $
 @version $Revision: 3478 $, $Date:: 2012-06-21 11:38:32#$ */

@Controller
public final class RetailerController extends BaseDellController {

    @RequestMapping(value = AdminCrumb.URL_RETAILER_LIST, method = RequestMethod.GET)
    public void get(HttpServletRequest request, Model model) {
    	Collection<Retailer> retailers = retailerManager.getAllRetailers(),
    						 activeRetailers = new ArrayList<Retailer>(),
    						 inactiveRetailers = new ArrayList<Retailer>();
    	
    	// sort retailers by active
    	for (Retailer retailer : retailers) {
    		if (retailer.getActive() == true)
    			activeRetailers.add(retailer);
    		else
    			inactiveRetailers.add(retailer);
    	}
    	
    	model.addAttribute("retailers", retailers);
    	model.addAttribute("activeRetailers", activeRetailers);
    	model.addAttribute("inactiveRetailers", inactiveRetailers);
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
    			new AdminCrumb(request.getContextPath())
    			.home()
    			.render(AdminCrumb.TEXT_RETAILER));
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILER_EDIT, method = RequestMethod.GET)
    public void edit(@RequestParam Long id, HttpServletRequest request, Model model) {
        logger.info("load id :=" + id);

        Retailer retailer = retailerManager.getRetailer(id);
        if (retailer != null) {
            model.addAttribute("retailer", retailer);
        	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
        			new AdminCrumb(request.getContextPath())
        			.retailer(retailer.getName())
        			.render(AdminCrumb.TEXT_EDIT));
        }
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILER_EDIT, method = RequestMethod.POST)
    public ModelAndView edit_submit(HttpServletRequest request, Model model, 
    								@ModelAttribute Retailer bean, BindingResult binding) {
 
    	Retailer retailer = retailerManager.getRetailer(bean.getId());
        
        if (!binding.hasErrors()) {
            retailer.setName(bean.getName());
            retailer.setDescription(bean.getDescription());
            retailer.setActive(bean.getActive());
            retailer = retailerManager.updateRetailer(retailer);
            model.addAttribute("retailer", retailer);
            
            String redirectUrl = new AdminCrumb(request.getContextPath())
							     .toAbsolute(AdminCrumb.URL_RETAILER_LIST);
            return new ModelAndView(new RedirectView(redirectUrl));
        }
        
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
    			new AdminCrumb(request.getContextPath())
    			.retailer(retailer.getName())
    			.render(AdminCrumb.TEXT_EDIT));
    	String viewName = new AdminCrumb(request.getContextPath())
        				  .toView(AdminCrumb.URL_RETAILER_EDIT);
        return new ModelAndView(viewName);
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILER_ADD, method = RequestMethod.GET)
    public void add(HttpServletRequest request, Model model) {
    	Retailer retailer = new Retailer();
    	retailer.setActive(true);
        model.addAttribute("retailer", retailer);
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
    			new AdminCrumb(request.getContextPath())
    						   .retailer()
    						   .render(AdminCrumb.TEXT_ADD));
    }

    @RequestMapping(value = AdminCrumb.URL_RETAILER_ADD, method = RequestMethod.POST)
    public ModelAndView add_submit(HttpServletRequest request, Model model, 
    							   @ModelAttribute(value = "retailer") @Valid RetailerBean retailerBean, 
    							   BindingResult binding) {
        if (!StringUtils.isEmpty(retailerBean.getDescription())) {
            if (((retailerBean.getDescription()).trim().length() == 0)) {
                retailerBean.setDescription((retailerBean.getDescription()).trim());
                binding.addError(new FieldError("retailer", "description", null, true,
                        new String[]{"NotEmpty.retailer.description"}, null, "Please provide the valid input."));
            }
        }
        if (!StringUtils.isEmpty(retailerBean.getName())) {
            if (((retailerBean.getName()).trim().length() == 0)) {
                retailerBean.setName((retailerBean.getName()).trim());
                binding.addError(new FieldError("retailer", "name", null, true,
                        new String[]{"NotEmpty.retailer.name"}, null, "Please provide the valid input."));
            }
        }

        if (!binding.hasErrors()) {
            Retailer retailer = new Retailer();
            retailer = FormBeanConverter.convert(retailerBean, retailer);
            retailer = retailerManager.createRetailer(retailer);
            model.addAttribute("retailer", retailer);

            String redirectUrl = new AdminCrumb(request.getContextPath())
								 .toAbsolute(AdminCrumb.URL_RETAILER_LIST);
            return new ModelAndView(new RedirectView(redirectUrl));
        } 

        model.addAttribute("retailer", retailerBean);
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
    			new AdminCrumb(request.getContextPath())
    						   .retailer()
    						   .render(AdminCrumb.TEXT_ADD));        
        String viewName = new AdminCrumb(request.getContextPath())
						  .toView(AdminCrumb.URL_RETAILER_ADD);
        return new ModelAndView(viewName);
   }
}
