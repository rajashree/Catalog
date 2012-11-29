/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller.admin;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.managers.*;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.web.controller.BaseDellController;
import com.sourcen.core.config.ConfigurationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

import com.dell.acs.web.crumbs.AdminCrumb;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Samee K.S
 * @author : svnName $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

@Controller
public class LibraryController extends BaseDellController {

    @Autowired
    private DocumentManager documentManager;

    @Autowired
    private EventManager eventManager;

    @Autowired
    private CouponManager couponManager;

    @Autowired
    private CampaignManager campaignManager;

    @Autowired
    private ArticleManager articleManager;


    @RequestMapping(value = AdminCrumb.URL_LIBRARY_VIEW, method = RequestMethod.GET)
    public void libraryMainPage(HttpServletRequest request, Model model,
                                @RequestParam(value = "siteID", required = true) Long siteID) {

        RetailerSite retailerSite = retailerManager.getRetailerSite(siteID);
        Retailer retailer = retailerSite.getRetailer();

        model.addAttribute("retailerSite", retailerSite);
        model.addAttribute("documents", documentManager.getDocuments(siteID, EntityConstants.Entities.DOCUMENT.getId(), null));
        model.addAttribute("events", eventManager.getEventsByRetailerSiteId(siteID));
        model.addAttribute("campaigns", campaignManager.getCampaignsBySite(siteID));
        model.addAttribute("coupons", couponManager.getCouponsForRetailerSite(siteID));
        model.addAttribute("images", documentManager.getDocuments(siteID, EntityConstants.Entities.IMAGE.getId(), null));
        model.addAttribute("videos", documentManager.getDocuments(siteID, EntityConstants.Entities.VIDEO.getId(), null));
        model.addAttribute("links", documentManager.getDocuments(siteID, EntityConstants.Entities.LINK.getId(), null));
        model.addAttribute("articles", documentManager.getDocuments(siteID, EntityConstants.Entities.ARTICLE.getId(), null));


        model.addAttribute(AdminCrumb.MODEL_ATTRIBUTE_NAME,
                new AdminCrumb(request.getContextPath())
                        .retailerSite(retailer.getName(),
                                retailer.getId(),
                                retailerSite.getSiteName())
                        .render(AdminCrumb.TEXT_LIBRARY));
    }
}
