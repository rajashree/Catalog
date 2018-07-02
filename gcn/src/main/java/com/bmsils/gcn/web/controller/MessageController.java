/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.managers.MessageManager;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.Message;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.utils.GCNUtils;
import com.bmsils.gcn.utils.ValidationUtils;
import com.bmsils.gcn.web.beans.MessageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/12/12
 * Time: 7:10 PM
 * MessageController contains REST API calls related to Messages
 */
@Controller
@RequestMapping("/api/v1/rest")
public class MessageController extends BaseController{

    /**
     * Method used to send message
     * @param gcn
     * @param contactGCN
     * @param message
     * @param request
     * @return MV object with "sendMessage" operation's success or failure response
     */
    @RequestMapping(value = "sendMessage", method = RequestMethod.POST)
    public ModelAndView sendMessage(@RequestParam(required=true) String gcn,
                                      @RequestParam(required=true) String contactGCN,
                                      @RequestParam(required=true) String message,
                                      HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(contactGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }

        try{
            User sender = userManager.getUser(gcn);
            User receiver = userManager.getUser(contactGCN);
            messageManager.sendMessage(sender, receiver, message);

            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("message.action.sendMessage.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("message.action.sendMessage.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to poll the message history
     * @param gcn
     * @param contactGCN
     * @param duration
     * @return MV object with list of Message objects
     */
    @RequestMapping("pollMessageHistory")
    public ModelAndView pollMessageHistory(@RequestParam(required=true) String gcn, @RequestParam(required=true) String contactGCN, @RequestParam(required=false) String duration ) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateGCN(contactGCN)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }

        try{
            List<MessageBean> messages;
            SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            Calendar cal = Calendar.getInstance();
            if(!GCNUtils.isNullOrEmpty(duration)){
                cal.add(Calendar.MINUTE, -Integer.parseInt(duration));
                messages = messageManager.getMessageHistory(gcn, contactGCN, cal.getTime());
            }else{
                messages = messageManager.getMessageHistory(gcn, contactGCN, null);
            }

            modelMap.put("status", "success");
            modelMap.put("data",messages);
            modelMap.put("message", getMessageText("message.action.pollMessageHistory.success",null));

        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("message.action.pollMessageHistory.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    MessageManager messageManager;

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    
    @Autowired
    UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
