package com.dell.acs.web.ws.v1;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 3:13 PM
 */
public interface FacebookWallShareService {
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
    String fbWallPost(String fbUID, String fbAuthToken, String email, String contentID);


}
