/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.managers;

import com.dell.acs.CouponAlreadyExistsException;
import com.dell.acs.persistence.domain.Coupon;
import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.repository.CouponRepository;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/23/12
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CouponManagerImpl implements CouponManager {


    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RetailerSiteRepository retailerSiteRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon saveCoupon(Coupon coupon) throws CouponAlreadyExistsException {
        if ((coupon.getTitle().trim().length() != 0) && 
            (coupon.getDescription().trim().length() != 0) && 
            (coupon.getCouponCode().trim().length() != 0)) {
        	
            if ((coupon.getRetailerSite() != null) && 
            	(coupon.getRetailerSite().getId() != null)) {
            	
                coupon.setCreatedDate(new Date());
                coupon.setModifiedDate(new Date());
                
                RetailerSite retailerSite = retailerSiteRepository.get(coupon.getRetailerSite().getId());
                coupon.setRetailerSite(retailerSite);
                
                Retailer retailer = retailerSite.getRetailer();
                coupon.setRetailer(retailer);
            }
        }
        return this.couponRepository.saveCoupon(coupon);
    }

    /* @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon createCoupon(Coupon coupon){
        if (coupon.getTitle().trim().length() != 0 && coupon.getDescription().trim().length() != 0 && coupon.getCouponCode().trim().length() != 0) {
            couponRepository.insert(coupon);
        }
        return coupon;
    }*/

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon updateCoupon(Coupon coupon) throws CouponAlreadyExistsException {
        //TODO: Need to set Modified by
        if (coupon.getTitle().trim().length() != 0 && coupon.getDescription().trim().length() != 0 && coupon.getCouponCode().trim().length() != 0) {
            coupon.setModifiedDate(new Date());
            this.couponRepository.update(coupon);
        }
        return coupon;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon getCoupon(Long couponID) {
        return this.couponRepository.get(couponID);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<Coupon> getCouponsForRetailerSite(Long retailerSiteID) {
        return this.couponRepository.getCouponsByRetailerSite(retailerSiteID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Coupon getActiveCoupon(Long retailerSiteID) {
        return this.couponRepository.getActiveCoupon(retailerSiteID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteCoupon(Long coupondID) {
        this.couponRepository.deleteCoupon(coupondID);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public List<Coupon> getAllCoupons() {
        return null;
    }

    @Override
    @Transactional
    public void deleteCoupon(Coupon coupon) {
        this.couponRepository.remove(coupon);
    }
}
