package com.dell.acs.managers;

import com.dell.acs.persistence.domain.FacebookWallShare;
import com.dell.acs.persistence.repository.FacebookWallShareRepository;
import com.sourcen.core.notification.EmailAddressNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class FacebookWallShareManagerImpl implements FacebookWallShareManager {

    private static final Logger log = LoggerFactory.getLogger(FacebookWallShareManagerImpl.class);

    @Autowired
    private FacebookWallShareRepository facebookWallShareRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public FacebookWallShare saveFacebookWallShare(FacebookWallShare fbWallShare) {
        this.facebookWallShareRepository.insert(fbWallShare);
        return fbWallShare;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<EmailAddressNotification> getEligibleNotifications() {
        return this.facebookWallShareRepository.getEligibleNotifications();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public EmailAddressNotification updateFacebookWallShare(EmailAddressNotification notification) {
        if (notification != null) {
            // log.info(" Update the facebook share notification to :::: "+notification.toString());
            //log.info(" Update the facebook share notification to :::: "+facebookWallShareRepository.get(notification.getShareId()).toString());
            FacebookWallShare share = facebookWallShareRepository.get(notification.getShareId());
            share.setNotification(Integer.valueOf(notification.getStatus()));
            this.facebookWallShareRepository.update(share);
        }
        return notification;
    }


}
