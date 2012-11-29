package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * TwitterHashTagTweetsHandlerTest test cases.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterHashTagTweetsHandlerTest extends CurationSourceHandlerTest {

    public TwitterHashTagTweetsHandlerTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getConstructorArgs() {
        Collection<Object[]> tests = new LinkedList<Object[]>();

        tests.add(new Object[]{
                createDummySource(EntityConstants.CurationSourceType.TWITTER_HASHTAG, "twitter.hashtag", "dell")});

        return tests;
    }

    @Override
    protected CurationSourceDataHandler getHandler(CurationSource source) {
        return new TwitterHashTagTweetsHandler();
    }

}
