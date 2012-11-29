package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a youtube channel returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class YoutubeChannelFeedHandler extends YoutubeUserFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.YOUTUBE_CHANNEL;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String channel = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("youtube.channel"));
        Assert.isTrue(!StringUtils.isEmpty(channel),
                "youtube.channel cannot be empty for source:=" + curationSource.getId());

        return "https://gdata.youtube.com/feeds/api/users/" + encode(channel) + "/uploads?orderby=updated&alt=atom&v=2";
    }

}
