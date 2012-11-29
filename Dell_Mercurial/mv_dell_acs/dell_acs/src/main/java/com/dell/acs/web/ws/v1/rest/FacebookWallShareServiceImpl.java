package com.dell.acs.web.ws.v1.rest;

import com.dell.acs.managers.FacebookWallShareManager;
import com.dell.acs.persistence.domain.Coupon;
import com.dell.acs.persistence.domain.FacebookWallShare;
import com.dell.acs.persistence.repository.CouponRepository;
import com.dell.acs.web.ws.v1.FacebookWallShareService;
import com.restfb.DefaultFacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebService;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 3:24 PM
 */
@WebService
@RequestMapping("/api/v1/rest/FBViralShareService")
public class FacebookWallShareServiceImpl extends WebServiceImpl implements FacebookWallShareService {

    private static final Logger LOG = LoggerFactory.getLogger(FacebookWallShareServiceImpl.class);


    /**
     * Store the Facebook User's detail into the content server upon successful
     * wall share.
     * <p/>
     * The fbAuthToken provided by the FB_APP will be authorized against the FB API
     * to ensure that we are not getting spoofed by the User to get free
     * coupons :)
     * <p/>
     * If the AuthToken is valid, then save the user share for the coupon notification
     * scheduler to send an email.
     *
     * @param fbUID
     * @param fbAuthToken
     * @param email
     * @param contentID
     */
    @Override
    @RequestMapping("fbWallPost")
    public String fbWallPost(@RequestParam(required = true) final String fbUID,
                             @RequestParam(required = true) final String fbAuthToken,
                             @RequestParam(required = true) final String email,
                             @RequestParam(required = true) final String contentID) {
        LOG.info("Save the FB wallpost into the content server....");

        //TODO: Need to APPLY BUSINESS RULES here to limit the user from getting creative to get more coupons out of the system.
        //TODO: Discuss with Greg & Steven about the Business Rules, most likely post 3/30
        User facebookUser = null;
        String fbAuthStatus = "Authorization successful.";
        boolean isValid = true;
        String response = "{ \"status\":\"success\"}";
        try {
            DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbAuthToken);
            facebookUser = facebookClient.fetchObject("me", User.class);
        } catch (FacebookOAuthException fbOauthEx) {
            fbAuthStatus = fbOauthEx.getMessage();
            LOG.error("Facebook OAuth failed due to invalid AuthToken ::  " + fbOauthEx.getMessage());
            if (fbAuthStatus.contains("OAuthException")) {
                isValid = false;
            }
        }


        //TODO: We may be unable to get FB User's email over here just using the FBAuthToken. The alternate might
        //be where once the User gives Authorization for the App to post the content on the Wall. May be the
        //FB app could grab the emailID and pass it on to the Content Server service.
        if (isValid) {

            int notification = 0;
            String couponCode = "";
            Coupon coupon = this.couponRepository.getActiveCoupon();
            if (coupon != null) {
                couponCode = coupon.getCouponCode();
            } else {
                //TODO: Fallback coupon or Default coupon? Check with Greg/Steven
            }
            LOG.info("Active Coupon ---- " + couponCode);
            FacebookWallShare fbShareObj = new FacebookWallShare();
            fbShareObj.setFbUID(fbUID);
            fbShareObj.setContentID(contentID);
            fbShareObj.setEmailID(email);
            fbShareObj.setNotification(notification);
            fbShareObj.setCouponCode(couponCode);
            fbShareObj.setFbAuthStatus(fbAuthStatus);

            //Save the fbShare to DB
            FacebookWallShare fbObj = this.facebookWallShareManager.saveFacebookWallShare(fbShareObj);
        } else {

            response = "{ \"status\":\"Authorization failed.\"}";
        }

        LOG.info("FB  status : " + response);
        return response;

    }

    //IOC
    @Autowired
    private FacebookWallShareManager facebookWallShareManager;

    public void setFacebookWallShareManager(FacebookWallShareManager facebookWallShareManager) {
        this.facebookWallShareManager = facebookWallShareManager;
    }

    @Autowired
    private CouponRepository couponRepository;

    public void setCouponRepository(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }
}
