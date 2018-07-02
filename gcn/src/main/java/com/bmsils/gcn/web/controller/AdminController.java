/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.utils.ValidationUtils;
import com.bmsils.gcn.web.beans.BasicProfileDataBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/30/12
 * Time: 6:22 PM
 * AdminController contains REST API calls related to Admin functionality
 */
@Controller
@RequestMapping("/api/v1/rest")
public class AdminController extends BaseController{

    /**
     * Method used to add an Alias to the exisiting GCN
     * @param adminGCN
     * @param userGCN
     * @param alias
     * @return MV object with "addAlias" operation's success or failure response
     */
    @RequestMapping(value = "admin/addAlias", method = RequestMethod.POST)
    public ModelAndView addAlias(@RequestParam(required=true) String adminGCN,@RequestParam(required=true) String userGCN,@RequestParam(required=true) String alias) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(adminGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(userGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(adminGCN);
            if(user.isAdmin()){
                userManager.addAlias(userGCN,alias);
                modelMap.put("status", "success");
                modelMap.put("message",getMessageText("admin.action.addAlias.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("admin.action.addAlias.failure.accessDenied",null));
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("admin.action.addAlias.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to remove an existing alias associated with a GCN
     * @param adminGCN
     * @param userGCN
     * @return MV object with "removeAlias" operation's success or failure response
     */
    @RequestMapping(value = "admin/removeAlias", method = RequestMethod.POST)
    public ModelAndView removeAlias(@RequestParam(required=true) String adminGCN,@RequestParam(required=true) String userGCN) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(adminGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(userGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(adminGCN);
            if(user.isAdmin()){
                userManager.removeAlias(userGCN);
                modelMap.put("status", "success");
                modelMap.put("message",getMessageText("admin.action.removeAlias.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("admin.action.removeAlias.failure.accessDenied",null));
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("admin.action.removeAlias.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to list all the users along with aliases in the Admin console
     * @param adminGCN
     * @param start
     * @param limit
     * @return  MV object with list of BasicProfileDataBean objects
     */
    @RequestMapping(value = "admin/getUserAndAliases", method = RequestMethod.POST)
    public ModelAndView getUserAndAliases(@RequestParam(required=true) String adminGCN, @RequestParam(required = false, defaultValue = "0")int start, @RequestParam(required = false,defaultValue = "10")int limit) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(adminGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(adminGCN);
            if(user.isAdmin()){
                List<User> userList = userManager.getUserAndAliases(start,limit);
                List<BasicProfileDataBean> users = new ArrayList<BasicProfileDataBean>();
                for(User userObj : userList){
                    BasicProfileDataBean userBean = EntityToBeanMapper.getInstance().doMapping(userObj,BasicProfileDataBean.class);
                    users.add(userBean);
                }

                modelMap.put("status", "success");
                modelMap.put("data",users);
                modelMap.put("message",getMessageText("admin.action.getUserAndAliases.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("admin.action.getUserAndAliases.failure",null));
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("admin.action.addAlias.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Method used to mark a GCN as an admin
     * @param adminGCN
     * @param userGCN
     * @param isAdmin
     * @return MV object with "markAsAdmin" operation's success or failure response
     */
    @RequestMapping(value = "admin/markAsAdmin", method = RequestMethod.POST)
    public ModelAndView markAsAdmin(@RequestParam(required=true) String adminGCN,@RequestParam(required=true) String userGCN,@RequestParam(required=false, defaultValue = "false") boolean isAdmin) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(adminGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(userGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User adminUser = userManager.getUser(adminGCN);
            User user = userManager.getUser(userGCN);
            if(adminUser.isAdmin()){
                userManager.markAsAdmin(user,isAdmin);
                modelMap.put("status", "success");
                modelMap.put("message",getMessageText("admin.action.addAlias.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("admin.action.addAlias.failure.accessDenied",null));
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("admin.action.addAlias.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
