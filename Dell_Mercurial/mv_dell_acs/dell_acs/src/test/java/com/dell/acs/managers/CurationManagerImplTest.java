package com.dell.acs.managers;

import com.dell.acs.DellTestCase;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.util.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/19/12 11:23 AM#$
 */
@RunWith(Parameterized.class)
public class CurationManagerImplTest extends DellTestCase{


    private static Logger logger = LoggerFactory.getLogger(CurationManagerImplTest.class);

    //  Datum value
    private Long userId;

    private Long curationId;

    private Long retailerSiteId;

    private String curationName;

/*    Constructor.
    The JUnit test runner will instantiate this class once for every
    element in the Collection returned by the method annotated with
    @Parameters.*/



    public CurationManagerImplTest(Long userId, Long curationId, Long retailerSiteId, String curationName) {

        this.userId = userId;
        this.curationId = curationId;
        this.retailerSiteId = retailerSiteId;
        this.curationName = curationName;
    }


/*  Test data generator.
       This method is called the the JUnit parameterized test runner and
       returns a Collection of Arrays.  For each Array in the Collection,
       each array element corresponds to a parameter in the constructor.*/


    @Parameterized.Parameters
    public static Collection<Object[]> generateData() {

        // failure case: 0,1,2
        // failure case: 1,3,1 [duplicate entry for Dell World,India ]
        // success case: 1,4,1 [Not duplicate entry]
        Object[][] objectArray = new Object[][]{

                {0L, 5L, 2L, "Dell World,USA"},
                {1L, 3L, 1L, "Dell World,USA"},
                {1L, 4L, 1L, "Dell World,NY"}

        };

        return Arrays.asList(objectArray);
    }


/*     Curation helper method.*/



    @Test
    public void testCreation() throws Exception {

        logger.info("--------------------testCreation--------------------------");
        logger.info("Curation id. " + this.curationId);
        logger.info("Curation Name " + this.curationName);
        logger.info("User id. " + this.userId);
        logger.info("RetailerSite id. " + this.retailerSiteId);
        logger.info("-----------------------------------------------------------");

        User user = userManager.getUser(userId);
        // expected to get the entity here.
        Assert.notNull(user, "Unable to get the user for id:==>" + userId);

        // Demo Value for RetailerSite
        RetailerSite retailerSite = retailerManager.getRetailerSite(1L);
        Assert.notNull(retailerSite, "RetailerSite Object Not Found");

        Curation curation = new Curation();
        curation.setActive(true);
        curation.setDescription("Dell World,USA ");
        curation.setCreatedDate(new Date());
        curation.setModifiedDate(new Date());
        curation.setName(this.curationName);
        curation.setCreatedBy(user);
        curation.setModifiedBy(user);
        curation.setRetailerSite(retailerSite);

        curation = curationManager.saveCuration(curation);

        Assert.notNull(curation.getId(), "Unable to save the curation data.");
    }

    @Test
    public void updateCuration() {
        Long version = 0L;
        logger.info("--------------------updateCuration--------------------------");
        logger.info("Curation id. " + this.curationId);
        logger.info("Curation Name " + this.curationName);
        logger.info("User id. " + this.userId);
        logger.info("RetailerSite id. " + this.retailerSiteId);
        logger.info("-----------------------------------------------------------");

        Curation curation = curationManager.getCuration(this.curationId);
        Assert.notNull(curation, "Curation Object Not Found for Curation ID:==>" + this.curationId);
        curation.setName("Apple App World");
        curation.getProperties().setProperty("app.name","Dell Fun World");
/*        version=curation.getVersion();
        curation.setVersion(version+1);*/

        curationManager.updateCuration(curation);
    }

    @Test
    public void deleteCuration(){
        logger.info("--------------------deleteCuration--------------------------");
        logger.info("Curation id. " + this.curationId);
        logger.info("Curation Name " + this.curationName);
        logger.info("User id. " + this.userId);
        logger.info("RetailerSite id. " + this.retailerSiteId);
        logger.info("-----------------------------------------------------------");

        curationManager.deleteCuration(this.curationId);
    }

    @Test
    public void testgetAllCurationBySite(){

        logger.info("--------------------getAllCurationBySite--------------------------");
        logger.info("Curation id. " + this.curationId);
        logger.info("Curation Name " + this.curationName);
        logger.info("User id. " + this.userId);
        logger.info("RetailerSite id. " + this.retailerSiteId);
        logger.info("-----------------------------------------------------------");

        Collection<Curation> curationList=curationManager.getAllCurationByRetailerSite(this.retailerSiteId);
        for(Curation curation:curationList){
             logger.info("Curation Name:==>"+curation.getName());
        }
    }
}
