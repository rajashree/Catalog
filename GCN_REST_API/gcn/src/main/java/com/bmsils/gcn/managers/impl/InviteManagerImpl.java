/*
 * Copyright (c) 2012 - BMS Innolabs Software Pvt Ltd.
 * All rights reserved
 */

package com.bmsils.gcn.managers.impl;

import com.bmsils.gcn.ApplicationConstants;
import com.bmsils.gcn.managers.ConfigurationManager;
import com.bmsils.gcn.managers.InviteManager;
import com.bmsils.gcn.persistence.domain.Invite;
import com.bmsils.gcn.persistence.domain.ProfileVisibility;
import com.bmsils.gcn.persistence.domain.User;
import com.bmsils.gcn.persistence.repository.InviteRepository;
import com.bmsils.gcn.persistence.repository.ProfileVisibilityRepository;
import com.bmsils.gcn.persistence.repository.UserRepository;
import com.bmsils.gcn.utils.EntityToBeanMapper;
import com.bmsils.gcn.web.beans.BasicProfileDataBean;
import com.bmsils.gcn.web.beans.InviteBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rajashree
 * Date: 4/11/12
 * Time: 1:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class InviteManagerImpl implements InviteManager{

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationManagerImpl.class);



    @Override
    @Transactional
    public void inviteContact(User inviter, User invitee, List<String> visibleProfileFields) {
        Invite invite = new Invite();
        invite.setInviteeGcn(invitee);
        invite.setInviterGcn(inviter);
        invite.setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_SENT.getValue());
        invite.setCreationDate(new Date());
        inviteRepository.put(invite);

        ProfileVisibility profileVisibility = new ProfileVisibility();
        profileVisibility.setContactGcn(invitee);
        profileVisibility.setUserGcn(inviter);

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

                }
            }

        }
        profileVisibilityRepository.put(profileVisibility);

    }

    @Override
    @Transactional
    public List<InviteBean> getInvites(String gcn) {
        List<Invite> invites = inviteRepository.getInvites(gcn);
        List<InviteBean> inviteBeanList = new ArrayList<InviteBean>();
        for(Invite invite:invites){
            InviteBean inviteBean = new InviteBean();
            inviteBean.setFromQRFlag(invite.isFromQRFlag());
            inviteBean.setInviter(EntityToBeanMapper.getInstance().doMapping(invite.getInviterGcn(), BasicProfileDataBean.class));
            inviteBean.setInvitee(EntityToBeanMapper.getInstance().doMapping(invite.getInviteeGcn(), BasicProfileDataBean.class));
            inviteBeanList.add(inviteBean);
        }
        return inviteBeanList;
    }

    @Override
    @Transactional
    public int confirmInviteRequest(String inviter, String invitee, List<String> visibleProfileFields,boolean confirm){
           	Invite invite = inviteRepository.get(inviter,invitee);
    	
        if(confirm)
            invite.setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_ACCEPTED.getValue());
        else
            invite.setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_DECLINED.getValue());
        inviteRepository.put(invite);

        ProfileVisibility profileVisibility = new ProfileVisibility();
        profileVisibility.setContactGcn(userRepository.get(inviter));
        profileVisibility.setUserGcn(userRepository.get(invitee));

        for(String profileField : visibleProfileFields){
            if(ApplicationConstants.ProfileFields.getByValue(profileField) != null){
                switch(ApplicationConstants.ProfileFields.getByValue(profileField)){
                    case PHONE_NUMBER:
                        profileVisibility.setPhoneNumber(true);
                        break; 
                    case PHONE_NUMBER2:
                        profileVisibility.setPhoneNumber2(true);
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
                    case DESIGNATION:
                        profileVisibility.setDesignation(true);
                        break;
                    case OFFICE_PHONE_NUMBER2:
                        profileVisibility.setOfficePhoneNumber2(true);
                        break;
                    

                }
            }

        }
        profileVisibilityRepository.put(profileVisibility);

        return invite.getSubscriptionStatus();
    }

    @Override
    @Transactional
    public void handleQRInvite(String gcn, String contactGCN, List<String> gcnVisibleProfileFields, List<String> contatGCNVisibleProfileFields, boolean fromQR) {
        Invite invite = new Invite();
        invite.setSubscriptionStatus(ApplicationConstants.SubscriptionStatus.INVITE_ACCEPTED.getValue());
        User inviter = userRepository.get(gcn);
        invite.setInviterGcn(inviter);
        invite.setInviteeGcn(userRepository.get(contactGCN));
        invite.setFromQRFlag(fromQR);
        invite.setCreationDate(new Date());
        inviteRepository.insert(invite);

        ProfileVisibility profileVisibility = new ProfileVisibility();
        profileVisibility.setContactGcn(userRepository.get(gcn));
        profileVisibility.setUserGcn(userRepository.get(contactGCN));

        for(String profileField : gcnVisibleProfileFields){
            if(ApplicationConstants.ProfileFields.getByValue(profileField) != null){
                switch(ApplicationConstants.ProfileFields.getByValue(profileField)){
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

                }
            }

        }
        profileVisibilityRepository.put(profileVisibility);
        ProfileVisibility profileVisibility1 = new ProfileVisibility();
        profileVisibility1.setContactGcn(userRepository.get(contactGCN));
        profileVisibility1.setUserGcn(userRepository.get(gcn));

        for(String profileField : contatGCNVisibleProfileFields){
            if(ApplicationConstants.ProfileFields.getByValue(profileField) != null){
                switch(ApplicationConstants.ProfileFields.getByValue(profileField)){
                    case PHONE_NUMBER:
                        profileVisibility1.setPhoneNumber(true);
                        break;
                    case EMAIL_ID:
                        profileVisibility1.setEmailId(true);
                        break;
                    case FACEBOOK_ID:
                        profileVisibility1.setFacebookId(true);
                        break;
                    case TWITTER_ID:
                        profileVisibility1.setTwitterId(true);
                        break;
                    case RES_ADDRESS:
                        profileVisibility1.setResidentialAddress(true);
                        break;
                    case LINKED_IN_ID:
                        profileVisibility1.setLinkedinId(true);
                        break;
                    case OFFICE_NAME:
                        profileVisibility1.setOfficeName(true);
                        break;
                    case OFFICE_PHONE_NUMBER:
                        profileVisibility1.setOfficePhoneNumber(true);
                        break;
                    case OFFICE_EMAIL_ID:
                        profileVisibility1.setOfficeEmailId(true);
                        break;
                    case OFFC_ADDRESS:
                        profileVisibility1.setOfficeAddress(true);
                        break;

                }
            }

        }
        profileVisibilityRepository.put(profileVisibility1);
        inviter.setPresence(ApplicationConstants.PresenceType.AVAILABLE.getValue());
        userRepository.merge(inviter);


    }

    @Override
    @Transactional
    public List<Invite> getEligibleInvitesForPN() {
        return inviteRepository.getEligibleInvitesForPN(configurationManager.getProperty("pushNotification.invite.duration"));
    }

    @Override
    @Transactional
    public void blockContact(String gcn, String contactGCN) {
        inviteRepository.blockContact(gcn,contactGCN);
    }

    @Override
    @Transactional
    public void unBlockContact(String gcn, String contactGCN) {
        inviteRepository.unBlockContact(gcn,contactGCN);
    }


    /**
     * Dependency Injection of various Spring beans follows
     */
    @Autowired
    private UserRepository userRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    private ConfigurationManager configurationManager;

    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }
}

