package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a twitter user's tweets returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterUserTweetsHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.TWITTER_USERNAME;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String username = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("twitter.username"));
        Assert.isTrue(!StringUtils.isEmpty(username),
                "twitter.username cannot be empty for source:=" + curationSource.getId());

        return "http://twitter.com/statuses/user_timeline/" + encode(username) + ".atom";
    }

}
