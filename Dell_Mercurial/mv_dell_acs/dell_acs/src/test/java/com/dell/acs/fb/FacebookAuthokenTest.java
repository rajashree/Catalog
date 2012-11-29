package com.dell.acs.fb;

import com.restfb.DefaultFacebookClient;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.types.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/28/12
 * Time: 2:06 PM
 */
public class FacebookAuthokenTest {

    private static final Logger logger = LoggerFactory.getLogger(FacebookAuthokenTest.class);
    
    @Test
    public void fbValidAuthTokenTest() {
        /**
         * Creating the facebook user instance using access token.
         */
        String fbAuthToken = "AAACEdEose0cBAN51BpxWc3BamfuAORZAAbn6VEsLBn8oIq1vVPXBfUm4mTQBzoxPixVCZCA4279vOW83lMt7DHKAXnW8LOWHoN4rKjDS2wsPT15lUQ";
        DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbAuthToken);
        User facebookUser = facebookClient.fetchObject("me", User.class);
        Assert.notNull(facebookUser, "facebookUser cannot be null");
        logger.debug("Email   :::    " + facebookUser.getEmail());
        logger.debug("Bio   :::    " + facebookUser.getBio());
        logger.debug("FacebookUser    :::    " + facebookUser.toString());

    }

    @Test
    public void fbInvalidAuthTokenTest() {

        String fbAuthToken = "AAACEdEose0cBAED8VTePumjVmXvnxTQ4IYvTEU90ssZCV9gEmdvI4FIGV22yxZAEK89nci81fynEDHzGFZBZC76zEFoorgorZAUt8muR1C2ZCVEAZB3JsdG";
        boolean isValid = true;
        try {
            DefaultFacebookClient facebookClient = new DefaultFacebookClient(fbAuthToken);
            User facebookUser = facebookClient.fetchObject("me", User.class);
        } catch (FacebookOAuthException fbOauthEx) {
            String oauthExMsg = fbOauthEx.getMessage();
            logger.debug("Facebook OAuth failed due to invalid AuthToken ::  " + fbOauthEx.getMessage());
            if (oauthExMsg.contains("OAuthException")) {
                isValid = false;
            }
        }

    }


}
