package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import org.springframework.util.Assert;

/**
 * Extracts data from a youtube keyword feed returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class YoutubeKeywordFeedHandler extends YoutubeUserFeedHandler {

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.YOUTUBE_KEYWORD;
    }

    @Override
    protected String getRssUrl(final CurationSource curationSource) {
        String keyword = StringUtils.trimBothEnds(curationSource.getProperties().getProperty("youtube.keyword"));
        Assert.isTrue(!StringUtils.isEmpty(keyword),
                "youtube.channel cannot be empty for source:=" + curationSource.getId());


        return "https://gdata.youtube.com/feeds/api/videos?q=" + encode(keyword) + "&orderby=updated&alt=atom&v=2";
    }



}
