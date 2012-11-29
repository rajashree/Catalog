package com.dell.acs.curation;

import com.dell.acs.content.EntityConstants;
import com.dell.acs.persistence.domain.CurationCache;
import com.dell.acs.persistence.domain.CurationSource;
import com.sourcen.core.util.StringUtils;
import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Extracts data from a RSS feed and returns Items to be cached.
 *
 * @author Navin Raj Kumar G.S.
 */
public class RSSFeedHandler extends AbstractCurationSourceDataHandler {

    protected static final Logger logger = LoggerFactory.getLogger(RSSFeedHandler.class);

    private static final String[] textTypes = new String[]{"html", "text/html", "plain", "text/plain"};

    @Override
    protected EntityConstants.CurationSourceType getHandlerType() {
        return EntityConstants.CurationSourceType.RSS_FEED;
    }

    @Override
    public Collection<CurationCache> getItems(final CurationSource curationSource) throws ExecutorException {
        // prechecks.
        String rssUrl = getRssUrl(curationSource);
        Long sourceId = curationSource.getId();

        if (rssUrl == null || rssUrl.trim().isEmpty()) {
            throw new ExecutorException("Unable to process source:=" + sourceId + " as feed URL was empty.");
        }

        if (!StringUtils.startsWithAny(rssUrl, new String[]{"http://", "https://"})) {
            logger.warn("the rss.url for source:=" + sourceId +
                    " does not contain a protocol prefix. adding http:// to the url to process.");
            rssUrl = "http://" + rssUrl;
        }

        Collection<CurationCache> result = null;

        try {
            logger.info("trying to fetch feed:=" + rssUrl);
            // get the feed data.
            URL feedUrl = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> sourceItems = feed.getEntries();

            // we dont really care about the order here.
            result = new ArrayList<CurationCache>();

            // keep the date consistent for all imported entries.
            Date importedDate = new Date();

            for (SyndEntry source : sourceItems) {
                CurationCache cacheItem = new CurationCache();

                cacheItem.setCurationSource(curationSource);
                cacheItem.setGuid(source.getUri());
                cacheItem.setTitle(source.getTitle());
                cacheItem.setLink(source.getLink());

                SyndContent description = source.getDescription();
                if (description != null) {
                    cacheItem.setDescription(description.getValue());
                }

                // body is what will be displayed. later we can have processors to handle the "body"
                // field that can update/alter the value of the item based on the source field.

                List<SyndContent> contents = source.getContents();
                String sourceContent = null;
                if (contents == null || contents.isEmpty()) {
                    if (description != null) {
                        sourceContent = cacheItem.getDescription();
                    }
                } else {
                    if (contents.size() == 1) {
                        sourceContent = contents.get(0).getValue();
                    } else {
                        // find the appropriate text/html or html or text/plain content.
                        for (SyndContent content : contents) {
                            String type = content.getType();
                            if (type != null && StringUtils.indexOf(textTypes, type) > -1) {
                                sourceContent = content.getValue();
                            }
                        }
                        // if we didnt find any appropriate ones set the first one.
                        if (sourceContent == null) {
                            sourceContent = contents.get(0).getValue();
                        }
                    }
                }

                if (sourceContent == null) {
                    sourceContent = cacheItem.getDescription();
                }

                cacheItem.setBody(sourceContent);
                cacheItem.setSource(sourceContent);

                cacheItem.setStatus(EntityConstants.Status.PUBLISHED.getId());

                if (source.getPublishedDate() != null) {
                    cacheItem.setPublishedDate(source.getPublishedDate());
                } else {
                    // if publishedDate is null, this is not really a proper RSS :(
                    cacheItem.setPublishedDate(importedDate);
                }

                if (source.getUpdatedDate() == null) {
                    cacheItem.setUpdatedDate(cacheItem.getPublishedDate());
                } else {
                    cacheItem.setUpdatedDate(source.getUpdatedDate());
                }

                cacheItem.setImportedDate(importedDate);

                processItem(source, cacheItem);

                // check if media is enabled.
                try {
                    MediaEntryModule mediaEntryModule = (MediaEntryModule) source.getModule(MediaEntryModule.URI);
                    if (mediaEntryModule != null) {
                        processItem(source, cacheItem, mediaEntryModule);
                    }
                } catch (Exception e) {
                    // ignore
                }


                result.add(cacheItem);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // this will cause the result to contain partial items. and its fine as long
            // as we verify and insert into DB.
        }

        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }

    // hook to modify or add additional details to the item.
    protected void processItem(final SyndEntry source, final CurationCache cacheItem) {
        // none in the rss reader.
    }

    // hook to alter any media entires.
    protected void processItem(final SyndEntry source, final CurationCache cacheItem,
            final MediaEntryModule mediaEntryModule) {

    }

    protected String getRssUrl(final CurationSource curationSource) {
        return StringUtils.trimBothEnds(curationSource.getProperties().getProperty("rss.url"));
    }

    protected static String encode(String argument) {
        try {
            return URLEncoder.encode(argument, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
        return argument;
    }

}
