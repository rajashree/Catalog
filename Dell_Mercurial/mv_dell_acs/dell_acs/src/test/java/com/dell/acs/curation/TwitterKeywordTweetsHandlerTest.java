package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

/**
 * TwitterKeywordTweetsHandler test cases.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterKeywordTweetsHandlerTest extends CurationSourceHandlerTest {

    public TwitterKeywordTweetsHandlerTest(final CurationSource curationSource) {
        super(curationSource);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getConstructorArgs() {
        Collection<Object[]> tests = new LinkedList<Object[]>();

        tests.add(new Object[]{
                createDummySource(EntityConstants.CurationSourceType.TWITTER_KEYWORD, "twitter.keyword", "dell")});

        return tests;
    }

    @Override
    protected CurationSourceDataHandler getHandler(CurationSource source) {
        return new TwitterKeywordTweetsHandler();
    }

}
