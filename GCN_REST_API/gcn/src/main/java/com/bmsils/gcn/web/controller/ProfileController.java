/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.web.controller;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.managers.*;
import com.bmsils.gcn.persistence.domain.*;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.utils.GCNUtils;
import com.bmsils.gcn.utils.ValidationUtils;
import com.bmsils.gcn.web.beans.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/3/12
 * Time: 4:44 PM
 * ProfileController contains REST API calls related to User
 */
@Controller
@RequestMapping("/api/v1/rest")
public class ProfileController extends BaseController{

    /**
     * Method used to register GCN
     * @param gcn
     * @param password
     * @param uuid
     * @param phoneNumber
     * @param deviceType
     * @param email
     * @return MV object with "registerGCN" operation's success or failure response
     */
    @RequestMapping(value = "registerGCN", method = RequestMethod.POST)
    public ModelAndView registerGCN(@RequestParam(required=true) String gcn,@RequestParam(required=true) String password,@RequestParam(required=false) String uuid,@RequestParam(required=false)Long phoneNumber, @RequestParam(required=true)String deviceType, @RequestParam(required=false)String email) {
      
    	Map<String, Object> modelMap = new HashMap<String, Object>();

        if(ApplicationConstants.DeviceType.isValidDevice(deviceType)){
            if(GCNUtils.isNullOrEmpty(uuid) || GCNUtils.isNullOrEmpty(phoneNumber)){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("application.error.missingRequestParameters",null));
                modelMap.put("error_code",HttpServletResponse.SC_BAD_REQUEST);
                return new ModelAndView("jsonView", modelMap);
            }
            if(!ValidationUtils.isValidateUUID(uuid)){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }

            if(deviceManager.getTotalGCNCount(uuid) > 5){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("rule.validation.multipleRegFromDevice.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }
        }else{
            if(!ValidationUtils.isValidateEmailAddress(email)){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("field.validation.email.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }
            if(userManager.getTotalGCNCount(email) > 2){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("rule.validation.multipleRegFromEmail.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }

        }
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }

        try{
            userManager.registerGCN(gcn,password,uuid,deviceType, phoneNumber,email);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("profile.action.registerGCN.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.registerGCN.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to login
     * @param gcn
     * @param password
     * @param uuid
     * @param deviceType
     * @return MV object with "login" operation's success or failure response
     */
    @RequestMapping("login")
    public ModelAndView login(@RequestParam(required=true) String gcn,  @RequestParam(required=true) String password, @RequestParam(required=false) String uuid, @RequestParam(required=true) String deviceType  ) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        
        if(ApplicationConstants.DeviceType.isValidDevice(deviceType)){
            if(GCNUtils.isNullOrEmpty(uuid)){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("application.error.missingRequestParameters",null));
                modelMap.put("error_code",HttpServletResponse.SC_BAD_REQUEST);
                return new ModelAndView("jsonView", modelMap);
            }
            if(!ValidationUtils.isValidateUUID(uuid)){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }
            if(deviceManager.getTotalDeviceCount(gcn) > 5){
                modelMap.put("status","failure");
                modelMap.put("message",getMessageText("rule.validation.devicePerGCN.failure",null));
                return new ModelAndView("jsonView", modelMap);
            }
        }
        if(!(ValidationUtils.isValidateGCN(gcn)||userManager.checkUserFromAlias(gcn, password))){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            HashMap<String, Object> response = userManager.login(gcn, password, uuid, deviceType);
            ApplicationConstants.AuthenticationResultType result = (ApplicationConstants.AuthenticationResultType) response.get("resultType");
            if((response.get("isAdmin")!=null)&&(Boolean)response.get("isAdmin")){
                modelMap.put("isAdmin",true);
            }else{
                modelMap.put("isAdmin",false);
            }
            if(ApplicationConstants.DeviceType.isValidDevice(deviceType)){
                Device device = deviceManager.getDeviceDetailsById(uuid,gcn);
                if(device != null){
                    if(device.isBlockGcn()){
                        modelMap.put("isBlockGCN",true);
                    }
                }
            }
            switch(result){
                case USER_NOT_FOUND:
                    modelMap.put("status", "failure");
                    modelMap.put("message", getMessageText("profile.action.login.failure.userNotFound",null));
                    break;
                case PASSWORD_INCORRECT:
                    modelMap.put("status", "failure");
                    modelMap.put("message", getMessageText("profile.action.login.failure.pwdIncorrect",null));
                    break;
                case MULTIPLE_DEVICE_LOGIN:
                    modelMap.put("status", "success");
                    Map dataMap =   new HashMap();
                    dataMap.put("multipleDeviceLogin",true);
                    modelMap.put("data",dataMap);
                    modelMap.put("message", getMessageText("profile.action.login.success",null));
                    break;
                case LOGIN_SUCCESSFUL:
                    modelMap.put("status", "success");
                    modelMap.put("message", getMessageText("profile.action.login.success",null));
                    break;

            }
        }catch(Exception e){
        	e.printStackTrace();
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.login.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain the profile details
     * @param gcn
     * @return  MV object of User
     */
    @RequestMapping("getProfileDetails")
    public ModelAndView getProfileDetails(@RequestParam(required=true) String gcn) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(gcn);
            if(user != null){
                // Image image = imageManager.getDefaultImage(user.getUserGCN());
                ProfileDataBean profileDataBean = EntityToBeanMapper.getInstance().doMapping(user, ProfileDataBean.class);
                //  profileDataBean.setAvatar(image.getImage());
                modelMap.put("status", "success");
                modelMap.put("data",profileDataBean);
                modelMap.put("message", getMessageText("profile.action.getProfileDetails.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("profile.action.getProfileDetails.failure",null));
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.getProfileDetails.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to update the profile details
     * @return MV object with "updateProfileDetails" operation's success or failure response
     */
    @RequestMapping("updateProfileDetails")
    public ModelAndView updateProfileDetails(HttpServletResponse response, HttpServletRequest request,
                                             @RequestParam(required = true) String gcn,
                                             @RequestParam(required = false) String firstName,
                                             @RequestParam(required = false) String lastName,
                                             @RequestParam(required = false) String userName,
                                             @RequestParam(required = false) String emailId,
                                             @RequestParam(required = false) String facebookId,
                                             @RequestParam(required = false) String linkedinId,
                                             @RequestParam(required = false) String designation,
                                             @RequestParam(required = false) String twitterId,
                                             @RequestParam(required = false) String officeEmailId,
                                             @RequestParam(required = false) String officeName,
                                             @RequestParam(required = false) String officePhoneNumber,
                                             @RequestParam(required = false) String phoneNumber,
                                             @RequestParam(required = false) String office_Phone_Number2,
                                             @RequestParam(required = false) String phone_Number2,
                                             @RequestParam(required = false) String residentialAddAddressLine1,
                                             @RequestParam(required = false) String residentialAddAddressLine2,
                                             @RequestParam(required = false) String residentialAddCity,
                                             @RequestParam(required = false) String residentialAddState,
                                             @RequestParam(required = false) String residentialAddCountry,
                                             @RequestParam(required = false) String residentialAddPostalCode,
                                             @RequestParam(required = false) String officeAddAddressLine1,
                                             @RequestParam(required = false) String officeAddAddressLine2,
                                             @RequestParam(required = false) String officeAddCity,
                                             @RequestParam(required = false) String officeAddState,
                                             @RequestParam(required = false) String officeAddCountry,
                                             @RequestParam(required = false) String officeAddPostalCode,
                                             @RequestParam(required = false) String profileStatus,
                                             @RequestParam(required = false) String profileFieldsUpdated,
                                             @RequestParam(required = false) MultipartFile avatar,
                                             @RequestParam(required = false) String imageId
    ) {
    	
    	/*
    	 * USER JAYANTA
    	 * DATE:-26-06-12
    	 * I have added office_Phone_Number2,phone_Number2 in the request parameter above*/
        Map<String, Object> modelMap = new HashMap<String, Object>();
        try{
            User user = userManager.getUser(gcn);
            
            if(user != null){
                user.setLastUpdateDate(new Date());
                if(emailId != null)
                    user.setEmailId(emailId);
               
               
                if(designation != null){
                    user.setDesignation(designation);
                   
                }
                if(twitterId != null){
                    user.setTwitterId(twitterId);
                }
                if(facebookId != null)
                    user.setFacebookId(facebookId);
                if(linkedinId != null)
                    user.setLinkedinId(linkedinId);
                if(firstName != null)
                    user.setFirstName(firstName);
                if(lastName != null)
                    user.setLastName(lastName);
                if(userName != null)
                    user.setUserName(userName);
                if(officeEmailId != null)
                    user.setOfficeEmailId(officeEmailId);
                if(officeName != null)
                    user.setOfficeName(officeName);
                if(officePhoneNumber != null){
                    user.setOfficePhoneNumber(Long.parseLong(officePhoneNumber));
                   
                }
                if(office_Phone_Number2 != null)
                    user.setOfficePhoneNumber2(Long.parseLong(office_Phone_Number2));
                if(phoneNumber != null){
                    user.setPhoneNumber(Long.parseLong(phoneNumber));
                }
                if(phone_Number2 != null){
                    user.setPhoneNumber2(Long.parseLong(phone_Number2));
                    System.out.println("phone_Number2 "+phone_Number2);
                }

                if(profileStatus != null)
                    user.setProfileStatus(profileStatus);

                if(user.getResidentialAddress() != null && (user.getResidentialAddress()).getId() != null){
                    Address resAddress = user.getResidentialAddress();
                    if(!GCNUtils.isNullOrEmpty(residentialAddAddressLine1))
                        resAddress.setAddressLine1(residentialAddAddressLine1);
                    if(!GCNUtils.isNullOrEmpty(residentialAddAddressLine2))
                        resAddress.setAddressLine2(residentialAddAddressLine2);
                    if(!GCNUtils.isNullOrEmpty(residentialAddCity))
                        resAddress.setCity(residentialAddCity);
                    if(!GCNUtils.isNullOrEmpty(residentialAddState))
                        resAddress.setState(residentialAddState);
                    if(!GCNUtils.isNullOrEmpty(residentialAddCountry))
                        resAddress.setCountry(residentialAddCountry);
                    if(!GCNUtils.isNullOrEmpty(residentialAddPostalCode))
                        resAddress.setPostalCode(residentialAddPostalCode);
                    addressManager.updateAddress(resAddress);
                    user.setResidentialAddress(resAddress);
                }else{
                    Address address = new Address();
                    address.setAddressLine1(residentialAddAddressLine1);
                    address.setAddressLine2(residentialAddAddressLine2);
                    address.setCity(residentialAddCity);
                    address.setState(residentialAddState);
                    address.setCountry(residentialAddCountry);
                    address.setPostalCode(residentialAddPostalCode);
                    if((residentialAddAddressLine1 !=null) || (residentialAddAddressLine2 !=null)|| (residentialAddCity !=null)|| (residentialAddState !=null)|| (residentialAddCountry !=null)|| (residentialAddPostalCode !=null) ){
                        address.setCreationDate(new Date());
                        addressManager.createAddress(address);
                        user.setResidentialAddress(address);
                    }
                }

                if(user.getOfficeAddress() != null && (user.getOfficeAddress()).getId() != null){
                    Address officeAddress = user.getOfficeAddress();
                    if(!GCNUtils.isNullOrEmpty(officeAddAddressLine1))
                        officeAddress.setAddressLine1(officeAddAddressLine1);
                    if(!GCNUtils.isNullOrEmpty(officeAddAddressLine2))
                        officeAddress.setAddressLine2(officeAddAddressLine2);
                    if(!GCNUtils.isNullOrEmpty(officeAddCity))
                        officeAddress.setCity(officeAddCity);
                    if(!GCNUtils.isNullOrEmpty(officeAddState))
                        officeAddress.setState(officeAddState);
                    if(!GCNUtils.isNullOrEmpty(officeAddCountry))
                        officeAddress.setCountry(officeAddCountry);
                    if(!GCNUtils.isNullOrEmpty(officeAddPostalCode))
                        officeAddress.setPostalCode(officeAddPostalCode);
                    addressManager.updateAddress(officeAddress);
                    user.setOfficeAddress(officeAddress);
                }else{
                    Address address = new Address();
                    address.setAddressLine1(officeAddAddressLine1);
                    address.setAddressLine2(officeAddAddressLine2);
                    address.setCity(officeAddCity);
                    address.setState(officeAddState);
                    address.setCountry(officeAddCountry);
                    address.setPostalCode(officeAddPostalCode);
                    if((officeAddAddressLine1 !=null) || (officeAddAddressLine2 !=null)|| (officeAddCity !=null)|| (officeAddState !=null)|| (officeAddCountry !=null)|| (officeAddPostalCode !=null) ){
                        address.setCreationDate(new Date());
                        addressManager.createAddress(address);
                        user.setOfficeAddress(address);
                    }

                }
                Long defaultImageId = null;
                if(imageId != null){
                    Image image = imageManager.getImage(Long.parseLong(imageId));
                    image.setDefault(true);
                    if( ArrayUtils.isNotEmpty(image.getImage()))
                        defaultImageId = imageManager.updateImage(image);
                }else if(avatar != null){
                    Image image = new Image(user,true,avatar.getBytes());
                    defaultImageId = imageManager.addImage(image);

                }
                if(defaultImageId != null)
                    imageManager.setDefaultImage(user.getUserGCN(),defaultImageId);
                userManager.updateUser(user);
                userManager.updateProfileFieldUpdates(user.getUserGCN(),profileFieldsUpdated);
                modelMap.put("status", "success");
                modelMap.put("message", getMessageText("profile.action.updateProfileDetails.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("profile.action.updateProfileDetails.failure",null));
            }
        }catch(DataIntegrityViolationException de){
            if(de.getMessage().contains("emailId")){
                modelMap.put("message",getMessageText("profile.action.updateProfileDetails.failure.emailAlreadyExists",null));
            }else{
                modelMap.put("message",getMessageText("profile.action.updateProfileDetails.failure",null));
            }
            logger.error(de.getMessage());
            modelMap.put("status", "failure");

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.updateProfileDetails.failure",null));
        }

        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain the settings related to the account
     * @param gcn
     * @param uuid
     * @return MV object with details related to settings
     */
    @RequestMapping("getSettings")
    public ModelAndView getSettings(@RequestParam(required=true) String gcn, @RequestParam(required = true) String uuid) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateUUID(uuid)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            Device device = userManager.getUserSettings(gcn,uuid);
            Map<String,String> dataMap = new HashMap<String, String>();
            dataMap.put("primaryDevice",String.valueOf(device.isPrimaryDevice()));
            modelMap.put("status", "success");
            modelMap.put("data",dataMap);
            modelMap.put("message", getMessageText("profile.action.getSettings.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.getSettings.failure",null));
        }

        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to updateSettings
     * @param gcn
     * @param uuid
     * @param isPrimaryDevice
     * @return MV object with "updateSettings" operation's success or failure response
     */
    @RequestMapping("updateSettings")
    public ModelAndView updateSettings(@RequestParam(required=true) String gcn, @RequestParam(required = true) String uuid, @RequestParam(required=false) String isPrimaryDevice) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(!ValidationUtils.isValidateUUID(uuid)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.uuid.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            userManager.updateUserSettings(gcn, uuid, Boolean.parseBoolean(isPrimaryDevice));
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("profile.action.updateSettings.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.updateSettings.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to updateProfileStatus
     * @param gcn
     * @param profileStatus
     * @return MV object with "updateProfileStatus" operation's success or failure response
     */
    @RequestMapping("updateProfileStatus")
    public ModelAndView updateProfileStatus(@RequestParam(required=true) String gcn, @RequestParam(required = true) String profileStatus) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }

        try{
            User user = userManager.getUser(gcn);
            
            if(user != null){
                userManager.updateProfileStatus(user,profileStatus);
                modelMap.put("status", "success");
                modelMap.put("message",getMessageText("profile.action.updateProfileStatus.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message",getMessageText("profile.action.updateProfileStatus.failure.userNotFound",null));
            }

        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.updateProfileStatus.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to change the password
     * @param gcn
     * @param password
     * @param isBlockGCN
     * @return MV object with "changePassword" operation's success or failure response
     */
    @RequestMapping("changePassword")
    public ModelAndView changePassword(@RequestParam(required=true) String gcn, @RequestParam(required = true) String password, @RequestParam(required=false) String isBlockGCN) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(gcn);
            if(user != null){
                String pwd = new String((new BASE64Decoder()).decodeBuffer(password));
                user.setPassword(GCNUtils.md5Encoder.encodePassword(pwd, GCNUtils.encoderSalt));
                user.setPasswordChangeFlag(true);
            }
            userManager.updateUser(user);
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("profile.action.changePassword.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.changePassword.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Poll Request methods - contains polling of messages, polling of invites, polling of active messages, polling of passwordUpdate status
     * @param gcn
     * @param pollCategory
     * @return response of poll request depends on the Poll Category
     */
    @RequestMapping("pollRequest")
    public ModelAndView pollRequest(@RequestParam(required=true) String gcn, @RequestParam(required = true) String pollCategory) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        User user = userManager.getUser(gcn);
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        if(user != null){

            switch(ApplicationConstants.PollCategory.getByValue(pollCategory)){
                case INVITES:
                    try{
                        List<InviteBean> invites = inviteManager.getInvites(gcn);
                        modelMap.put("status", "success");
                        modelMap.put("data",invites);
                        modelMap.put("message", getMessageText("profile.action.pollRequestInvites.success",null));
                    }catch(Exception e){
                        logger.error(e.getMessage());
                        e.printStackTrace();
                        modelMap.put("status", "failure");
                        modelMap.put("message", getMessageText("profile.action.pollRequestInvites.failure",null));
                    }
                    break;
                case MESSAGES:
                    try{
                        List<MessageBean> messages = messageManager.getMessages(gcn,false);
                        modelMap.put("status", "success");
                        modelMap.put("data",messages);
                        modelMap.put("message", getMessageText("profile.action.pollRequestMessages.success",null));
                    }catch(Exception e){
                        logger.error(e.getMessage());
                        modelMap.put("status", "failure");
                        modelMap.put("message", getMessageText("profile.action.pollRequestMessages.failure",null));
                    }
                    break;
                case PASSWORD_UPDATE:
                    try{
                        HashMap<String,String> dataMap = new HashMap<String, String>();
                        dataMap.put("passwordChangeFlag",String.valueOf(user.isPasswordChangeFlag()));
                        modelMap.put("status", "success");
                        modelMap.put("data",dataMap);
                        modelMap.put("message", getMessageText("profile.action.pollRequestPwdChange.success",null));

                    }catch(Exception e){
                        logger.error(e.getMessage());
                        modelMap.put("status", "failure");
                        modelMap.put("message", getMessageText("profile.action.pollRequestPwdChange.failure",null));
                    }
                    break;
                case ACTIVE_CHAT_MESSAGES:
                    try{
                        List<MessageBean> messages = messageManager.getMessages(gcn,true);
                        modelMap.put("status", "success");
                        modelMap.put("data",messages);
                        modelMap.put("message", getMessageText("profile.action.pollRequestMessages.success",null));
                    }catch(Exception e){
                        logger.error(e.getMessage());
                        modelMap.put("status", "failure");
                        modelMap.put("message", getMessageText("profile.action.pollRequestMessages.failure",null));
                    }
                    break;



            }
        }else{
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.pollRequest.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain the Contac list associated with a GCN
     * @param gcn
     * @return MV object with list of BasicProfileDataBean objects
     */
    @RequestMapping("getContacts")
    public ModelAndView getContacts(@RequestParam(required=true) String gcn ) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            Collection<User> users = userManager.getContacts(gcn);
            Collection<BasicProfileDataBean> contacts = new ArrayList<BasicProfileDataBean>();
            for(User user : users){
                contacts.add(EntityToBeanMapper.getInstance().doMapping(user,BasicProfileDataBean.class));
            }
            modelMap.put("status", "success");
            modelMap.put("data",contacts);
            modelMap.put("message", getMessageText("profile.action.getContacts.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.getContacts.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain the contact GCN's profile details
     * @param gcn
     * @param contactGCN
     * @return MV object with ProfileDataBean object
     */
    @RequestMapping("getContactProfileDetails")
    public ModelAndView getContactProfileDetails(@RequestParam(required=true) String gcn, @RequestParam(required=true) String contactGCN ) {
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
            ProfileDataBean profileDataBean = userManager.getContactProfileDetails(gcn, contactGCN);
            modelMap.put("status", "success");
            modelMap.put("data", profileDataBean);
            modelMap.put("message", getMessageText("profile.action.getContactProfileDetails.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.getContactProfileDetails.failure",null));
        }

        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain basic details of the associated contact
     * @param gcn
     * @param contactGCN
     * @return MV object with ProfileDataBean object
     */
    @RequestMapping("getContactChatDetails")
    public ModelAndView getContactChatDetails(@RequestParam(required=true) String gcn, @RequestParam(required=true) String contactGCN ) {
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
            Map<String,String> dataMap = userManager.getContactChatDetails(gcn, contactGCN);
            modelMap.put("status", "success");
            modelMap.put("data",dataMap);
            modelMap.put("message", getMessageText("profile.action.getContactChatDetails.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.getContactChatDetails.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to obtain the Recent Updates of the contacts associated with a GCN
     * @param gcn
     * @return  MV object with list of ProfileVisibilityBean objects
     */
    @RequestMapping("getRecentUpdates")
    public ModelAndView getRecentUpdates(@RequestParam(required=true) String gcn){
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            String userGCN = userManager.getUserGCN(gcn);
            Map recentUpdates = userManager.getRecentUpdates(userGCN);
            List result = new ArrayList();
            for(Object key : recentUpdates.keySet()) {
                List list =  (List)recentUpdates.get(String.valueOf(key));
                result.add(new ProfileVisibilityBean((String)key,(String)list.get(0),(byte[])list.get(1)));
            }


            modelMap.put("status", "success");
            modelMap.put("data",result);
            modelMap.put("message", getMessageText("profile.action.getRecentUpdates.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.getRecentUpdates.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Method used to search a User by GCN number
     * @param gcn
     * @param request
     * @return MV object with details of user name and city
     */
    @RequestMapping(value = "searchByGCN", method = RequestMethod.POST)
    public ModelAndView searchByGCN(@RequestParam(required=true) String gcn,HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        Map<String, String> data = new HashMap<String, String>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(gcn);
            data.put("firstName",user.getFirstName());
            data.put("lastName",user.getLastName());
            if(user.getResidentialAddress() != null)
                data.put("city",user.getResidentialAddress().getCity());
            else
                data.put("city",null);
            modelMap.put("data",data);
            modelMap.put("status", "success");
            modelMap.put("message",getMessageText("profile.action.searchByGCN.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.searchByGCN.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to update the shared profile details with a particular contact
     * @param gcn
     * @param contactGCN
     * @param visibleProfileFields
     * @return MV object with "updateSharedProfileFields" operation's success or failure response
     */
    @RequestMapping(value = "updateSharedProfileFields", method = RequestMethod.POST)
    public ModelAndView updateSharedProfileFields(@RequestParam(required=true) String gcn,@RequestParam(required=true) String contactGCN,@RequestParam(required=true) String visibleProfileFields) {
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
            userManager.updateSharedProfileFields(gcn,contactGCN,GCNUtils.isNullOrEmpty(visibleProfileFields)?new ArrayList<String> ():Arrays.asList(visibleProfileFields.split(",")));
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("profile.action.updateSharedProfileFields.success",null));

        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.updateSharedProfileFields.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used for forgot password
     * @param emailId
     * @return MV object with "forgotPwd" operation's success or failure response
     */
    @RequestMapping(value = "forgotPwd", method = RequestMethod.POST)
    public ModelAndView forgotPwd(@RequestParam(required=true) String emailId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateEmailAddress(emailId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.email.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUserByEmail(emailId);
            if(user != null){
                userManager.addUserProperty(user,"resetPwdToken", RandomStringUtils.randomAlphanumeric(12));
                userManager.addUserProperty(user,"resetPwdEmailSent", "0");
                modelMap.put("status", "success");
                modelMap.put("message", getMessageText("profile.action.forgotPwd.success",null));
            }else{
                modelMap.put("status", "failure");
                modelMap.put("message", getMessageText("profile.action.forgotPwd.failure.userWithEmailNotFound",null));
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.forgotPwd.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used for validating the password token -
     * this will be called from the link in the password reset email sent after the user requests for password
     * @param emailId
     * @param pwdToken
     * @return MV object with "validatePwdToken" operation's success or failure response
     */
    @RequestMapping(value = "validatePwdToken", method = RequestMethod.POST)
    public ModelAndView validatePwdToken(@RequestParam(required=true) String emailId,@RequestParam(required=true) String pwdToken) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateEmailAddress(emailId)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.email.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUserByEmail(emailId);
            if(user != null){
                UserProperty prop = userManager.getUserProperty(user,pwdToken);
                if(prop != null){
                    modelMap.put("status", "success");
                    modelMap.put("message", getMessageText("profile.action.validatePwdToken.success",null));
                }else{
                    modelMap.put("status", "failure");
                    modelMap.put("message", getMessageText("profile.action.validatePwdToken.failure.resetTokenNotFound",null));
                }
                userManager.removeUserProperty(user.getUserGCN(),"resetPwdToken");
                userManager.removeUserProperty(user.getUserGCN(),"resetPwdEmailSent");


            }else{
                modelMap.put("status", "failure");
                modelMap.put("message", getMessageText("profile.action.validatePwdToken.failure.userWithEmailNotFound",null));
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message",getMessageText("profile.action.validatePwdToken.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }

    /**
     * Method used to reset password.  It is not a secured call and is used in Forgot Password workflow
     * @param gcn
     * @param password
     * @return MV object with "resetPassword" operation's success or failure response
     */
    @RequestMapping("resetPassword")
    public ModelAndView resetPassword(@RequestParam(required=true) String gcn, @RequestParam(required = true) String password) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if(!ValidationUtils.isValidateGCN(gcn)){
            modelMap.put("status","failure");
            modelMap.put("message",getMessageText("field.validation.gcn.failure",null));
            return new ModelAndView("jsonView", modelMap);
        }
        try{
            User user = userManager.getUser(gcn);
            if(user != null){
                String pwd = new String((new BASE64Decoder()).decodeBuffer(password));
                user.setPassword(GCNUtils.md5Encoder.encodePassword(pwd, GCNUtils.encoderSalt));
                user.setPasswordChangeFlag(true);
            }
            userManager.updateUser(user);
            modelMap.put("status", "success");
            modelMap.put("message", getMessageText("profile.action.resetPassword.success",null));
        }catch(Exception e){
            logger.error(e.getMessage());
            modelMap.put("status", "failure");
            modelMap.put("message", getMessageText("profile.action.resetPassword.failure",null));
        }
        return new ModelAndView("jsonView", modelMap);
    }


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private UserManager userManager;

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    private AddressManager addressManager;

    public void setAddressManager(AddressManager addressManager) {
        this.addressManager = addressManager;
    }

    @Autowired
    private InviteManager inviteManager;

    public InviteManager getInviteManager() {
        return inviteManager;
    }

    public void setInviteManager(InviteManager inviteManager) {
        this.inviteManager = inviteManager;
    }

    @Autowired
    private MessageManager messageManager;

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Autowired
    private ImageManager imageManager;

    public ImageManager getImageManager() {
        return imageManager;
    }

    public void setImageManager(ImageManager imageManager) {
        this.imageManager = imageManager;
    }

    @Autowired
    private DeviceManager deviceManager;

    public DeviceManager getDeviceManager() {
        return deviceManager;
    }

    public void setDeviceManager(DeviceManager deviceManager) {
        this.deviceManager = deviceManager;
    }
}
