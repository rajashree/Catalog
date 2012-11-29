package com.dell.acs.persistence.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** @author Navin Raj Kumar G.S. */
public enum CurationSourceType {

    TWITTER_USERNAME(100, "twitter.username"),
    TWITTER_LIST(101, "twitter.username", "twitter.listname"),
    TWITTER_HASHTAG(102, "twitter.hashtag"),
    TWITTER_KEYWORD(103, "twitter.keyword"),

    FACEBOOK_USERNAME(200, "facebook.username"),
    FACEBOOK_PAGE(201, "facebook.page"),

    RSS_FEED(300, "rss.url"),

    YOUTUBE_USERNAME(400, "youtube.username"),
    YOUTUBE_CHANNEL(401, "youtube.channel"),
    YOUTUBE_KEYWORD(402, "youtube.keyword"),

    USER_GENERATED(1000, null);

    private int id;

    private Set<String> properties;

    private CurationSourceType(int id, String... properties) {
        this.id = id;
        if (properties == null || properties.length == 0) {
            this.properties = Collections.emptySet();
        } else {
            HashSet<String> possibleProperties = new HashSet<String>(properties.length);
            Collections.addAll(possibleProperties, properties);
            this.properties = Collections.unmodifiableSet(possibleProperties);
        }
    }

    public int getId() {
        return id;
    }

    public Set<String> getProperties() {
        return properties;
    }
}
