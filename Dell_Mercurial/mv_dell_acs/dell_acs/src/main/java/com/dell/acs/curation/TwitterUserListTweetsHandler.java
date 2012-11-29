package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a twitter userlist's tweets returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterUserListTweetsHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.TWITTER_LIST;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String username = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("twitter.username"));
        String listname = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("twitter.listname"));
        Assert.isTrue(!StringUtils.isEmpty(username),
                "twitter.username cannot be empty for source:=" + curationSource.getId());
        Assert.isTrue(!StringUtils.isEmpty(listname),
                "twitter.listname cannot be empty for source:=" + curationSource.getId());

        return "http://api.twitter.com/1/" + encode(username) + "/lists/" + encode(listname) + "/statuses.atom";
    }

}
