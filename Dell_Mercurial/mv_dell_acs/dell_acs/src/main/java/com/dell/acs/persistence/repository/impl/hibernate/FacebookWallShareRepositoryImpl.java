package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.persistence.domain.FacebookWallShare;
import com.dell.acs.persistence.domain.FacebookWallShareProperty;
import com.dell.acs.persistence.repository.FacebookWallShareRepository;
import com.sourcen.core.notification.EmailAddressNotification;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class FacebookWallShareRepositoryImpl extends PropertiesAwareRepositoryImpl<FacebookWallShare> implements FacebookWallShareRepository {

    /**
     * Logger instance for CouponRepositoryImpl.java class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FacebookWallShareRepositoryImpl.class);

    /**
     * Default constructor.
     */
    public FacebookWallShareRepositoryImpl() {
        super(FacebookWallShare.class, FacebookWallShareProperty.class);
    }


    /**
     * Return all the records which are eligible for coupon email notifications.
     * All the records which have 0 or -1 on the <code>notification</code>
     * column are eligible for notification.
     *
     * @return a List of EmailAddressNotification
     */
    @Override
    public List<EmailAddressNotification> getEligibleNotifications() {
        List<EmailAddressNotification> notificationList = null;
        Criteria criteria = getSession().createCriteria(FacebookWallShare.class)
                .add(Restrictions.in("notification", new Object[]{0, -1}));
        criteria.setMaxResults(100);

        List<FacebookWallShare> shares = criteria.list();

        if (shares != null) {
            EmailAddressNotification notification;
            notificationList = new ArrayList<EmailAddressNotification>(shares.size());
            for (FacebookWallShare share : shares) {
                //TODO: We do not have the Facebook Name coming into the content sever. We might capture that as well to ensure we address the user with name, than email.
                notification = new EmailAddressNotification(share.getEmailID(), share.getCouponCode(), share.getEmailID(), share.getId());
                if (notification != null) {
                    notificationList.add(notification);
                } //if
            }//for
        }//if

        return notificationList;
    }
}
