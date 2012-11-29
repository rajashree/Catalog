package com.dell.acs.managers;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Document;
import com.dell.acs.persistence.domain.RetailerSite;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

/**
 * @author Sandeep Heggi
 * @author $LastChangedBy: Sandeep $
 * @version $Revision: 3707 $, $Date:: 2012-07-16 4:56 PM#$
 */
public class ArticleManagerImplTest {

    protected static ApplicationContext applicationContext;
    protected static Logger logger = LoggerFactory.getLogger(ArticleManagerImplTest.class);

    DocumentManager documentManager = applicationContext.getBean("documentManagerImpl", DocumentManager.class);
    RetailerManager retailerManager = applicationContext.getBean("retailerManagerImpl", RetailerManager.class);

    Long articleID = 1L;

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/applicationContext.xml"});
    }

    /**
     * Test method to Create an Article.
     */
    @Test
    public void testAddArticle() {
        try {
            if (documentManager != null) {
                Document article = new Document();
                RetailerSite retailerSite = retailerManager.getRetailerSite(1L);
                article.setName("Test Article1");
                article.setDescription("Test description for Test Article1");
                article.setBody("<html><body>This is Test Article body<body></html>");
                article.setRetailerSite(retailerSite);
                //Set the type of Content i.e., Article
                article.setType(EntityConstants.Entities.ARTICLE.getId());
                documentManager.saveDocument(article);
                articleID = article.getId();
                logger.info("Successfully Created Article := " + article.getName());
            }
        } catch (EntityExistsException ex) {
            logger.info(ex.getMessage());
        }
    }

    /**
     * Test method to Update an Article.
     */
    @Test
    public void testUpdateArticle() {
        //Article ID to test update operation
        Long articleID = 5L;
        if (documentManager != null) {
            try {
                Document article = documentManager.getDocument(articleID);
                article.setName("Test Article- Updated");
                documentManager.saveDocument(article);
                logger.info("Article is updated successfully ->" + article.getName());
            } catch (EntityNotFoundException ex) {
                logger.info(ex.getMessage());
            } catch (EntityExistsException ex) {
                logger.info(ex.getMessage());
            }
        }
    }

    /**
     * Test method to get an Article
     */
    @Test
    public void testGetArticle() {
        // Article to test retrieve operation
        Long articleID = 5L;
        try {
            Document article = documentManager.getDocument(articleID);
            if (article != null) {
                logger.info("Article is := " + article.getName());
            }
        } catch (EntityNotFoundException ex) {
            logger.error("Article not found with ID:=" + articleID);
        }
    }

    /**
     * Test method to get all articles for a retailerSite
     */
    @Test
    public void testGetAllArticlesByRetailerSite() {
        //Retailer Site ID for which the articles should be retrieved.
        Long retailerSiteID = 1L;
        logger.info("Articles present are :");
        for (Document article : documentManager.getDocuments(retailerSiteID, EntityConstants.Entities.ARTICLE.getId(), null)) {
            logger.info(article.getName());
        }
    }

    /**
     * Test method to Delete an Article.
     */
    @Test
    public void testDeleteArticle() {
        //Article ID to test delete
        Long articleID = 5L;
        try {
            documentManager.deleteDocument(articleID);
            logger.info("Deleted article with ID := " + articleID);
        } catch (EntityNotFoundException e) {
            logger.info("Article Not Found with ID:= " + articleID);
        }
    }


}
