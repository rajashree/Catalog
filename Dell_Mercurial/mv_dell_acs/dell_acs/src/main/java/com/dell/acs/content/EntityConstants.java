/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.dell.acs.content;

import com.dell.acs.persistence.domain.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Samee K.S
 * @author : sameeks $
 * @version : 0 $, :: 2012-03-07 09:56:40#$
 */

public class EntityConstants {

    public static enum Entities {

        CAMPAIGN("CAMPAIGN", 1000), CAMPAIGN_CATEGORY("CAMPAIGN_CATEGORY", 1001),
        DOCUMENT("DOCUMENT", 2000), IMAGE("IMAGE", 2001), ARTICLE("ARTICLE", 2002), VIDEO("VIDEO", 2003), LINK("LINK", 2004),
        EVENT("EVENT", 3000),
        CURATION("CURATION", 5000),
        PRODUCT("PRODUCT", 6000),
        USER("USER",7000),
        API_KEY("API_KEY",8000);


        private String value;
        private int id;

        private Entities(String v, int id) {
            this.value = v;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Entities getByValue(String value) {
            for (Entities entity : Entities.values()) {
                if (value.equalsIgnoreCase(entity.name())) {
                    return entity;
                }
            }
            throw new IllegalArgumentException("Specified name does not relate to a valid Entity Type");
        }

        public static Entities getById(int id) {
            for (Entities entity : Entities.values()) {
                if (id == entity.id) {
                    return entity;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid Entity Type");
        }
    }

    public static enum EnumPixelTracker {

        LINKTRACKER("LinkTracker", true, 1, null),
        MARKETVINE("MarketVine", true, 2, null),
        INTEGRATEDWITHFEED("IntegratedWithFeed", true, 3, null);
        //HASOFFERS("HasOffers", true, 3, null);

        private boolean isActive;
        private String trackerName;
        private int intValue;
        private String endPoint;

        EnumPixelTracker(String trackerName, boolean isActive, int intValue, String endPoint) {
            this.trackerName = trackerName;
            this.isActive = isActive;
            this.intValue = intValue;
            this.endPoint = endPoint;
        }

        public boolean isActive() {
            return isActive;
        }

        public String getTrackerName() {
            return trackerName;
        }

        public int intValue() {
            return intValue;
        }

        public String getEndPoint() {
            return endPoint;
        }


        public static EnumPixelTracker valueOf(Integer value) {
            if (value == null) {
                return null;
            }
            for (EnumPixelTracker s : values()) {
                if (s.intValue() == value) {
                    return s;
                }
            }
            return null;
        }
    }

    public static enum EnumDataImport {
        CJ("CJ", true, 1),
        FICSTAR("FICSTAR", true, 2),
        GOOGLE("GOOGLE", true, 3),
        MERCHANT("MERCHANT", true, 4),
        UNKNOWN("UNKNOWN", false, 5);

        private String dataImportType;
        private boolean isActive;
        private int value;

        EnumDataImport(String dataImportType, boolean isActive, int value) {
            this.dataImportType = dataImportType;
            this.isActive = isActive;
            this.value = value;
        }

        public String getDataImportType() {
            return dataImportType;
        }

        public boolean isActive() {
            return isActive;
        }

        public int getValue() {
            return value;
        }

        public static EnumDataImport valueOf(Integer value) {
            if (value == null) {
                return null;
            }

            for (EnumDataImport type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }

        public static CurationSourceType getByValue(String value) {
            for (CurationSourceType type : CurationSourceType.values()) {
                if (value.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified name does not relate to a valid Curation source type");
        }

        public static CurationSourceType getById(int id) {
            for (CurationSourceType type : CurationSourceType.values()) {
                if (id == type.id) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid Entity Type");
        }
    }

    public static enum CurationSourceType {

        TWITTER_USERNAME(100, "twitter.username"),
        TWITTER_LIST(101, "twitter.username", "twitter.listname"),
        TWITTER_HASHTAG(102, "twitter.hashtag"),
        TWITTER_KEYWORD(103, "twitter.keyword"),

        FACEBOOK_USERNAME(200, "facebook.username"),
        FACEBOOK_PAGE(201,"facebook.name", "facebook.page"),
        FACEBOOK_KEYWORD(202, "facebook.keyword"),

        RSS_FEED(300, "rss.url","rss.name"),

        YOUTUBE_USERNAME(400, "youtube.username"),
        YOUTUBE_CHANNEL(401, "youtube.channel"),
        YOUTUBE_KEYWORD(402, "youtube.keyword"),

        USER_GENERATED(1000, null),

        /* Library constant is for use case  when curation comes for document or other like library related stuff */
        // Shouldn't be used will be removed later.
        LIBRARY(2100, "library "),

        // Since we can't have a generic term library for all LIBRARY contents like DOCUMENTS, EVENTS etc.
        DOCUMENT(2000, "DOCUMENT"), IMAGE(2001, "IMAGE"), ARTICLE(2002,"ARTICLE"), VIDEO(2003, "VIDEO"), LINK(2004, "LINK"),
        EVENT(3000, "EVENT");

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


        /**
         * A helper method to get a specific SourceType of an object from its key
         *
         * @param type the state value of the CurationSourceType object
         * @return the CurationSourceType object corresponding to the key value. <tt>NULL</tt> if the key is invalid.
         */
          public static int getId(String type) {
            for (CurationSourceType sourceType : CurationSourceType.values()) {
                if (sourceType.name().equalsIgnoreCase(type)) {
                    return sourceType.id;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid sourceType");
        }


        public static CurationSourceType getById(int id) {
            for (CurationSourceType sourceType : CurationSourceType.values()) {
                if (id == sourceType.id) {
                    return sourceType;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid sourceType");
        }

         public static CurationSourceType getType(String type) {
            for (CurationSourceType sourceType : CurationSourceType.values()) {
                if (sourceType.name().equalsIgnoreCase(type)) {
                    return sourceType;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid sourceType");
        }

    }

    /**
     * The Status enum which will be used across all the Entity objects.
     * If any Entity has a status column, please use this ENUM.
     * If there is a specific state which is undefined. Please add it to
     * the Enum definition
     */
    public static enum Status {
        /**
         * Deleted document state.
         */
        DELETED("DELETED", 0),

        /**
         * Published document state.
         */
        PUBLISHED("PUBLISHED", 1),

        /**
         * Archived document state.
         */
        ARCHIVED("ARCHIVED", 2),

        /**
         * Expired document state.
         */
        EXPIRED("EXPIRED", 3),

        /**
         * Rejected document state.
         */
        REJECTED("REJECTED", 4),

        /**
         * Pending Approval document state.
         */
        PENDING_APPROVAL("PENDING_APPROVAL", 5),

        /**
         * Incomplete document state.
         */
        INCOMPLETE("INCOMPLETE", 6);


        private String status;
        private int id;

        private Status(String status, int id) {
            this.status = status;
            this.id = id;
        }


        public String getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }

        /**
         * A helper method to get a specific Status of an object from its key
         *
         * @param status the state value of the Status object
         * @return the Status object corresponding to the key value. <tt>NULL</tt> if the key is invalid.
         */
        public static Status getByStatus(String status) {
            for (Status state : Status.values()) {
                if (state.getStatus().equals(status)) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Specified status does not relate to a valid Status type.");
        }

        /**
         * A helper method to get a specific Status of an object from its id
         *
         * @param id the id of the Status object
         * @return the Status object corresponding to the id value. <tt>NULL</tt> if the id is invalid.
         */
        public static Status getById(int id) {
            for (Status state : Status.values()) {
                if (id == state.id) {
                    return state;
                }
            }
            throw new IllegalArgumentException("Specified id does not relate to a valid Status Type");
        }
    }

    public static enum ExecutionStatus {

        IN_QUEUE("IN_QUEUE", 0),

        PROCESSING("PROCESSING", 100),

        DONE("DONE", 200),

        // all 4xx are errors.
        ERROR("ERROR", 400),

        ERROR_READ("ERROR_READ", 401),

        ERROR_EXTRACTING("ERROR_EXTRACTING", 402),

        ERROR_PARSING("ERROR_PARSING", 403),

        ERROR_WRITE("ERROR_WRITE", 404),

        ERROR_NO_HANDLER("ERROR_NO_HANDLER", 415);

        private String status;

        private int id;

        private ExecutionStatus(String status, int id) {
            this.status = status;
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public int getId() {
            return id;
        }
    }

}
