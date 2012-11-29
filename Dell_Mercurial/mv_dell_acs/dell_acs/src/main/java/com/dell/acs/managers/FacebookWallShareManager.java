package com.dell.acs.managers;

import com.dell.acs.persistence.domain.FacebookWallShare;
import com.sourcen.core.managers.Manager;
import com.sourcen.core.notification.EmailAddressNotification;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FacebookWallShareManager extends Manager {

    /**
     * Save the Facebook user & the details of the wall post share.
     *
     * @param fbWallShare FacebookWallShare object
     * @return the updated FacebookWallShare object after sharing.
     */
    FacebookWallShare saveFacebookWallShare(FacebookWallShare fbWallShare);

    /**
     * Returns all the eligible notification records for which notifications
     * need to sent. All the record which have notification which have 0 or -1
     * are fetched for notifications.
     *
     * @return List of all the eligible EmailAddressNotification
     */
    List<EmailAddressNotification> getEligibleNotifications();

    /**
     * Update the notification column to either 1 or -1.
     * 1 - Notification successfully sent
     * -1 - Unable to send the notification. Therefore, the record will be qualified
     * for the next batch process.
     *
     * @param notification
     * @return
     */
    EmailAddressNotification updateFacebookWallShare(EmailAddressNotification notification);


}
