package com.dell.acs.curation;

import com.dell.acs.DellTestCase;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.jobs.CurationSourceCacheImportJobTest;
import com.dell.acs.managers.RetailerManager;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.util.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 2704 $, $Date:: 2012-07-18 10:23:47#$
 */
public class CurationFlowTest extends DellTestCase {


    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext();
        applicationContext.setConfigLocations(
                new String[]{"/spring/test/applicationContext-test.xml", "/spring/applicationContext-jobs.xml"});
        applicationContext.getEnvironment().setActiveProfiles("default", "persistence", "test");
        applicationContext.refresh();
        setUpManagers();
    }


    /* RetailerManager */
    RetailerManager retailerManager = (RetailerManager)
            applicationContext.getBean("retailerManagerImpl", RetailerManager.class);


    /**
     * flowExecutor() method provide the functionality for
     * managing the whole Curation flow
     */
    @Test
    public void flowExecutor() throws Throwable {
         //Curation curation = createCuration();
         CurationSource curationSource = createCurationSource(1L);
         curationImport();
    }

    /**
     * createCuration() method provide the functionality for
     * crating the Curation object and returning the persistence
     * state Curation Object
     *
     * @return persistence state Curation Object
     * @throws Exception
     */
    public Curation createCuration() throws Exception {
        Curation curation = new Curation();

        User user = userManager.getUser("admin");
        Assert.notNull(user, "User Object Not Found");

        RetailerSite retailerSite = retailerManager.getRetailerSitebyName("dell");
        Assert.notNull(retailerSite, "RetailerSite Object Not Found");

        curation.setActive(true);
        curation.setCreatedDate(new Date());
        curation.setDescription("Dell World,China ");
        curation.setModifiedDate(new Date());
        curation.setName("Dell World,China ");
        curation.setRetailerSite(retailerSite);
        curation.setCreatedBy(user);
        curation.setModifiedBy(user);
        curation = curationManager.saveCuration(curation);
        curation.getProperties().setProperty("app.name", "Apple World ");

        curationManager.updateCuration(curation);
        Assert.notNull(curation, "Curation Not Saved yet..");

        logger.info("Curation:= " + curation.getName() + " created successfully...");

        return curation;
    }

    /**
     * createCurationSource() method provide the functionality for
     * crating the CurationSource object and returning the persistence
     * state CurationSource Object
     *
     * @param curationId
     * @return
     */
    public CurationSource createCurationSource(Long curationId) throws Exception {

        User user = userManager.getUser("admin");
        Assert.notNull(user, "User Object Not Found");

        CurationSource curationSource = new CurationSource("TechCrunch"
                , EntityConstants.CurationSourceType.RSS_FEED.getId()
                , user);

        Map<String, String> properties = new HashMap<String, String>() {
            {
                //put("rss.url", "http://feeds.feedburner.com/delltechcenter");
                put("rss.url", "http://feeds.feedburner.com/TechCrunch");
            }
        };

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            curationSource.getProperties().setProperty(entry.getKey(), entry.getValue());
        }

        curationSourceManager.createSource(curationId,curationSource);
        Assert.notNull(curationSource, "Curation Source Not Saved yet..");

        return curationSource;
    }

    /**
     * curationImport() provide the functionality for starting
     * import job for the curation by using curation source and curation
     * and create curation cache object and persist that imported data
     * and creating curation content
     *
     * @throws Exception
     */
    public void curationImport() throws Exception {

        /* final class ImportCurationJobTest extends QuartzJobTest {

           ImportCurationJobTest() {
               super(null);
           }

           public void startImportJob() throws Exception {
                this.executeJob("curationSourceCacheImportJobTrigger");
           }
       }

       new ImportCurationJobTest().startImportJob();*/
      new CurationSourceCacheImportJobTest("curationSourceCacheImportJobTrigger").scheduleJob();
    }

}



