package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * RSSFeedHandler test cases.
 *
 * @author Navin Raj Kumar G.S.
 */
public class RSSFeedHandlerTest extends CurationSourceHandlerTest {

    public RSSFeedHandlerTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getConstructorArgs() {
        Collection<Object[]> tests = new LinkedList<Object[]>();

        tests.add(new Object[]{getRssSource("feeds.feedburner.com/delltechcenter")});

        return tests;
    }

    @Override
    protected CurationSourceDataHandler getHandler(CurationSource source) {
        return new RSSFeedHandler();
    }

    protected static CurationSource getRssSource(String url) {
        return createDummySource(EntityConstants.CurationSourceType.RSS_FEED, "rss.url", url);
    }

}
