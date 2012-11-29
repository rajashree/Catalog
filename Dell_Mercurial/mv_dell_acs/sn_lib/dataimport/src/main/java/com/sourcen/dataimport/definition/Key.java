/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 Key is provide the container for holding he keys for the column primary, foreign key or some other reference keys.

 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1698 $, $Date:: 2012-04-18 11:27:59#$ */
public final class Key implements InitializingBean {

    /**
     logger class
     */
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Key.class);

    /**
     class fields
     */
    private ColumnDefinition destinationColumn;

    private Boolean primaryKey;

    private String type;

    private String sourceKey;

    private String destinationKey;

    private String sourceKeySource;

    private String destinationKeySource;

    private TableDefinition tableDefinition;

    /**
     default constructor
     */
    public Key() {
    }

    /**
     parameter constructor
     */
    public Key(final String type, final String sourceKey, final String destinationKey) {
        this.type = type;
        this.sourceKey = sourceKey;
        this.destinationKey = destinationKey;
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {

        Assert.notNull(sourceKey, "The source key cannot be null");
        Assert.notNull(destinationKey, "The destination key cannot be null");

        if (this.type == null) {
            primaryKey = true;
        } else {
            primaryKey = this.type.equalsIgnoreCase("primary");
        }

        if (this.sourceKeySource == null) {
            this.sourceKeySource = "source";
        }
        if (this.destinationKeySource == null) {
            this.destinationKeySource = "destination";
        }
        Assert.notNull(tableDefinition, "tableDefinition cannot be null");

        destinationColumn = tableDefinition.getColumnByName(this.destinationKey);


    }

    /**
     inner class specifying the Types for the keys
     */
    public static final class Type {

        public static final Type SOURCE = new Type("source");

        public static final Type DESTINATION = new Type("source");

        private Type(final String type) {
            this.type = type;
        }

        public static boolean is(final String key, final Type keyType) {
            return keyType.type.equalsIgnoreCase(key);
        }

        private String type;
    }

    /**
     getters and setters
     */

    public ColumnDefinition getDestinationColumn() {
        return destinationColumn;
    }

    public void setDestinationColumn(final ColumnDefinition destinationColumn) {
        this.destinationColumn = destinationColumn;
    }

    public Boolean getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(final Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(final String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getDestinationKey() {
        return destinationKey;
    }

    public void setDestinationKey(final String destinationKey) {
        this.destinationKey = destinationKey;
    }

    public String getSourceKeySource() {
        return sourceKeySource;
    }

    public void setSourceKeySource(final String sourceKeySource) {
        this.sourceKeySource = sourceKeySource;
    }

    public String getDestinationKeySource() {
        return destinationKeySource;
    }

    public void setDestinationKeySource(final String destinationKeySource) {
        this.destinationKeySource = destinationKeySource;
    }

    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(final TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }

    @Override
    public String toString() {
        return "Key{"
                + "destinationColumn=" + destinationColumn
                + ", destinationKey='" + destinationKey + '\''
                + ", destinationKeySource='" + destinationKeySource + '\''
                + ", primaryKey=" + primaryKey
                + ", sourceKey='" + sourceKey + '\''
                + ", sourceKeySource='" + sourceKeySource + '\''
                + ", tableDefinition=#" + tableDefinition.getSourceTable()
                + ", type='" + type + '\''
                + '}';
    }
}
