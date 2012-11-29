/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.web.ws.v1.RetailerService;
import com.dell.acs.web.ws.v1.beans.WSBeanUtil;
import com.dell.acs.web.ws.v1.beans.WSRetailer;
import com.dell.acs.web.ws.v1.beans.WSRetailerSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;
import java.util.Collection;
import java.util.List;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: navinr $
 * @version $Revision: 2705 $, $Date:: 2012-05-29 10:26:39#$
 */
@WebService
@RequestMapping("/api/v1/rest/RetailerService")
public class RetailerServiceImpl extends WebServiceImpl implements RetailerService {


    @Override
    @RequestMapping("getRetailer")
    public WSRetailer getRetailer(@RequestParam(required = true) final Long retailerId) {
        Retailer retailer = retailerManager.getActiveRetailer(retailerId);
        return WSBeanUtil.convert(retailer, new WSRetailer());
    }

    @Override
    @RequestMapping("getRetailers")
    public Collection<WSRetailer> getRetailers() {
        List<Retailer> retailers = retailerManager.getActiveRetailers();
        return WSBeanUtil.convert(retailers, WSRetailer.class);
    }

    @Override
    @RequestMapping("getRetailerSite")
    public WSRetailerSite getRetailerSite(@RequestParam(required = true) final Long retailerSiteId) {
        return WSBeanUtil.convert(retailerManager.getActiveRetailerSite(retailerSiteId), new WSRetailerSite());
    }

    @Override
    @RequestMapping("getRetailerSites")
    public Collection<WSRetailerSite> getRetailerSites(@RequestParam(required = true) Long retailerId) {
        return WSBeanUtil.convert(retailerManager.getActiveRetailerSites(retailerId), WSRetailerSite.class);
    }

    //
    // IoC
    //
    @Autowired
    private RetailerManager retailerManager;

    public void setRetailerManager(final RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }
}
