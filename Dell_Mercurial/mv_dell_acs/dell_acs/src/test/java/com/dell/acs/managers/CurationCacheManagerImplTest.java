package com.dell.acs.managers;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 * @version $Revision: 2704 $, $Date:: 2012-07-18 10:23:47#$
 */

import com.dell.acs.DellTestCase;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.*;
import com.sourcen.core.util.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * CurationCacheManagerImplTest provides set of functionality for testing
 * the Curation Cache Management Implementation for different Use Cases
 * C.U.R.D Operation Related to Curation Cache Management
 */


public class CurationCacheManagerImplTest extends DellTestCase {


    protected static Logger logger = LoggerFactory.getLogger(CurationCacheManagerImplTest.class);

    private static CurationCacheManager curationCacheManager =
            applicationContext.getBean("curationCacheManagerImpl", CurationCacheManager.class);
    private static CurationManager curationManager =
            applicationContext.getBean("curationManagerImpl", CurationManager.class);
    private static RetailerManager retailerManager =
            applicationContext.getBean("retailerManagerImpl", RetailerManager.class);
    private static UserManager userManager =
            applicationContext.getBean("userManagerImpl", UserManager.class);
    private static CurationSourceManager sourceManager =
            applicationContext.getBean("curationSourceManagerImpl", CurationSourceManager.class);

    @Test
    public void testCreateCurationCacheManager() throws Exception {
        /* Getting the Curation Object */
        Curation curation = createCuration();
        /* Getting the CurationSource object */
        CurationSource curationSource = createCurationSource();

        /* Dummy Data for testing */
        CurationCache curationCache = new CurationCache();
        curationCache.setBody("Curation1 Cache Body ");
        curationCache.setDescription("Curation1 Cache Description");
        curationCache.setTitle("Curation1");
        curationCache.setGuid("12313123ASASASS");
        curationCache.setImportedDate(new Date());
        curationCache.setSource("Curation1 Source");
        curationCache.setPublishedDate(new Date());
//        curationCache.setCuration(curation);
        curationCache.setCurationSource(curationSource);
        curationCache.setUpdatedDate(new Date());
        curationCacheManager.saveCache(curationCache);
    }

    @Test
    public void testDeleteCurationCacheManager() {
        CurationCache curationCache = curationCacheManager.getCache(1L);
        curationCacheManager.deleteCache(curationCache.getId());
    }

    @Test
    public void updateCurationCacheManager() {
        CurationCache curationCache = curationCacheManager.getCache(1L);
        curationCache.setBody("Updated " + curationCache.getBody());
        curationCache.setDescription("Updated " + curationCache.getDescription());
        curationCache.setTitle("Updated " + curationCache.getTitle());
        curationCacheManager.updateCache(curationCache);
    }

    @Test
    public void testExistCurationCache() {
        /* Getting the Curation Object */
        Curation curation = curationManager.getCuration(1L);
        /* Getting the CurationSource object */
        CurationSource curationSource = sourceManager.getSource(1L);

        CurationCache curationCache = new CurationCache();
        curationCache.setGuid("12313123ASASASS");
//        curationCache.setCuration(curation);
        curationCache.setCurationSource(curationSource);
        curationCache.setUpdatedDate(new Date());
        boolean exist = curationCacheManager.cacheExist(curationCache);
        if (exist) {
            logger.info("Curation Cache is Exists");
        } else {
            logger.info("Curation Cache isn't  Exists");
        }
    }


    /* Method to create Curation which
    is dependent object for CurationCache */
    private Curation createCuration() {
        Curation curation = new Curation();

        User user = null;
        try {
            user = userManager.getUser(1L);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        Assert.notNull(user, "User Object Not Found");

        // Demo Value for RetailerSite
        RetailerSite retailerSite = retailerManager.getRetailerSite(1L);
        Assert.notNull(retailerSite, "RetailerSite Object Not Found");

        curation.setActive(true);
        curation.setCreatedDate(new Date());
        curation.setDescription("Dell World App");
        curation.setModifiedDate(new Date());
        curation.setName("Dell World App 5");
        curation.setRetailerSite(retailerSite);
        curation.setCreatedBy(user);
        curation.setModifiedBy(user);
        curation = curationManager.saveCuration(curation);
        curation.getProperties().setProperty("app.name", "Dell World App5");
        curationManager.updateCuration(curation);
        Assert.notNull(curation, "Object Not Saved yet..");
        logger.info("Curation:= " + curation.getName() + " created successfully...");
        return curation;
    }

    /* Method for updating the Curation Object */
    private void updateCuration() {
        Long version = 0L;
        Curation curation = curationManager.getCuration(1L);
        curation.setName("Apple App World");
        version = curation.getVersion();
        curation.setVersion(version);
        curationManager.updateCuration(curation);
    }

    /* Creating CurationSource Object which is dependent
            object for CurationCache */
    private CurationSource createCurationSource() {
        CurationSource curationSource = new CurationSource();
        try {
            if (sourceManager != null) {

                User user = userManager.getUser(1L);
                Assert.notNull(user, "User Object Not Found");

                Curation curation = curationManager.getCuration(1L);
                Assert.notNull(curation, "CurationData Object Not Found");
                // DEMO VALUES FOR CURATION
//                curationSource.setCuration(curation);
                curationSource.setName("Test-Curation-I");
                curationSource.setDescription("Testing for Source Type Enum");
              //nxc  curationSource.setActive(true);
                curationSource.setSourceType(EntityConstants.CurationSourceType.RSS_FEED.getId());
                curationSource.setLimit(500);
                curationSource.setCreatedBy(user);
                curationSource.setModifiedBy(user);
                curationSource.setCreatedDate(new Date());
                curationSource.setModifiedDate(new Date());
                curationSource.getProperties().setProperty(EntityConstants.CurationSourceType.RSS_FEED.name(), "http://feeds.feedburner.com/delltechcenter.");
                curationSource = sourceManager.createSource(1l,curationSource);
                logger.info("CurationSourceObject craeted successfully->" + curationSource.getName());
            }

        } catch (Exception e) {
            logger.info("CurationSource object is not saved" + e.getMessage());
        }

        return curationSource;
    }

}
