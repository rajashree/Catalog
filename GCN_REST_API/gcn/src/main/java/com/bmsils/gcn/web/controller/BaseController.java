/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.ConfigurationManager;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.utils.GCNUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/22/12
 * Time: 5:35 PM
 * Abstract BaseController class being extended by All Controllers
 */
public abstract class BaseController implements InitializingBean{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    //21232f297a57a5a743894a0e4a801fc3
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception ex, HttpServletResponse response){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        modelMap.put("status","failure");
        if(ex.getClass().equals(MissingServletRequestParameterException.class)){
            modelMap.put("message",getMessageText("application.error.missingRequestParameters",null));
            modelMap.put("error_code",HttpServletResponse.SC_BAD_REQUEST);
        }else{
            modelMap.put("message",ex.getMessage());
        }
        logger.error(ex.getMessage());
        return new ModelAndView("jsonView", modelMap);
    }

    public String getMessageText(String propertyKey, String[] params){
        return getI18nService().getMessage(propertyKey, params, Locale.getDefault());
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        User user = userManager.getUser(configurationManager.getProperty("admin.gcn","admin"));
        if(user == null){
            User newUser = new User();
            newUser.setUserGCN(configurationManager.getProperty("admin.gcn","admin"));
            newUser.setAdmin(true);
            newUser.setCreationDate(new Date());
            newUser.setLastUpdateDate(new Date());
            newUser.setUserName(configurationManager.getProperty("admin.username", "admin"));
            newUser.setLastName(configurationManager.getProperty("admin.lastname", "admin"));
            newUser.setFirstName(configurationManager.getProperty("admin.firstname", "admin"));
            newUser.setEmailId(configurationManager.getProperty("admin.email", "admin@bmsils.com"));
            newUser.setPassword(GCNUtils.md5Encoder.encodePassword(configurationManager.getProperty("admin.password", "admin"), GCNUtils.encoderSalt));
            newUser.setPhoneNumber(configurationManager.getLongProperty("admin.phonenumber",10101010L));
            userManager.addUser(newUser);
        }

    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private ConfigurationManager configurationManager;

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Autowired
    private ReloadableResourceBundleMessageSource i18nService;

    public ReloadableResourceBundleMessageSource getI18nService() {
        return i18nService;
    }

    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

}
