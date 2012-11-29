package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts tweets based on hashtags. returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterHashTagTweetsHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.TWITTER_HASHTAG;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String hashTag = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("twitter.hashtag"));
        Assert.isTrue(!StringUtils.isEmpty(hashTag),
                "twitter.hashtag cannot be empty for source:=" + curationSource.getId());
        return "http://search.twitter.com/search.atom?q=%23" + encode(hashTag);
    }
}
