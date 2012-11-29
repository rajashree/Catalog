package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.synd.SyndEntry;
import org.springframework.util.Assert;

/**
 * Extracts data from a youtube user feed returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class YoutubeUserFeedHandler extends RSSFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.YOUTUBE_USERNAME;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String username = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("youtube.username"));
        Assert.isTrue(!StringUtils.isEmpty(username),
                "youtube.username cannot be empty for source:=" + curationSource.getId());

        return "https://gdata.youtube.com/feeds/api/users/" + encode(username) + "/uploads?orderby=updated&alt=atom&v=2";
    }

    @Override
    protected void processItem(final SyndEntry source, final CurationCache cacheItem,
            final MediaEntryModule mediaEntryModule) {
        super.processItem(source, cacheItem, mediaEntryModule);
    }
}
