package com.dell.acs.managers;


import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.User;
import com.dell.acs.persistence.repository.CurationSourceRepository;
import com.sourcen.core.util.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 @author Mahalakshmi
 @author $LastChangedBy: mmahalaxmi $
 @version $Revision: 1595 $, $Date:: 7/19/12  3:01 PM#$ */
public class CurationSourceManagerImplTest {

    protected static ApplicationContext applicationContext;

    protected static Logger logger = LoggerFactory.getLogger(CurationSourceManagerImplTest.class);

    private static CurationSourceRepository sourceRepository;

    private static CurationSourceManager sourceManager;

    private static UserManager userManager;

    private static RetailerManager retailerManager;

    private static CurationManager curationManager;

    @BeforeClass
    public static void setUp() throws Exception {
        applicationContext = new ClassPathXmlApplicationContext(new String[]{"/spring/test/applicationContext-test.xml"});
        sourceRepository =
                applicationContext.getBean("curationSourceRepositoryImpl", CurationSourceRepository.class);
        sourceManager =
                applicationContext.getBean("curationSourceManagerImpl", CurationSourceManager.class);
        userManager =
                applicationContext.getBean("userManagerImpl", UserManager.class);
        retailerManager =
                applicationContext.getBean("retailerManagerImpl", RetailerManager.class);
        curationManager =
                applicationContext.getBean("curationManagerImpl", CurationManager.class);

    }


    @Test
    public void testUpdateCurationSource() throws EntityExistsException {
        // NOT SUPPORTED AS OF NOW

//        CurationSource curationSource = sourceManager.getSource(1L);
//        curationSource.setDescription("RSS Curation Source ");
//        curationSource.setActive(false);
//        //  curationSource.setSourceType(400);           for failure case
//        //CurationSource curationSourceUpdated = sourceManager.updateCurationSource(curationSource);
//        logger.info("Upadted ->" + curationSourceUpdated.getName());
    }


    @Test
    public void testCreateSource() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
                    put("rss.url", "http://feeds.feedburner.com/delltechcenter");
                }});

        // should execute without any error.
        sourceManager.createSource( 1l,curationSource);

    }

    @Test
    public void testCreateNewSourceForExisting() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
                    put("rss.url", "http://en.community.dell.com/rss.aspx?Tags=Healthcare");
                }});

        // should execute without any error.
        sourceManager.createSource( 1l,curationSource);

    }

    @Test(expected = EntityExistsException.class)
    public void testCreateSourceForExisting() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
                    put("rss.url", "http://feeds.feedburner.com/delltechcenter");
                }});

        // should execute without any error.
        sourceManager.createSource( 1l,curationSource);

    }

    @Test
    public void testCreateSourceForNewCuration() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
                    put("rss.url", "http://feeds.feedburner.com/delltechcenter");
                }});

        // should execute without any error.
        sourceManager.createSource( 2l,curationSource);

    }


    /**
     check for a active curationSource AND couration.

     @throws Exception
     */


    @Test
    public void testActiveGetSources() throws EntityNotFoundException {
        Collection<CurationSource> activeCurationSources = sourceManager.getSources(1L);
        assertNotNull("list of CurationSource", activeCurationSources);
        assertTrue("List of Active CurationSource are retrived", activeCurationSources.size() > 0);

        Collection<CurationSource> activeCurationSource = sourceManager.getSource();
        assertNotNull("list of CurationSource", activeCurationSource);
        assertTrue(activeCurationSource.size() > 0);
    }

    @Test
    public void testActiveGetCurationSource() throws EntityNotFoundException {
        CurationSource source = sourceManager.getSource(24L, 2l);
        assertNotNull("CurationSource", source);

        CurationSource curationSources = sourceManager.getSource(24L);
        assertNotNull("CurationSource", curationSources);


    }


    @Test
    public void testActiveGetCurations() throws EntityNotFoundException {
        Collection<Curation> curations = sourceManager.getCurations(24L);
        assertNotNull("list of curations", curations);
        assertTrue("List of Active Curation are retrived", curations.size() > 0);


        Collection<Curation> activeCurations = sourceManager.getCurations();
        assertNotNull("list of curations", activeCurations);
        assertTrue(activeCurations.size() > 0);

    }

    /**
     check for a Inactive curationSource AND couration.

     @throws Exception
     */


    @Test(expected = EntityNotFoundException.class)
    public void testInActiveGetSources() throws EntityNotFoundException {

        Collection<CurationSource> sources = sourceManager.getSources(1L);
        assertNotNull("Empty list of CurationSource", sources);
        assertTrue(sources.size() < 0);

        Collection<CurationSource> activeCurationSource = sourceManager.getSource();
        assertNotNull("Empty list of CurationSource", activeCurationSource);
        assertTrue(activeCurationSource.size() < 0);

    }

    @Test(expected = EntityNotFoundException.class)
    public void testInActiveGetCurationSource() throws EntityNotFoundException {

        CurationSource source = sourceManager.getSource(27L, 2l);
        assertNull("CurationSource", source);

        CurationSource curationSources = sourceManager.getSource(29L);
        assertNull("CurationSource", curationSources);

    }


    @Test(expected = EntityNotFoundException.class)
    public void testInActiveGetCurations() throws EntityNotFoundException {

        Collection<Curation> curations = sourceManager.getCurations(5L);
        assertNull("Empty list of curations", curations);
        assertFalse(curations.size() < 0);

        Collection<Curation> activeCurations = sourceManager.getCurations();
        assertNotNull("Empty list of curations", activeCurations);
        assertFalse(activeCurations.size() < 0);


    }


    @Test(expected = EntityNotFoundException.class)
    public void testInActiveGetSource() throws EntityNotFoundException {

        Collection<CurationSource> sources = sourceManager.getSources(1L);
        assertNotNull("Empty list of CurationSource", sources);
        assertTrue(sources.size() < 0);

        Collection<CurationSource> activeCurationSource = sourceManager.getSource();
        assertNotNull("Empty list of CurationSource", activeCurationSource);
        assertFalse(activeCurationSource.size() < 0);

    }


    @Test
    public void testDeleteExistingSource() {
        sourceManager.removeSourceMapping(25L, 1L);

    }

    //helper method
    public CurationSource addCurationSource(String sourceName, Integer type, Map<String, String> properties)
            throws Exception {
        User user = userManager.getUser("dellmppmgr");
        Assert.notNull(user, "User Object Not Found");

        Curation curation = curationManager.getCuration(1L);
        Assert.notNull(curation, "CurationData Object Not Found");

        CurationSource curationSource = new CurationSource(sourceName, type, user);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            curation.getProperties().setProperty(entry.getKey(), entry.getValue());
        }
        return curationSource;
    }


}
