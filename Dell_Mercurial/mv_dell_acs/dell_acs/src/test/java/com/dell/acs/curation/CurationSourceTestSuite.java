package com.dell.acs.curation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** @author Navin Raj Kumar G.S. */

@RunWith(Suite.class)
@Suite.SuiteClasses({
        RSSFeedHandlerTest.class,
        DellWorldFeedsTest.class,
        TwitterHashTagTweetsHandlerTest.class,
        TwitterKeywordTweetsHandlerTest.class,
        TwitterUserTweetsHandlerTest.class,
        TwitterUserListTweetsHandlerTest.class,
        YoutubeChannelFeedHandlerTest.class,
        YoutubeUserFeedHandlerTest.class,
        YoutubeKeywordFeedHandlerTest.class,
        FacebookPageStatusHandlerTest.class,
        FacebookUserStatusHandlerTest.class
})
public class CurationSourceTestSuite {

}
