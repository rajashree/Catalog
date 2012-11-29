package com.dell.acs.persistence.repository.impl.hibernate;

import com.dell.acs.CouponAlreadyExistsException;
import com.dell.acs.persistence.domain.Coupon;
import com.dell.acs.persistence.domain.CouponProperty;
import com.dell.acs.persistence.repository.CouponRepository;
import com.sourcen.core.persistence.repository.impl.hibernate.PropertiesAwareRepositoryImpl;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 12:13 PM
 */
@Repository
public class CouponRepositoryImpl extends PropertiesAwareRepositoryImpl<Coupon>
        implements CouponRepository {

    /**
     * Logger instance for CouponRepositoryImpl.java class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CouponRepositoryImpl.class);

    /**
     * Default constructor.
     */
    public CouponRepositoryImpl() {
        super(Coupon.class, CouponProperty.class);
    }

    @Override
    public Coupon getCoupon(Long couponID) {
        try {
            Object o = getSession().createCriteria(Coupon.class)
                    .add(Restrictions.eq("id", couponID));
            if (o != null) {
                return (Coupon) o;
            }
        } catch (Exception e) {
            logger.warn("CouponRepositoryImpl " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteCoupon(Long couponID) {
        remove(couponID);
    }

    @Override
    public Coupon saveCoupon(Coupon coupon) throws CouponAlreadyExistsException {
        try {
            if (coupon.getId() != null) {
                update(coupon);
                LOG.debug("Updated coupon - " + coupon.getId() + " :: " + coupon.getCouponCode());
            } else {
                insert(coupon);
                LOG.debug("Saved new coupon - " + coupon.getCouponCode());
            }
        } catch (HibernateException hex) {
            LOG.error("Coupon already exists with that CouponCode.");
            throw new CouponAlreadyExistsException(hex.getMessage());
        }

        return coupon;

    }

    @Override
    public List<Coupon> getCouponsByRetailerSite(Long retailerSiteID) {

        LinkedList<Coupon> coupons = new LinkedList<Coupon>();
        try {
            Criteria criteria = getSession().createCriteria(Coupon.class);
            List<Object> oList = criteria.add(Restrictions.eq("retailerSite.id", retailerSiteID))
                    .list();
            if (oList != null) {
                for (Object cObj : oList) {
                    Coupon c = (Coupon) cObj;

                    coupons.add(c);
                }
                return coupons;
            }
        } catch (Exception e) {
            logger.warn("Get Coupons by Retailer Site ID " + e.getMessage());
        }
        return null;

    }

    public Coupon getActiveCoupon(Long retailerSiteID) {
        logger.info("Fetching Active coupon for the retailerSiteID  ::: " + retailerSiteID);
        Coupon coupon = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 7);
            Criteria criteria = getSession().createCriteria(Coupon.class)
                    .add(Restrictions.ge("endDate", new Date()))
                    .add(Restrictions.lt("endDate", calendar.getTime()))
                    .add(Restrictions.eq("retailerSite.id", retailerSiteID));
            criteria.setMaxResults(1);
            if (criteria.list() != null) {
                coupon = (Coupon) criteria.list().get(0);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching active coupon for the retailerSiteID -  " + retailerSiteID + "  ::  " + e.getMessage());
        }

        return coupon;
    }

    @Override
    public Coupon getActiveCoupon() {
        Coupon coupon = null;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 7);
            Criteria criteria = getSession().createCriteria(Coupon.class)
                    .add(Restrictions.ge("endDate", new Date()))
                    .add(Restrictions.lt("endDate", calendar.getTime()));
            criteria.setMaxResults(1);
            if (criteria.list() != null) {
                coupon = (Coupon) criteria.list().get(0);
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching active coupon :: " + e.getMessage());
        }

        return coupon;
    }

    /**
     * @inheritdoc
     */
    @Override
    public List<Coupon> getAllCoupons() {
        Criteria criteria = getSession().createCriteria(Coupon.class);
        List<Coupon> coupons = criteria.list();
        return coupons;
    }
}
