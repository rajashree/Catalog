/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.managers.UserManager;
import com.bmsils.gcn.notification.ForgotPwdNotification;
import com.bmsils.gcn.persistence.domain.Device;
import com.bmsils.gcn.persistence.domain.ProfileVisibility;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.domain.UserProperty;
import com.bmsils.gcn.persistence.repository.*;
import com.bmsils.gcn.utils.GCNUtils;
import com.bmsils.gcn.web.beans.ProfileDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 3/22/12
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserManagerImpl implements UserManager {


    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);


    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public HashMap<String,Object> login(String gcn, String password, String uuid, String deviceType){
        HashMap<String, Object> result = new HashMap<String, Object>();
        User user = userRepository.get(gcn);
        if(user != null){
            result.put("isAdmin",user.isAdmin());
            try {
                String pwd = new String((new BASE64Decoder()).decodeBuffer(password));
                if(user.getPassword().equals(GCNUtils.md5Encoder.encodePassword(pwd, GCNUtils.encoderSalt))){
                    updatePresence(user);
                    List<Device> devices = deviceRepository.getDeviceDetails(user.getUserGCN());
                    if(GCNUtils.isNullOrEmpty(uuid)){
                        result.put("resultType",ApplicationConstants.AuthenticationResultType.LOGIN_SUCCESSFUL);
                        return result;
                    }else{
                        for(Device device : devices){
                            if(uuid.equals(device.getUuid())){
                                result.put("resultType",ApplicationConstants.AuthenticationResultType.LOGIN_SUCCESSFUL);
                                return result;
                            }
                        }
                        Device obj = new Device();
                        obj.setUuid(uuid);
                        obj.setUserGCN(user);
                        obj.setDeviceType(deviceType);
                        deviceRepository.insert(obj);
                        updatePresence(user);
                        result.put("resultType",ApplicationConstants.AuthenticationResultType.MULTIPLE_DEVICE_LOGIN);
                        return result;
                    }
                }else{
                    result.put("resultType",ApplicationConstants.AuthenticationResultType.PASSWORD_INCORRECT);
                    return result;
                }
            } catch (IOException e) {
                result.put("resultType",ApplicationConstants.AuthenticationResultType.PASSWORD_INCORRECT);
                return result;
            }
        }else{
            result.put("resultType",ApplicationConstants.AuthenticationResultType.USER_NOT_FOUND);
            return result;
        }


    }

    private void updatePresence(User user){
        user.setPresence(ApplicationConstants.PresenceType.AVAILABLE.getValue());
        userRepository.merge(user);
    }

    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(String userGCN) {
        User user = userRepository.get(userGCN);
        return user;
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUserByEmail(String emailid) {
        User user = userRepository.getUserByEmail(emailid);
        return user;
    }

    
    @Transactional
    public void markAsAdmin(User user, boolean isAdmin) {
        user.setAdmin(isAdmin);
        userRepository.merge(user);
    }

    
    public List<ForgotPwdNotification> getEligibleForgotPasswordRequests() {
        return userRepository.getEligibleForgotPasswordRequests();
    }

    @SuppressWarnings("unchecked")
	
    public void updateProfileStatus(User user, String profileStatus) {
        user.setProfileStatus(profileStatus);
       
        userRepository.merge(user);
    }


    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void registerGCN(String gcn, String password, String uuid, String deviceType, Long phoneNumber, String email) throws IOException {
        String pass = new String((new BASE64Decoder()).decodeBuffer(password));
        String pwd = GCNUtils.md5Encoder.encodePassword(pass, GCNUtils.encoderSalt);
        User obj = new User();
        obj.setUserGCN(gcn);
        obj.setPassword(pwd);
        obj.setEmailId(email);
        obj.setPhoneNumber(phoneNumber);
        obj.setCreationDate(new Date());
        obj.setLastUpdateDate(new Date());
        userRepository.insert(obj);

        if(ApplicationConstants.DeviceType.isValidDevice(deviceType)){
            Device device = new Device();
            device.setUuid(uuid);
            device.setUserGCN(obj);
            device.setDeviceType(deviceType);
            device.setPrimaryDevice(true);
            deviceRepository.insert(device);
        }

    }

    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public User getUser(String gcn, String password) {
        return (User)userRepository.get(gcn, password);
    }

    
    public Collection<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        /*   users.add(new User("rob", "Robert", "Erskine"));
       users.add(new User("bob","Bob","Marshall"));*/
        return users;
    }

    
    @Transactional
    public void addUser(User user) {
        userRepository.put(user);
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateUser(User user) {
        userRepository.update(user);
    }

    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Device getUserSettings(String userGCN, String uuid) {
        User user = userRepository.get(userGCN);
        if(user != null){
            return deviceRepository.getDeviceDetails(user.getUserGCN(), uuid);
        }
        return null;
    }

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateUserSettings(String userGCN, String uuid, boolean isPrimaryDevice) {
        List<Device> devices = deviceRepository.getDeviceDetails(userGCN);
        for(Device d:devices){
            if(d.getUuid().equalsIgnoreCase(uuid)){
                d.setPrimaryDevice(isPrimaryDevice);
                deviceRepository.update(d);
            }else{
                d.setPrimaryDevice(!isPrimaryDevice);
                deviceRepository.update(d);
            }
        }
    }

    
    @Transactional
    public Collection<User> getContacts(String gcn) {
        return inviteRepository.getContacts(getUserGCN(gcn));
    }

    
    @Transactional
    public ProfileDataBean getContactProfileDetails(String gcn, String contactGCN) {
        ProfileVisibility userPV = profileVisibilityRepository.getContactProfileDetailsVisiblity(gcn, contactGCN);
        ProfileVisibility contactPV = profileVisibilityRepository.getContactProfileDetailsVisiblity(contactGCN, gcn);
        User user = userRepository.get(contactGCN);
        profileVisibilityRepository.updateProfileFieldUpdates(contactGCN,gcn,null);
        ProfileDataBean profileDataBean = new ProfileDataBean();
        //Default visibile fields
        if(!GCNUtils.isNullOrEmpty(user.getUserGCN())){
            profileDataBean.setUserGCN(user.getUserGCN());
        }
        if(!GCNUtils.isNullOrEmpty(user.getUserName())){
            profileDataBean.setUserName(user.getUserName());
        }
        if(!GCNUtils.isNullOrEmpty(user.getFirstName())){
            profileDataBean.setFirstName(user.getFirstName());
        }
        if(!GCNUtils.isNullOrEmpty(user.getLastName())){
            profileDataBean.setLastName(user.getLastName());
        }
        if(contactPV.isEmailId())
            profileDataBean.setEmailId(user.getEmailId());
        if(contactPV.isFacebookId())
            profileDataBean.setFacebookId(user.getFacebookId());
        if(contactPV.isLinkedinId())
            profileDataBean.setLinkedinId(user.getLinkedinId());
        if(contactPV.isDesignation())
            profileDataBean.setDesignation(user.getDesignation());
        if(contactPV.isOfficeAddress()){
            if(user.getOfficeAddress() != null){
                profileDataBean.setOfficeAddAddressLine1(user.getOfficeAddress().getAddressLine1());
                profileDataBean.setOfficeAddAddressLine2(user.getOfficeAddress().getAddressLine2());
                profileDataBean.setOfficeAddCity(user.getOfficeAddress().getCity());
                profileDataBean.setOfficeAddCountry(user.getOfficeAddress().getCountry());
                profileDataBean.setOfficeAddPostalCode(user.getOfficeAddress().getPostalCode());
                profileDataBean.setOfficeAddState(user.getOfficeAddress().getState());
            }

        }
        if(contactPV.isResidentialAddress()){
            if(user.getResidentialAddress() != null){
                profileDataBean.setResidentialAddAddressLine1(user.getResidentialAddress().getAddressLine1());
                profileDataBean.setResidentialAddAddressLine2(user.getResidentialAddress().getAddressLine2());
                profileDataBean.setResidentialAddCity(user.getResidentialAddress().getCity());
                profileDataBean.setResidentialAddCountry(user.getResidentialAddress().getCountry());
                profileDataBean.setResidentialAddPostalCode(user.getResidentialAddress().getPostalCode());
                profileDataBean.setResidentialAddState(user.getResidentialAddress().getState());
            }
        }
        if(contactPV.isOfficeEmailId())
            profileDataBean.setOfficeEmailId(user.getOfficeEmailId());
        if(contactPV.isOfficeName())
            profileDataBean.setOfficeName(user.getOfficeName());
        if(contactPV.isOfficePhoneNumber())
            profileDataBean.setOfficePhoneNumber(user.getOfficePhoneNumber());
        if(contactPV.isPhoneNumber())
            profileDataBean.setPhoneNumber(user.getPhoneNumber());
        if(contactPV.isTwitterId())
            profileDataBean.setTwitterId(user.getTwitterId());
        if(contactPV.isOfficeEmailId())
            profileDataBean.setOfficeEmailId(user.getOfficeEmailId());

        HashMap<String,String> properties = new HashMap<String, String>();
        properties.put("userSharedProfileFields",getSharedProfileFields(userPV));
        properties.put("contactSharedProfileFields",getSharedProfileFields(contactPV));
        profileDataBean.setProperties(properties);
        return profileDataBean;
    }

    private String getSharedProfileFields(ProfileVisibility pv){
        List fields = new ArrayList();
        String result = "";
        if(pv.isDesignation())
            fields.add(ApplicationConstants.ProfileFields.DESIGNATION.getValue());
        if(pv.isEmailId())
            fields.add(ApplicationConstants.ProfileFields.EMAIL_ID.getValue());
        if(pv.isFacebookId())
            fields.add(ApplicationConstants.ProfileFields.FACEBOOK_ID.getValue());
        if(pv.isLinkedinId())
            fields.add(ApplicationConstants.ProfileFields.LINKED_IN_ID.getValue());
        if(pv.isPhoneNumber())
            fields.add(ApplicationConstants.ProfileFields.PHONE_NUMBER.getValue());
        if(pv.isResidentialAddress())
            fields.add(ApplicationConstants.ProfileFields.RES_ADDRESS.getValue());
        if(pv.isTwitterId())
            fields.add(ApplicationConstants.ProfileFields.TWITTER_ID.getValue());
        if(pv.isOfficeAddress())
            fields.add(ApplicationConstants.ProfileFields.OFFC_ADDRESS.getValue());
        if(pv.isOfficeName())
            fields.add( ApplicationConstants.ProfileFields.OFFICE_NAME.getValue());
        if(pv.isOfficeEmailId())
            fields.add(ApplicationConstants.ProfileFields.OFFICE_EMAIL_ID.getValue());
        if(pv.isOfficePhoneNumber())
            fields.add(ApplicationConstants.ProfileFields.OFFICE_PHONE_NUMBER.getValue());
        if(fields.size() > 0)
            return StringUtils.collectionToCommaDelimitedString(fields);
        else
            return null;
    }


    
    public void updateProfileFieldUpdates(String gcn, String profileFields) {
    	System.out.println("inside usermanagerimpl line no 316");
        profileVisibilityRepository.updateProfileFieldUpdates(gcn,profileFields);
    }

    
    @Transactional
    public Map<String,String> getContactChatDetails(String gcn, String contactGCN) {
        //The below call is required in case we decide to add additional fields in the response.  This should be based on the field visibility
        //ProfileVisibility profileVisibility = profileVisibilityRepository.getContactProfileDetailsVisiblity(gcn, contactGCN);
        User user = userRepository.get(contactGCN);
        HashMap<String,String> dataMap = new HashMap<String, String>();
        dataMap.put("userGCN",contactGCN);
        dataMap.put("firstName",user.getFirstName());
        dataMap.put("lastConnectedTime",(messageRepository.getLastConnectedTime(gcn,contactGCN) !=null)?messageRepository.getLastConnectedTime(gcn,contactGCN).toString():null);
        return dataMap;
    }


    
    @Transactional
    public Map<String,Object> getRecentUpdates(String gcn) {
        return profileVisibilityRepository.getRecentUpdates(gcn);
    }

    
    @Transactional
    public List<String> getRegisteredGCNs() {
        List<User> users = userRepository.getAll();
        List<String> result = new ArrayList<String>();
        if(users != null){
            for(User user : users){
                result.add(user.getUserGCN());
            }
        }
        return null;
    }

    
    @Transactional
    public void addAlias(String userGCN, String alias) {
        userRepository.addAlias(userGCN,alias);
    }

    
    @Transactional
    public void removeAlias(String userGCN) {
        userRepository.removeAlias(userGCN);
    }

    
    @Transactional
    public int getTotalGCNCount(String email) {
        return userRepository.getTotalGCNCount(email);
    }

    
    @Transactional
    public void updateSharedProfileFields(String gcn, String contactGCN, List<String> visibleProfileFields) {
        ProfileVisibility result =  profileVisibilityRepository.getContactProfileDetailsVisiblity(gcn,contactGCN);
        ProfileVisibility profileVisibility = new ProfileVisibility(result.getUserGcn(),result.getContactGcn());
        for(String profileField : visibleProfileFields){
            if(ApplicationConstants.ProfileFields.getByValue(profileField) != null){

                switch(ApplicationConstants.ProfileFields.getByValue(profileField)){
                    case DESIGNATION:
                        profileVisibility.setDesignation(true);
                        break;
                    case PHONE_NUMBER:
                        profileVisibility.setPhoneNumber(true);
                        break;
                    case EMAIL_ID:
                        profileVisibility.setEmailId(true);
                        break;
                    case FACEBOOK_ID:
                        profileVisibility.setFacebookId(true);
                        break;
                    case TWITTER_ID:
                        profileVisibility.setTwitterId(true);
                        break;
                    case RES_ADDRESS:
                        profileVisibility.setResidentialAddress(true);
                        break;
                    case LINKED_IN_ID:
                        profileVisibility.setLinkedinId(true);
                        break;
                    case OFFICE_NAME:
                        profileVisibility.setOfficeName(true);
                        break;
                    case OFFICE_PHONE_NUMBER:
                        profileVisibility.setOfficePhoneNumber(true);
                        break;
                    case OFFICE_EMAIL_ID:
                        profileVisibility.setOfficeEmailId(true);
                        break;
                    case OFFC_ADDRESS:
                        profileVisibility.setOfficeAddress(true);
                        break;
                    case OFFICE_PHONE_NUMBER2:
                        profileVisibility.setOfficePhoneNumber2(true);
                        break;
                    case PHONE_NUMBER2:
                        profileVisibility.setPhoneNumber2(true);
                        break;

                }
            }

        }
        profileVisibilityRepository.merge(profileVisibility);
    }

    
    public String getUserGCN(String gcn) {
        return userRepository.getUserGCN(gcn);
    }

    
    public List<User> getUserAndAliases(int start, int limit) {
        return userRepository.getUserAndAliases(start, limit);
    }

    
    public boolean addUserProperty(User user, String propName, String propValue) {
        return userPropertyRepository.addUserProperty(user,propName,propValue);
    }

    
    public void updateUserProperty(String userGCN, String propName, String propValue) {
        userPropertyRepository.updateUserProperty(userGCN,propName,propValue);
    }

    public UserProperty getUserProperty(User user, String propValue){
        return userPropertyRepository.getUserProperty(user,propValue);
    }


    
    public boolean removeUserProperty(String userGCN, String propName) {
        User user = userRepository.get(userGCN);
        if(user != null){
            return userPropertyRepository.removeUserProperty(user,propName);
        }
        return false;

    }

    /* 
    @Transactional(readOnly = false, propagation =  Propagation.REQUIRED)
    public void updatePassword(String gcn, String password, boolean b) {
        userRepository.update(user);
    }*/



    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private DeviceRepository deviceRepository;

    public DeviceRepository getDeviceRepository() {
        return deviceRepository;
    }

    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Autowired
    private AddressRepository addressRepository;

    public AddressRepository getAddressRepository() {
        return addressRepository;
    }

    public void setAddressRepository(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Autowired
    private InviteRepository inviteRepository;

    public InviteRepository getInviteRepository() {
        return inviteRepository;
    }

    public void setInviteRepository(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    @Autowired
    private ProfileVisibilityRepository profileVisibilityRepository;

    public ProfileVisibilityRepository getProfileVisibilityRepository() {
        return profileVisibilityRepository;
    }

    public void setProfileVisibilityRepository(ProfileVisibilityRepository profileVisibilityRepository) {
        this.profileVisibilityRepository = profileVisibilityRepository;
    }

    @Autowired
    private MessageRepository messageRepository;

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    public void setMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    private UserPropertyRepository userPropertyRepository;

    public UserPropertyRepository getUserPropertyRepository() {
        return userPropertyRepository;
    }

    public void setUserPropertyRepository(UserPropertyRepository userPropertyRepository) {
        this.userPropertyRepository = userPropertyRepository;
    }

	
	public boolean checkUserFromAlias(String alias, String password) {
		
		userRepository.getFromAlias(alias, password);
		
		return userRepository.getFromAlias(alias, password);
	}
}
