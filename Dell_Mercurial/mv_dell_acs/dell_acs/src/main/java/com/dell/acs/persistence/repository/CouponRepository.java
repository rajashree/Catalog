package com.dell.acs.persistence.repository;

import com.dell.acs.CouponAlreadyExistsException;
import com.dell.acs.persistence.domain.Coupon;
import com.sourcen.core.persistence.repository.IdentifiableEntityRepository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CouponRepository extends IdentifiableEntityRepository<Long, Coupon> {

    Coupon getCoupon(Long couponID);

    void deleteCoupon(Long couponID);

    Coupon saveCoupon(Coupon coupon) throws CouponAlreadyExistsException;

    List<Coupon> getCouponsByRetailerSite(Long retailerSiteID);

    Coupon getActiveCoupon(Long retailerSiteID);

    Coupon getActiveCoupon();

    List<Coupon> getAllCoupons();
}
