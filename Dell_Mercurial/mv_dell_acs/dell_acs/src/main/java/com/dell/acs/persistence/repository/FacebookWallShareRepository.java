package com.dell.acs.persistence.repository;

import com.dell.acs.persistence.domain.FacebookWallShare;
import com.sourcen.core.notification.EmailAddressNotification;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FacebookWallShareRepository extends IdentifiableEntityRepository<Long, FacebookWallShare> {

    /**
     * Return all the records which are eligible for coupon email notifications.
     * All the records which have 0 or -1 on the <code>notification</code>
     * column are eligible for notification.
     *
     * @return a List of EmailAddressNotification
     */
    public List<EmailAddressNotification> getEligibleNotifications();

}
