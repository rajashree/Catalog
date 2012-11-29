package com.dell.acs.managers;

import com.dell.acs.CouponAlreadyExistsException;
import com.dell.acs.persistence.domain.Coupon;
import com.sourcen.core.managers.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CouponManager extends Manager {


    Coupon saveCoupon(Coupon coupon) throws CouponAlreadyExistsException;

    Coupon updateCoupon(Coupon coupon) throws CouponAlreadyExistsException;

    Coupon getCoupon(Long couponID);

    List<Coupon> getCouponsForRetailerSite(Long retalierID);

    Coupon getActiveCoupon(Long retailerSiteID);

    void deleteCoupon(Long coupondID);

    List<Coupon> getAllCoupons();

    public void deleteCoupon(Coupon coupon);
}
