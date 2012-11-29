package com.dell.acs.managers;

import com.dell.acs.CurationContentAlreadyExistsException;
import com.dell.acs.DellTestCase;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.*;
import com.dell.acs.persistence.repository.RetailerSiteRepository;
import com.sourcen.core.util.Assert;
import com.sourcen.core.util.beans.ServiceFilterBean;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Date;

/**
 * @author Adarsh kumar
 * @author $LastChangedBy: Adarsh $
 */
public class CurationContentManagerImplTest extends DellTestCase {

    protected static Logger logger = LoggerFactory.getLogger(CurationContentManagerImplTest.class);

    private static CurationContentManager curationContentManager =
            applicationContext.getBean("curationContentManagerImpl", CurationContentManager.class);

    private static CurationManager curationManager =
            applicationContext.getBean("curationManagerImpl", CurationManager.class);

    private static RetailerManager retailerManager =
            applicationContext.getBean("retailerManagerImpl", RetailerManager.class);

    private static UserManager userManager =
            applicationContext.getBean("userManagerImpl", UserManager.class);

    private static CurationSourceManager curationSourceManager =
            applicationContext.getBean("curationSourceManagerImpl", CurationSourceManager.class);

    private static CurationSourceManager curationSourceManagerImpl =
            applicationContext.getBean("curationSourceManagerImpl", CurationSourceManager.class);
    private static DocumentManager documentManagerImpl =
            applicationContext.getBean("documentManagerImpl", DocumentManager.class);

    private static CurationCacheManager curationCacheManager =
            applicationContext.getBean("curationCacheManagerImpl", CurationCacheManager.class);

    private static RetailerSiteRepository retailerSiteRepository =
            applicationContext.getBean("retailerSiteRepositoryImpl", RetailerSiteRepository.class);


    /**
     * Test to create a Curation with ContentSource
     */
    @Test
    public void testAll() {
        /* testCreateCuration(1L); */
        /* testCreateCurationContentWithoutContentSource(5L); */
        /* testUpdateCurationContent(2L); */
        /* testDeleteCurationContent(2L); */
        /*  testGetCurationContent(2L);*/
    }


    @Test
    public void testGetCurationContentForCategory() {
        //Collection<CurationContent> contents = curationContentManager.getCurationContents(17L);
        Collection<CurationContent> contents = curationContentManager.getContents(new ServiceFilterBean(), "categoryID", 16L);
        logger.info("Found - " + contents.size() + " contents for category - " + 17);
    }


    @Test
    public void testAddDocumentAsCurationContent() {
        try {
            Long curationID = 3L;

            Curation curation = curationManager.getCuration(curationID);
            Assert.notNull(curation, "Curation Object Not Found");

            User user = userManager.getUser("admin");

            Assert.notNull(user, "User Object Not Found");
            //Document document = documentManagerImpl.getDocument(1L);
            CurationContent curationContent2 = new CurationContent();
            curationContent2.setCuration(curation);
            curationContent2.setCacheContent(1L);
            curationContent2.setStatus(EntityConstants.Status.PUBLISHED.getId());
            curationContent2.setType(EntityConstants.CurationSourceType.DOCUMENT.getId());
            curationContent2.setCreatedBy(user);
            curationContent2.setModifiedBy(user);
            curationContent2.setCreatedDate(new Date());
            curationContent2.setModifiedDate(new Date());

            curationContentManager.createContent(curationContent2);
        } catch (Exception ex) {

        }
    }

    @Test
    public void testAddCurationContentItem() {
        // Long curationID, Long curationContentCacheID, Long categoryID, Integer type, User user
        //curationContentManager.addCurationItem()
    }


    @Test
    public void testCreateExternalCurationContent() {
        try {
            // Parameters that are passed ro CS from the Curation APP
            Long curationID = 3L;
            Long curationContentCacheID = -1L;
            EntityConstants.CurationSourceType type = EntityConstants.CurationSourceType.RSS_FEED;
            Long categoryID = 17L;

            Curation curation = curationManager.getCuration(curationID);
            Assert.notNull(curation, "Curation Object Not Found");

            User user = userManager.getUser("admin");

            Assert.notNull(user, "User Object Not Found");
            type = EntityConstants.CurationSourceType.RSS_FEED;
            for (int i = 1; i <= 5; i++) {
                curationContentCacheID = Long.valueOf(i);
                CurationCache curationCache = curationCacheManager.getCache(curationContentCacheID);
                Assert.notNull(curationCache, "Curation Cache Object Not Found");
                CurationContent curationContent1 = new CurationContent();
                curationContent1.setCuration(curation);
                curationContent1.setCacheContent(curationCache.getId());
                curationContent1.setStatus(EntityConstants.Status.PUBLISHED.getId());
                curationContent1.setCreatedBy(user);
                curationContent1.setModifiedBy(user);
                curationContent1.setType(type.getId());
                curationContent1.setCreatedDate(new Date());
                curationContent1.setModifiedDate(new Date());
                curationContentManager.createContent(curationContent1);
            }
        } catch (CurationContentAlreadyExistsException e) {
            e.printStackTrace();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to create a Curation without ContentSource
     */
    @Test
    public void testCreateCurationContentWithoutContentSource() {
        try {
            Long count = 1L;

            Curation curation = curationManager.getCuration(count);
            Assert.notNull(curation, "Curation Object Not Found");

            CurationSource curationSource = curationSourceManager.getSource(count);
            Assert.notNull(curationSource, "Curation Source Object Not Found");

            User user = userManager.getUser("admin");
            Assert.notNull(user, "User Object Not Found");

            /* Creating Curation Content with CurationCache */
            CurationCache curationCache = curationCacheManager.getCache(count);
            Assert.notNull(curationCache, "Curation Cache Object Not Found");

            CurationContent curationContent = new CurationContent();
            curationContent.setCacheContent(curationCache.getId());
            curationContent.setCreatedBy(user);
            curationContent.setModifiedBy(user);

            curationContentManager.createContent(curationContent);
        } catch (CurationContentAlreadyExistsException e) {
            e.printStackTrace();
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test to update the CurationContent
     */
    @Test
    public void testUpdateCurationContent() {
        long curationContentId = 1L;
        CurationContent curationContent = curationContentManager.getContent(curationContentId);
        Assert.notNull(curationContent, "Curation Content Object Not Found");
        curationContent.setEdited(true);
        curationContentManager.updateContent(curationContent);
    }

    /**
     * Get the Curation Content given the ID
     */
    @Test
    public void testGetCurationContent() {
        Long curationContentID = -1L;
        //Curation Content ID to retrieve
        CurationContent curationContent = curationContentManager.getContent(curationContentID);
        Assert.notNull(curationContent, "Curation Content Object Not Found");
        logger.info("Curation Content is := " + curationContent.toString());
    }

    /**
     * Test to delete a Curation
     */
    @Test
    public void testDeleteCurationContent() {
        Long curationContentID = -1L;
        //Curation Content ID to delete
        curationContentManager.deleteContent(curationContentID);
        logger.info("Successfully deleted the curation content with ID :=" + curationContentID);
    }


    /* Method to create Curation which
        is dependent object for CurationCache */
    @Test
    public void createCuration() {
        Long countNumber = 1L;
        Curation curation = null;
        try {
            curation = curationManager.getCuration(countNumber);


            User user = userManager.getUser("admin");
            Assert.notNull(user, "User Object Not Found");

            // Demo Value for RetailerSite
            RetailerSite retailerSite = retailerManager.getRetailerSitebyName("dell");
            Assert.notNull(retailerSite, "RetailerSite Object Not Found");

            curation.setActive(true);
            curation.setCreatedDate(new Date());
            curation.setDescription("Dell World App" + countNumber);
            curation.setModifiedDate(new Date());
            curation.setName("Dell World App 5" + countNumber);
            curation.setRetailerSite(retailerSite);
            curation.setCreatedBy(user);
            curation.setModifiedBy(user);
            curation = curationManager.saveCuration(curation);
            curation.getProperties().setProperty("app.name", "Dell World App5" + countNumber);
            curationManager.updateCuration(curation);
            Assert.notNull(curation, "Object Not Saved yet..");
            logger.info("Curation:= " + curation.getName() + " created successfully...");
        } catch (Exception e) {
            logger.error(e.getMessage());
            if (curation == null) {
                curation = new Curation();
            }
        }
    }

    /* Creating CurationSource Object which is dependent
            object for CurationCache */
    @Test
    public void createCurationSource() {
        Long curationID = 3L;

        try {
            if (curationSourceManagerImpl != null) {
                User user = userManager.getUser("admin");

                CurationSource curationSource = new CurationSource("Twitter",
                        EntityConstants.CurationSourceType.TWITTER_LIST.getId(), user);
                //Curation curation = curationManager.getCuration(curationID);
                curationSource.setDescription("Twitter Source Type Enum");
                //curationSource.setActive(true);
                curationSource.setLimit(500);
                curationSource.getProperties().setProperty(EntityConstants.CurationSourceType.TWITTER_LIST.name(), "http://twitter.com/users/delltechcenter");
                curationSource = curationSourceManagerImpl.createSource(1L, curationSource);
                logger.info("CurationSourceObject created successfully->" + curationSource.getName());
            }

        } catch (Exception e) {
            logger.info("CurationSource object is not saved" + e.getMessage());
        }

    }

    /* Creating Curation Cache Object */
    @Test
    public void createCurationCache() throws Exception {
        Long curationID = 3L;
        Long curationSourceID = 2L;

        /* Getting the Curation Object */
        Curation curation = curationManager.getCuration(curationID);
        Assert.notNull(curation, "Curation Object Not Found");

        /* Getting the CurationSource object */
        CurationSource curationSource = curationSourceManager.getSource(curationSourceID);
        Assert.notNull(curationSource, "Curation Source Object Not Found");

        // Generate 10 records for the curationSource 1

        for (int i = 0; i < 5; i++) {
            CurationCache curationCache = new CurationCache();
            curationCache.setBody("Twitter " + i + " tweet");
            curationCache.setDescription("Tweet" + i + " Cache Description");
            curationCache.setTitle("Tweet - " + i);
            curationCache.setGuid("TweetEEEEE" + i);
            curationCache.setImportedDate(new Date());
            curationCache.setSource("Tweet Source" + i);
            curationCache.setPublishedDate(new Date());
            curationCache.setCurationSource(curationSource);
            curationCache.setUpdatedDate(new Date());
            curationCacheManager.saveCache(curationCache);
        }
    }

    /* Crating document */
    @Test
    public void createDocument() {
        try {
            RetailerSite retailerSite = retailerSiteRepository.getByName("dell");
            User user = userManager.getUser("admin");
            Document document = documentManagerImpl.getDocument(1L);
            if (document == null) {
                document = new Document();
            }
            document.setBody(" Document Body");
            document.setDescription("Document Description");
            document.setImage("Document Image");
            document.setDocument("PDF Document");
            document.setName("Document ");
            document.setCreatedBy(user);
            document.setModifiedBy(user);
            document.setStatus(EntityConstants.Status.PUBLISHED.getId());
            document.setType(1);
            document.setCreationDate(new Date());
            document.setEndDate(new Date());
            document.setRetailerSite(retailerSite);
            documentManagerImpl.saveDocument(document);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        } catch (EntityExistsException e) {
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

}
