package com.dell.acs.managers;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.util.Assert;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 @author Ashish
 @author $LastChangedBy: Ashish $
 @version $Revision: 1595 $, $Date:: 7/30/12 2:01 PM#$ */
public class DocumentManagerTest {

    private static DocumentManager documentManager;

    private static RetailerManager retailerManager;

    private static UserManager userManager;

    private static ApplicationContext applicationContext;

    private static Logger logger = LoggerFactory.getLogger(DocumentManagerTest.class);

    public static Collection<String> allowedImageFileExtensions = null;

    // Global Entity Declaration.

    private static User user;

    private static RetailerSite retailerSite;


    @BeforeClass
    public static void setUP() {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
        documentManager = (DocumentManager) applicationContext.getBean("documentManagerImpl", DocumentManager.class);
        retailerManager = (RetailerManager)
                applicationContext.getBean("retailerManagerImpl", RetailerManager.class);

        userManager = (UserManager)
                applicationContext.getBean("userManagerImpl", UserManager.class);
        allowedImageFileExtensions =
                // Image file extension.
                Arrays.asList("jpg", "gif", "png");
        try {
            user = userManager.getUser(4L);
        } catch (Exception e) {
            logger.error("User not Found");
        }

        retailerSite = retailerManager.getRetailerSite(1L);
    }

    @Test
    public void testAddDocument() throws EntityExistsException {
        Document document = new Document();
        document.setName("Document 01");
        document.setDescription("Document not belong to image ,video,link");
        document.setStartDate(new Date());
        document.setEndDate(new Date());
        document.setType(2000);
        document = documentManager.saveDocument(document);
        if (document.getId() != null) {
            logger.info("Document Object Created successfully.");
            document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
                    "/cdn/1_dell/1_dell/document/" + document.getId() + "/thumbnailFile.png");
            documentManager.saveDocument(document);
            logger.info("Document Object propery updated successfully");
        }
    }

    @Test
    public void testEditDocument() throws EntityNotFoundException, EntityExistsException {
        Document document = documentManager.getDocument(30L);
        Assert.notNull(document, "Document Not found");
        document.setName("Updated Document 01");
        document.setDescription("Document not belong to image ,video,link");
        document.setStartDate(new Date());
        document.setEndDate(new Date());
        document.setType(2000);
        document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
                "/cdn/1_dell/1_dell/document/" + document.getId() + "/thumbnailFile.png");
        document = documentManager.saveDocument(document);
        logger.info("Updated successfully");
    }


    @Test
    public void testCreateImage() throws EntityExistsException {
        Document document = new Document();
        document.setName("Image Test Document With Properties value");
        document.setRetailerSite(retailerSite);
        document.setType(EntityConstants.Entities.IMAGE.getId());
        document.setDescription("Test Image Description");
        document = documentManager.saveDocument(document);
        logger.info("Image document successfully saved");
        document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
                "/cdn/1_dell/1_dell/document/" + document.getId() + "/thumbnailFile.png");
        document = documentManager.saveDocument(document);
        logger.info("Image document successfully saved with properties");
    }

    @Test
    public void testEditImage() throws EntityExistsException, EntityNotFoundException {

        Document document = documentManager.getDocument(24L);
        document.setDescription("Description is updated");
        document = documentManager.saveDocument(document);
        logger.info("Document Updated successfully");

    }

    @Test
    public void testDocumentImageExtension() {

        if (allowedImageFileExtensions
                .contains(FilenameUtils.getExtension("test.bmp").toLowerCase())) {
           logger.info("Supported file for document");
        } else {
            logger.info("File not supported for document");
        }
    }

    @Test
    public void testCreateVideo() throws EntityExistsException {
        Document document = new Document();
        document.setName("Video Test Document With Properties value");
        document.setRetailerSite(retailerSite);
        document.setType(EntityConstants.Entities.VIDEO.getId());
        document.setDescription("Test Image Description");
        document = documentManager.saveDocument(document);
        logger.info("Video document successfully saved");
        document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
                "/cdn/1_dell/1_dell/document/" + document.getId() + "/thumbnailFile.png");
        document = documentManager.saveDocument(document);
        logger.info("Video document successfully saved with properties");
    }

    @Test
    public void testEditVideo() throws EntityNotFoundException, EntityExistsException {

        Document document = documentManager.getDocument(25L);
        document.setDescription("Description is updated");
        document = documentManager.saveDocument(document);
        logger.info("Document Updated successfully");
    }


    @Test
    public void testCreateLink() throws EntityExistsException {
        Document document = new Document();
        document.setName("Link Test Document With Properties value");
        document.setRetailerSite(retailerSite);
        document.setType(EntityConstants.Entities.LINK.getId());
        document.setDescription("Test Image Description");
        document = documentManager.saveDocument(document);
        logger.info("Link document successfully saved");
        document.getProperties().setProperty(DocumentManager.DOCUMENT_THUMBNAIL_PROPERTY,
                "/cdn/1_dell/1_dell/document/" + document.getId() + "/thumbnailFile.png");
        document = documentManager.saveDocument(document);
        logger.info("Link document successfully saved with properties");
    }

    @Test
    public void testEditLink() throws EntityNotFoundException, EntityExistsException{
        Document document = documentManager.getDocument(26L);
        document.setDescription("Description is updated");
        document = documentManager.saveDocument(document);
        logger.info("Document Updated successfully");
    }

    @Test
    public void testGetDocumentNameByID() {

        Long videoId = 22L;
        Long imageId = 23L;
        Long linkId = 24L;

        // Get the Image document Name.
        String imageName = documentManager.getDocumentNameByID(imageId, 2001);
        Assert.notNull(imageName, "Image Document Not Found");
        logger.info("Image document Found");
        // Get the Link document Name.
        String linkName = documentManager.getDocumentNameByID(linkId, 2004);
        Assert.notNull(imageName, "Link Document Not Found");
        logger.info("Link document Found");
        // Get the Video document Name.
        String videoName = documentManager.getDocumentNameByID(videoId, 20043);
        Assert.notNull(imageName, "Video Document Not Found");
        logger.info("Video document Found");

    }

}
