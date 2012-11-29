package com.dell.acs.managers;


import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 4/2/12
 * Time: 3:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCouponManagerImpl {

    private static final Logger log = LoggerFactory.getLogger(TestCouponManagerImpl.class);

    protected static ApplicationContext applicationContext;

    private CouponManager couponManager = applicationContext.getBean("couponManagerImpl", CouponManager.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }


    @Test
    public void successTestActiveCouponByRetailerSiteID() {
        if (couponManager != null) {
            log.info(" Active coupon for the Retailer SiteID :::   " + this.couponManager.getActiveCoupon(1L).getCouponCode());
        }

    }

    @Test
    public void failureTestActiveCouponByRetailerSiteID() {
        if (couponManager != null) {
            log.info(" Active coupon for the Retailer SiteID :::   " + this.couponManager.getActiveCoupon(0L));
        }

    }
}
