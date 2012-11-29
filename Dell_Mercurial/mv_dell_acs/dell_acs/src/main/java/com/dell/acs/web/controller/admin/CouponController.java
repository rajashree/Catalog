/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.CouponAlreadyExistsException;
import com.dell.acs.managers.CouponManager;
import com.dell.acs.persistence.domain.Coupon;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.dell.acs.web.crumbs.AdminCrumb;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("all")
@Controller
public class CouponController extends BaseDellController {

	@Autowired
    private CouponManager couponManager;
	
	private void addCrumbs(HttpServletRequest request, 
						   Model model, 
						   String crumbText,
						   RetailerSite retailerSite) {

		Retailer retailer = retailerSite.getRetailer();
    	model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME, 
	   		   	   new AdminCrumb(request.getContextPath())
	   	   					      .libraryCoupon(retailer.getName(),
	   	   					    		  					 retailer.getId(),
	   	   					    		  			         retailerSite.getSiteName(),retailerSite.getId())
	      						  .render(crumbText));  		
	}

    @RequestMapping(value = AdminCrumb.URL_COUPON_ADD, method = RequestMethod.GET)
    public void addCoupon(HttpServletRequest request, Model model, 
    				      @RequestParam(value = "siteID", required = true) Long siteID) {

        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        Coupon coupon = new Coupon();
        
        coupon.setRetailerSite(retailerSite);
        model.addAttribute("coupon", coupon);
        addCrumbs(request, model, AdminCrumb.TEXT_COUPON_ADD, retailerSite);
    }

    /**
     * Method to save Coupon.
     *
     * @param model      - Coupon model attribute
     * @param campaign   - Model object
     * @param binding    - Webbinding object
     * @param jsonString - selected coupon json string
     * @return - Redirect URL
     */
    // MWE -- this stuff was broken before I touched it; doesn't return error messages in UI for invalid data
    @RequestMapping(value = AdminCrumb.URL_COUPON_ADD, method = RequestMethod.POST)
    public ModelAndView saveCampaign(HttpServletRequest request, Model model,
                                     @ModelAttribute Coupon coupon, BindingResult binding,
                                     @RequestParam(value = "thumbnailFile", required = false) MultipartFile file) {
    	try {
            if (!binding.hasErrors()) {
                //TODO: Have a thumbnail image once Greg confirms the with Steve
                coupon = couponManager.saveCoupon(coupon);
                model.addAttribute("coupon", coupon);
                logger.info("Successfully created coupon - " + coupon.getCouponCode());
                
                String redirectUrl = 
                    	new AdminCrumb(request.getContextPath())
        		       	.toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_COUPON_EDIT, coupon.getId()));
                return new ModelAndView(new RedirectView(redirectUrl));                  
	        } else {
	            model.addAttribute("show", new Boolean(true));
	        }            
        } catch (CouponAlreadyExistsException caEx) {
            //TODO: Need to check with Navin, as to how do we handle business exceptions and show meaningful error messages
            // If any exception while creating a campaign - redirect to same page
            // ERROR: Add error message
            // add.do?siteID=
            model.addAttribute("couponCode", "Coupon code already exists. Please provide a different code."); 
        }

        
    	addCrumbs(request, model, AdminCrumb.TEXT_COUPON_ADD, 
    			  retailerManager.getRetailerSite(coupon.getRetailerSite().getId()));

        String viewName = new AdminCrumb(request.getContextPath())
		  				  .toView(AdminCrumb.URL_COUPON_ADD);
        return new ModelAndView(viewName);    	
    }

    @RequestMapping(value = AdminCrumb.URL_COUPON_EDIT, method = RequestMethod.GET)
    public void edit(@RequestParam(value = "id", required = true) Long id, 
    				 HttpServletRequest request, Model model) {

        Coupon coupon = couponManager.getCoupon(id);
        RetailerSite retailerSite = coupon.getRetailerSite();
        
        logger.info("load id :=" + id);
        
        model.addAttribute("coupon", coupon);
        addCrumbs(request, model, AdminCrumb.TEXT_COUPON_EDIT, retailerSite);
    }

    @RequestMapping(value = AdminCrumb.URL_COUPON_EDIT, method = RequestMethod.POST)
    public ModelAndView edit_submit(HttpServletRequest request, Model model, 
    								@ModelAttribute Coupon bean, BindingResult binding) {
        logger.info("Updating the following coupon id :=" + bean.getId());
        
        Coupon coupon = couponManager.getCoupon(bean.getId());
        if (!binding.hasErrors()) {
            coupon.setTitle(bean.getTitle());
            coupon.setDescription(bean.getDescription());
            coupon.setCouponCode(bean.getCouponCode());
            coupon.setStartDate(bean.getStartDate());
            coupon.setEndDate(bean.getEndDate());

            try {
                coupon = this.couponManager.updateCoupon(coupon);
                model.addAttribute("coupon", coupon);
                
                String redirectUrl = new AdminCrumb(request.getContextPath())
    		    		.toAbsolute(AdminCrumb.linkById(AdminCrumb.URL_COUPON_EDIT, bean.getId()));
                return new ModelAndView(new RedirectView(redirectUrl));             
            } catch (CouponAlreadyExistsException cex) {
                model.addAttribute("couponCode", "Coupon code already exists. Please provide a different code.");
            }

        }
        
        addCrumbs(request, model, AdminCrumb.TEXT_COUPON_EDIT, coupon.getRetailerSite());
        String viewName = new AdminCrumb(request.getContextPath())
						  .toView(AdminCrumb.URL_COUPON_EDIT);
        return new ModelAndView(viewName);       
    }
    

	// MWE -- did not retrofit for new AdminCrumb
    @RequestMapping(value = "admin/coupons/delete.do", method = RequestMethod.GET)
    public ModelAndView deleteCampaign(@RequestParam final Long id, final Model model) {
        logger.info("load id :=" + id);
        String redirectUrl = "list.do";
        try {
            this.couponManager.deleteCoupon(id);
        } catch (Exception e) {

        }
        return new ModelAndView(new RedirectView(redirectUrl));
    }    
}
