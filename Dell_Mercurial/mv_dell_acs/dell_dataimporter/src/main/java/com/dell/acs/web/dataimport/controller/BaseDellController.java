/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.dataimport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.managers.PreValidatedDataImportManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.web.controller.BaseController;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 2686 $, $Date:: 2012-05-29 07:33:48#$
 */
public abstract class BaseDellController extends BaseController {

    @Autowired
    protected RetailerManager retailerManager;

    @Autowired
    protected PreValidatedDataImportManager dataImportManager;

    @Autowired
    protected UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setDataImportManager(PreValidatedDataImportManager dataImportManager) {
        this.dataImportManager = dataImportManager;
    }

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    protected User getUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userManager.getUser(userDetails.getUsername());
        }
        return userManager.getUser("anonymous");
    }

}
