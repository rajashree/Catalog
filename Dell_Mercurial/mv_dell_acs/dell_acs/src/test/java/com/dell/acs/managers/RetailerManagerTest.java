package com.dell.acs.managers;

import com.dell.acs.persistence.domain.Retailer;
import com.dell.acs.persistence.repository.RetailerRepository;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Raghavendra
 * Date: 5/14/12
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class RetailerManagerTest {
    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(RetailerManagerImpl.class);
    RetailerRepository repository = applicationContext.getBean("retailerRepositoryImpl", RetailerRepository.class);
    RetailerManager manager = (RetailerManager) applicationContext.getBean("retailerManagerImpl", RetailerManager.class);

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

    /* Adding the retailer*/

    @Test
    public void testCreateRetailer()
    {
        Retailer retailer=new Retailer();
       if(manager!=null)
       {

           retailer.setName("test");

           retailer.setCreatedDate(new Date());
           retailer.setModifiedDate(new Date());
           retailer.setDescription("test description");
             manager.createRetailer(retailer);
        //   repository.insert(retailer);
       }
    }

    /* Updating Retailer*/
    @Test
    public void updateRetailerSite()
    {
        if(manager!=null)
        {
            Retailer retailer=manager.getRetailer(2L);
            retailer.setCreatedDate(new Date());
            retailer.setDescription("test Description");
            retailer.setModifiedDate(new Date());
            manager.updateRetailer(retailer);

        }
    }
}
