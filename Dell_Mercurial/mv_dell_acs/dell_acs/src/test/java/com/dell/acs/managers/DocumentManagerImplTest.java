package com.dell.acs.managers;

import com.dell.acs.DellTestCase;
import com.dell.acs.UserNotFoundException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.RetailerSite;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.util.Assert;
import org.apache.commons.io.FilenameUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
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
 @version $Revision: 1595 $, $Date:: 7/30/12 2:01 PM#$
 */
@RunWith(Parameterized.class)
public class DocumentManagerImplTest extends DellTestCase{

    // Required Parameter.
    private Collection<String> allowedImageFileExtensions = Arrays.asList("jpg", "gif", "png");

    private Long documentId;

    private Long userId;

    private Long retailerSiteId;

    private Integer docType;

    private String docName;

    /*    Constructor.
    The JUnit test runner will instantiate this class once for every
    element in the Collection returned by the method annotated with
    @Parameters.*/
    public DocumentManagerImplTest(Long documentId, Long userId, Long retailerSiteId, Integer docType, String docName) {

        this.documentId = documentId;
        this.userId = userId;
        this.retailerSiteId = retailerSiteId;
        this.docType = docType;
        this.docName = docName;
    }

/*  Test data generator.
       This method is called the the JUnit parameterized test runner and
       returns a Collection of Arrays.  For each Array in the Collection,
       each array element corresponds to a parameter in the constructor.
 */

    @Parameterized.Parameters
    public static Collection<Object[]> generateData() {

        // failure case: 0,0,0,2000,"Test Document" , due to document not exist.
        // failure case: 1,0,0,2000,"Test Document " , due to document not exist.
        // failure case: 1,1,0,2000,"Testing Document", due retailerSite not exist.
        // success case: 11,1,1,2000,"Document".
        // success case: 12,1,1,20001,"Image Document".
        // success case: 13,1,1,20002,"Article Document".
        // success case: 14,1,1,20003,"Video Document".
        // success case: 15,1,1,20004,"Link Document".

        Object[][] objectArray = new Object[][]{

                {0L, 0L, 0L, 2000, "Test Document"},
                {1L, 0L, 0L, 2000, "Test Document"},
                {1L, 1L, 0L, 2000, "Testing Document"},
                {11L, 1L, 1L, 2000, "Document"},
                {12L, 1L, 1L, 2001, "Image Document"},
                {13L, 1L, 1L, 2002, "Article Document"},
                {14L, 1L, 1L, 2003, "Video Document"},
                {15L, 1L, 1L, 2004, "Link Document"}

        };

        return Arrays.asList(objectArray);
    }

    @Test
    public void testAddDocument() throws UserNotFoundException {
        Document document = documentFactory(this.docType);
        Assert.notNull(document, "Unable to create the document");
        logger.info("Document created for type==>" + document.getType());
    }

    @Test
    public void testEditDocument() throws EntityNotFoundException {

        Document document = documentManager.getDocument(this.documentId);
        Assert.notNull(document, "Document[" + this.documentId + "] Not found ");
        if (this.docType == 2000) {
            document.setDescription("Document");
        } else if (this.docType == 2001) {
            document.setDescription("Image Document");
        } else if (this.docType == 2003) {
            document.setDescription("Video Document");
        } else if (this.docType == 2004) {
            document.setDescription("Link Document");
        } else if (this.docType == 2002) {
            document.setDescription("Article Document");
        }

        document.setStartDate(new Date());
        document.setEndDate(new Date());

        document.setImage("/cdn/1_dell/1_dell/document/" + document.getId() + "/dell_event.jpg");

        document = documentManager.saveDocument(document);

        logger.info("Document [" + document.getId() + "] updated successfully");
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
    public void testGetDocumentNameByID() {

        // Get the Image document Name.
        String documentName = documentManager.getDocumentNameByID(this.documentId, this.docType);
        if (this.docType == 2000) {
            Assert.notNull(documentName, "Document Not Found with id==>" + this.documentId);
        } else if (this.docType == 2001) {
            Assert.notNull(documentName, "Image Document Not Found with id==>" + this.documentId);
        } else if (this.docType == 2003) {
            Assert.notNull(documentName, "Video Document Not Found with id==>" + this.documentId);
        } else if (this.docType == 2004) {
            Assert.notNull(documentName, "Link Document Not Found with id==>" + this.documentId);
        } else if (this.docType == 2002) {
            Assert.notNull(documentName, "Article Document Not Found with id==>" + this.documentId);
        }
    }

    @Test
    public void testGetDocumentByRetailerSiteId() {
        Collection<Document> documents = documentManager.getDocuments(this.retailerSiteId, this.docType, null);
        Assert.notNull(documents, "Documents Not Found for the retailer site id==>" + this.retailerSiteId);
        logger.info("Documents Found for the particular retailerSite id==>" + this.retailerSiteId);
    }

    @Test
    public void testGetDocument(){
        Document document = documentManager.getDocument(this.documentId, this.docType);
        Assert.notNull(document, "Document["+this.documentId+"] Not Found ");
        logger.info("Document["+this.documentId+"] Found ");
    }

    @Test
    public void testDeleteDocument(){
       Document document = documentManager.getDocument(this.documentId, this.docType);
       Assert.notNull(document, "Unable to delete the document , document not exist");
       documentManager.deleteDocument(this.documentId);
       logger.info("Document["+document.getId()+"] deleted successfully");
    }

    public Document documentFactory(Integer type) throws UserNotFoundException {

        User user = userManager.getUser(this.userId);
        Assert.notNull(user, "User with id ==>" + this.userId + " not exist");

        RetailerSite retailerSite = retailerManager.getRetailerSite(this.retailerSiteId);
        Assert.notNull(retailerSite, "RetailerSite with id==>" + this.retailerSiteId + " not exist");

        Document document = new Document();
        if (this.docType == 2000) {
            document.setName("Document 01");
            document.setType(this.docType);
        } else if (this.docType == 2001) {
            document.setName("Image 01");
            document.setType(this.docType);
        } else if (this.docType == 2003) {
            document.setName("Video 01");
            document.setType(this.docType);
        } else if (this.docType == 2004) {
            document.setName("Link 01");
            document.setType(this.docType);
        } else if (this.docType == 2002) {
            document.setName("Article 01");
            document.setType(this.docType);
        }
        document.setDescription("Document created");
        document.setCreationDate(new Date());
        document.setModifiedDate(new Date());
        document.setCreatedBy(user);
        document.setModifiedBy(user);
        document.setRetailerSite(retailerSite);
        document.setStartDate(new Date());
        document.setEndDate(new Date());

        document = documentManager.saveDocument(document);
        logger.info("Document created successfully with id==>" + document.getId());

        return document;
    }

}
