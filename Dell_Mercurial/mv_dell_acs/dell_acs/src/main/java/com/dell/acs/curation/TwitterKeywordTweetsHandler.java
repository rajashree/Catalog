package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a twitter keyword's tweets source returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class TwitterKeywordTweetsHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.TWITTER_KEYWORD;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String keyword = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("twitter.keyword"));
        Assert.isTrue(!StringUtils.isEmpty(keyword),
                "twitter.keyword cannot be empty for source:=" + curationSource.getId());
        return "http://search.twitter.com/search.atom?q=" + encode(keyword);
    }

}
