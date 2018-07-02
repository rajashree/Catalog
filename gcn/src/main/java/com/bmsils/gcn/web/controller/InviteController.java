/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.managers.InviteManager;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.utils.GCNUtils;
import com.bmsils.gcn.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/10/12
 * Time: 6:34 PM
 * InviteController contains REST API calls related to Invites
 */
@Controller
@RequestMapping("/api/v1/rest")
public class InviteController extends BaseController{

    /**
     * Method used to send an invite to a particular GCN along with the profile details that a user would like to share
     * @param gcn
     * @param contactGCN
     * @param visibleProfileFields
     * @param request
     * @return MV object with "inviteContact" operation's success or failure response
     */
    @RequestMapping(value = "inviteContact", method = RequestMethod.POST)
    public ModelAndView inviteContact(@RequestParam(required=true) String gcn,
                                      @RequestParam(required=true) String contactGCN,
                                      @RequestParam(required=false) String visibleProfileFields,
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
            User inviter = userManager.getUser(gcn);
            User invitee = userManager.getUser(contactGCN);

            inviteManager.inviteContact(inviter, invitee, GCNUtils.isNullOrEmpty(visibleProfileFields)?new ArrayList<String> ():Arrays.asList(visibleProfileFields.split(",")));
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("invite.action.inviteContact.success",null));
        }catch(DataIntegrityViolationException de){
            logger.error(de.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("invite.action.inviteContact.failure.duplicateKey",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("invite.action.inviteContact.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to confirm the Invite request.
     * It also, sends out the profile details that the confirmer would like to share with the inviter
     * @param gcn
     * @param contactGCN
     * @param visibleProfileFields
     * @param confirm
     * @param request
     * @return MV object with "confirmInviteRequest" operation's success or failure response
     */
    @RequestMapping(value = "confirmInviteRequest", method = RequestMethod.POST)
    public ModelAndView confirmInviteRequest(@RequestParam(required=true) String gcn,
                                      @RequestParam(required=true) String contactGCN,
                                      @RequestParam(required=false) String visibleProfileFields,
                                      @RequestParam(required=true) String confirm,
                                      HttpServletRequest request) {
    	
        Map<String, Object> modelMap = new HashMap<String, Object>();
        
        try{
            int subscriptionStatus = inviteManager.confirmInviteRequest(contactGCN,gcn,GCNUtils.isNullOrEmpty(visibleProfileFields)?new ArrayList<String> ():Arrays.asList(visibleProfileFields.split(",")),Boolean.valueOf(confirm));
           
            modelMap.put("status", "success");
            if(subscriptionStatus == ApplicationConstants.SubscriptionStatus.INVITE_DECLINED.getValue()){
                modelMap.put("message",getMessageText("invite.action.confirmInviteRequest.success.inviteRejected",null));
            }else if(subscriptionStatus == ApplicationConstants.SubscriptionStatus.INVITE_ACCEPTED.getValue()){
                modelMap.put("message",getMessageText("invite.action.confirmInviteRequest.success.inviteAccepted",null));
            }
        }catch(Exception e){
            logger.error(e.getMessage());
           e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("invite.action.confirmInviteRequest.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to establish the Contact between two GCNs via the QR code workflow
     * @param gcn
     * @param contactGCN
     * @param gcnVisibleProfileFields
     * @param contactGCNVisibleProfileFields
     * @param fromQR
     * @return MV object with "handleQRInvite" operation's success or failure response
     */
    @RequestMapping(value = "handleQRInvite", method = RequestMethod.POST)
    public ModelAndView handleQRInvite(@RequestParam(required = true) String gcn,
                                 @RequestParam(required = true) String contactGCN,
                                 @RequestParam(required=false) String gcnVisibleProfileFields,
                                 @RequestParam(required=false) String contactGCNVisibleProfileFields,
                                 @RequestParam(required = true) String fromQR
                                 ) {
        Map<String, Object> modelMap = new HashMap<String, Object>();

        try{
            inviteManager.handleQRInvite(gcn,contactGCN,GCNUtils.isNullOrEmpty(gcnVisibleProfileFields)?new ArrayList<String> ():Arrays.asList(gcnVisibleProfileFields.split(",")),GCNUtils.isNullOrEmpty(contactGCNVisibleProfileFields)?new ArrayList<String> ():Arrays.asList(contactGCNVisibleProfileFields.split(",")),Boolean.valueOf(fromQR));

            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("invite.action.handleQRInvite.success",null));

        }catch(DataIntegrityViolationException de){
            logger.error(de.getMessage());
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("invite.action.handleQRInvite.failure.duplicateKey",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("invite.action.handleQRInvite.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to block a contact - who are part of the contact list
     * @param gcn
     * @param contactGCN
     * @return  MV object with "removeAlias" operation's success or failure response
     */
    @RequestMapping("blockContact")
    public ModelAndView blockContact(@RequestParam(required=true) String gcn, @RequestParam(required=true) String contactGCN  ) {
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
            inviteManager.blockContact(gcn,contactGCN);
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("invite.action.blockContact.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("invite.action.blockContact.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to unblock a contact - who are part of the contact list
     * @param gcn
     * @param contactGCN
     * @return MV object with "unBlockContact" operation's success or failure response
     */
    @RequestMapping("unBlockContact")
    public ModelAndView unBlockContact(@RequestParam(required=true) String gcn, @RequestParam(required=true) String contactGCN  ) {
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
            inviteManager.unBlockContact(gcn,contactGCN);
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("invite.action.unBlockContact.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("invite.action.unBlockContact.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private InviteManager inviteManager;

    public void setInviteManager(InviteManager inviteManager) {
        this.inviteManager = inviteManager;
    }

    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
