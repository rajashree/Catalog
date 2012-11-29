/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.web.controller;

import javax.servlet.http.HttpServletResponse;

import com.dell.acs.UserNotFoundException;
import com.dell.acs.managers.DataFilesDownloadManager;
import com.dell.acs.managers.DataImportManager;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.managers.UserManager;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.spring.security.UserDetailsImpl;
import com.sourcen.core.web.controller.BaseController;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 2686 $, $Date:: 2012-05-29 07:33:48#$
 */
public abstract class BaseDellController extends BaseController {

    @Autowired
    protected RetailerManager retailerManager;

    @Autowired
    protected DataImportManager dataImportManager;

    @Autowired
    protected DataFilesDownloadManager dataFilesDownloadManager;

    @Autowired
    protected UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setDataImportManager(DataImportManager dataImportManager) {
        this.dataImportManager = dataImportManager;
    }

    public void setDataFilesDownloadManager(final DataFilesDownloadManager dataFilesDownloadManager) {
        this.dataFilesDownloadManager = dataFilesDownloadManager;
    }

    public void setRetailerManager(RetailerManager retailerManager) {
        this.retailerManager = retailerManager;
    }

    protected User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        try {
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetailsImpl) {
                    user = ((UserDetailsImpl) principal).getUser();
                } else if (principal instanceof User) {
                    //We can get the currently authenticated principal i.e. *User* object from SecurityContext.
                    user = (User) authentication.getPrincipal();
                }
                if (user == null) {
                    user = userManager.getUser("anonymous");
                }
                logger.debug(" UserName " + user.getUsername());

            }
        } catch (UserNotFoundException ex) {

        }
        return user;
    }

    @ExceptionHandler(Exception.class)
    public
    @ResponseBody
    String handleException(Exception e, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        if (e instanceof TypeMismatchException) {
            return "Incompatible types ";
        }
        return e.getMessage();
    }
}
