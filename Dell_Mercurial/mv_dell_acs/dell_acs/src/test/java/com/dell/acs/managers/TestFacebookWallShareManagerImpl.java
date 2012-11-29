package com.dell.acs.managers;

import com.dell.acs.persistence.domain.FacebookWallShare;
import com.dell.acs.persistence.repository.FacebookWallShareRepository;
import com.sourcen.core.notification.EmailAddressNotification;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Vivek
 * Date: 3/27/12
 * Time: 2:19 PM
 * A simple test case to test the FacebookWallShareManager
 */
public class TestFacebookWallShareManagerImpl {

    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(TestFacebookWallShareManagerImpl.class);

    FacebookWallShareRepository repository = applicationContext.getBean("facebookWallShareRepositoryImpl", FacebookWallShareRepository.class);
    FacebookWallShareManager manager = applicationContext.getBean("facebookWallShareManagerImpl", FacebookWallShareManager.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

    @Test
    public void testSaveFBWallShare() {
        try {
            if (manager != null) {
                FacebookWallShare fbWall = new FacebookWallShare();
                fbWall.setFbUID("asdaewetrerytryt4545343ggfgf3");
                fbWall.setEmailID("dell@sample.com");
                fbWall.setContentID("productID212");
                fbWall.setNotification(0);
                fbWall.setCouponCode("23231");
                fbWall.setFbAuthStatus("Success");
                fbWall.setCreatedDate(new Date());
                fbWall.setModifiedDate(new Date());


                manager.saveFacebookWallShare(fbWall);
            }
        } catch (Exception ex) {
            logger.info("Error while creating the Campaign");
        }
    }

    @Test
    public void testGetNotificationRecords() {
        logger.info("    No of eligible notifications  " + manager.getEligibleNotifications());
    }

    @Test
    public void testUpdateNotificationStatus() {
        logger.info("   Status of update   ");
        EmailAddressNotification notification = new EmailAddressNotification("vivek@sourcen.com", "DEXPS34", "vivek@sourcen.com", 4, "0");
        this.manager.updateFacebookWallShare(notification);

    }

}
