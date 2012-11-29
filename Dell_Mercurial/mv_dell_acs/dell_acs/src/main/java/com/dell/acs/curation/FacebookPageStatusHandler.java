package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a facebook page's statuses and returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class FacebookPageStatusHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.FACEBOOK_PAGE;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {

        String facebookPageId = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("facebook.page"));
        Assert.isTrue(!StringUtils.isEmpty(facebookPageId),
                "facebook.page cannot be empty for source:=" + curationSource.getId());
        return "http://www.facebook.com/feeds/page.php?format=rss20&id=" + encode(facebookPageId);

    }
}
