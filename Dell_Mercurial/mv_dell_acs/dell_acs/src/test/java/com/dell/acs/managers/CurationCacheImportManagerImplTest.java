package com.dell.acs.managers;

import com.dell.acs.DellTestCase;
import com.dell.acs.ObjectLockedException;
import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.Curation;
import com.dell.acs.persistence.domain.CurationSource;
import com.dell.acs.persistence.domain.User;
import com.sourcen.core.HandlerNotFoundException;
import com.sourcen.core.util.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/** @author Navin Raj Kumar G.S. */
public class CurationCacheImportManagerImplTest extends DellTestCase {

    //
    //        Manager tests.
    //

    /**
     * check for a fresh curationSource.
     *
     * @throws Exception
     */
    @Test
    public void testGetSourceToUpdate() throws Exception {
        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
            put("rss.url", "http://feeds.feedburner.com/delltechcenter");
        }});

        // expected to get the entity here.
        Assert.notNull(curationSourceManager.getSourceToUpdateCache(),
                "Unable to find curationSource object to update.");

        // expected to NOT get anything here.
       //curationSource.setActive(false);
        curationSourceManager.updateSource(curationSource);
        Assert.isNull(curationSourceManager.getSourceToUpdateCache(), "Found a source when its not active as well.");

        // expected to NOT get anything here.
       // curationSource.setActive(true);
        curationSource.setLockedThread("dummyLock");
        curationSourceManager.updateSource(curationSource);
        Assert.isNull(curationSourceManager.getSourceToUpdateCache(), "Found a source when its not active as well.");

        // expected to get the object here.
        curationSource.setLockedThread(null);
        curationSourceManager.updateSource(curationSource);
        Assert.notNull(curationSourceManager.getSourceToUpdateCache(),
                "Unable to find curationSource object to update.");

        // expected to get the object here.
        curationSource.setLockedThread(null);
        curationSource.setExecutionStatus(EntityConstants.ExecutionStatus.DONE.getId());
        curationSourceManager.updateSource(curationSource);
        Assert.notNull(curationSourceManager.getSourceToUpdateCache(),
                "Unable to find curationSource object to update.");

    }

    @Test
    public void testUpdateCache() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
            put("rss.url", "http://feeds.feedburner.com/delltechcenter");
        }});

        // should execute without any error.
        curationSourceManager.updateCache(curationSource);

    }

    @Test(expected = ObjectLockedException.class)
    public void testUpdateCacheFailure() throws Exception {

        // Add the test object in the curation table.
        CurationSource curationSource = addCurationSource("rss-test",
                EntityConstants.CurationSourceType.RSS_FEED.getId(), new HashMap<String, String>() {{
            put("rss.url", "http://feeds.feedburner.com/delltechcenter");
        }});
        curationSource.setLockedThread("dummyLock");
        curationSourceManager.updateSource(curationSource);

        // should should fail.
        curationSourceManager.updateCache(curationSource);
    }

    @Test(expected = HandlerNotFoundException.class)
    public void testUpdateCacheFailureOnInvalidSource() throws Exception {

        // Add the test object in the curation table.
        // set a custom invalid source so it fails.
        CurationSource curationSource = addCurationSource("rss-test", 234, new HashMap<String, String>() {{
            put("rss.url", "http://feeds.feedburner.com/delltechcenter...");
        }});
        curationSource.setLockedThread("dummyLock");
        curationSourceManager.updateSource(curationSource);

        // should should fail with HandlerNotFoundException.
        curationSourceManager.updateCache(curationSource);
    }

    //
    //
    //          helper methods.
    //
    //

    public CurationSource addCurationSource(String sourceName, Integer type, Map<String, String> properties)
            throws Exception {
        User user = userManager.getUser("admin");
        Assert.notNull(user, "User Object Not Found");

        Curation curation = curationManager.getCuration(1L);
        Assert.notNull(curation, "CurationData Object Not Found");
        CurationSource curationSource = new CurationSource(sourceName, type, user);
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            curation.getProperties().setProperty(entry.getKey(), entry.getValue());
        }
        return curationSourceManager.createSource(1l,curationSource);
    }

}
