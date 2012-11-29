/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;

/**
 TableDefinition class provides the basic container for holding the table data into the tabular structure.

 @author Navin Raj Kumar G.S.
 @author $LastChangedBy: adarsh $
 @version $Revision: 1702 $, $Date:: 2012-04-18 11:30:55#$ */
public final class TableDefinition implements InitializingBean {

    /**
     logger class
     */
    private static final Logger logger = LoggerFactory.getLogger(TableDefinition.class);

    // package friendly so that ColumnDefinition can access this
    private DataImportConfig dataImportConfig;

    private String destinationTable;

    private String sourceTable;

    private String filter;

    private Properties properties;

    private String converter;

    private Collection<String> dependencies;

    private String sourceDataSource;

    private String destinationDataSource;

    private LinkedHashSet<Key> keys = new LinkedHashSet<Key>(1);


    public String sqlSelect;

    public String sqlInsert;

    public String sqlUpdate;

    public String sqlDelete;

    //we have to set this here to overcome a stack overflow error due to cyclic reference.
    public TableDefinition() {
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {

        Assert.notNull(destinationTable, "destination for a table cannot be null");

        // initialize the columns
        for (ColumnDefinition definition : getColumns()) {
            definition.setTableDefinition(this);
            definition.initialize();
        }

        // initialize the keys
        for (Key key : getKeys()) {
            key.setTableDefinition(this);
            key.afterPropertiesSet();
        }


        // generate queries.
        if (sqlSelect == null) {
            Assert.notNull(sourceTable, "source for a table " + destinationTable + " cannot be null");

            sqlSelect = "SELECT * FROM " + this.getSourceTable();
            if (this.getFilter() != null) {
                sqlSelect += " WHERE " + this.getFilter();
            }
        }

        LinkedList<String> columnNames = new LinkedList<String>();
        for (ColumnDefinition columnDefinition : this.getColumns()) {
            if (!columnDefinition.getSkipInsert()) {
                columnNames.add(columnDefinition.getDestination());
            }
        }
        String[] columnCount = new String[columnNames.size()];

        // INSERT Query.
        if (sqlInsert == null) {
            sqlInsert = "INSERT INTO " + this.getDestinationTable();
            for (int i = 0; i < columnCount.length; i++) {
                columnCount[i] = "?";
            }
            sqlInsert += " (" + Joiner.on(",").join(columnNames) + ") VALUES(" + Joiner.on(",").join(columnCount) + ")";
        }


        if (sqlUpdate == null) {
            sqlUpdate = "UPDATE " + this.getDestinationTable() + " SET ";
            LinkedList<String> columns = new LinkedList<String>();
            for (ColumnDefinition columnDefinition : this.getColumns()) {
                columns.add(columnDefinition.getDestination() + "=?");
            }
            sqlUpdate += Joiner.on(", ").join(columns);
        }

    }

    public Collection<String> getSourceColumnNames(final Boolean toLowerCase) {
        Collection<String> columnNames = new ArrayList<String>(columns.size());
        for (ColumnDefinition definition : columns) {
            if (toLowerCase) {
                columnNames.add(definition.getSource().toLowerCase().intern());
            } else {
                columnNames.add(definition.getSource().intern());
            }
        }
        return columnNames;
    }

    /**
     getters and setters
     */


    public ColumnDefinition getColumnByName(final String name) {
        for (ColumnDefinition columnDefinition : columns) {
            if (columnDefinition.getDestination().equals(name)) {
                return columnDefinition;
            }
        }
        return null;
    }

    public void setDataImportConfig(final DataImportConfig dataImportConfig) {
        this.dataImportConfig = dataImportConfig;
    }

    public DataImportConfig getDataImportConfig() {
        return dataImportConfig;
    }


    public String getDestinationTable() {
        return destinationTable;
    }

    public void setDestinationTable(final String destinationTable) {
        this.destinationTable = destinationTable;
    }


    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(final String sourceTable) {
        this.sourceTable = sourceTable;
    }


    public String getFilter() {
        return filter;
    }

    public void setFilter(final String filter) {
        this.filter = filter;

    }


    public Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
        }
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setProperty(String key, String value) {
        getProperties().put(key, value);
    }

    public String getProperty(String key) {
        return getProperty(key, null);
    }

    public String getProperty(String key, String defaultValue) {
        if (getProperties().containsKey(key)) {
            return getProperties().getProperty(key);
        }
        return defaultValue;
    }

    public Boolean getBooleanProperty(String key) {
        return getBooleanProperty(key, false);
    }

    public Boolean getBooleanProperty(String key, Boolean defaultValue) {
        if (getProperties().containsKey(key)) {
            return getProperties().getProperty(key).equalsIgnoreCase("true");
        }
        return defaultValue;
    }

    public Integer getIntegerProperty(String key) {
        return getIntegerProperty(key, 0);
    }

    public Integer getIntegerProperty(String key, Integer defaultValue) {
        if (getProperties().containsKey(key)) {
            try {
                return Integer.valueOf(getProperties().getProperty(key));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return defaultValue;
    }

    public Long getLongProperty(String key) {
        return getLongProperty(key, 0L);
    }

    public Long getLongProperty(String key, Long defaultValue) {
        if (getProperties().containsKey(key)) {
            try {
                return Long.valueOf(getProperties().getProperty(key));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return defaultValue;
    }


    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    public void setSqlSelect(String sqlSelect) {
        this.sqlSelect = sqlSelect;
    }

    public String getSqlInsert() {
        return sqlInsert;
    }


    public Collection<String> getDependencies() {
        if (dependencies == null) {
            dependencies = new LinkedList<String>();
        }
        return dependencies;
    }

    public void setDependencies(Collection<String> dependencies) {
        this.dependencies = dependencies;
    }

    public void setSqlInsert(String sqlInsert) {
        this.sqlInsert = sqlInsert;
    }

    public String getSqlUpdate() {
        return sqlUpdate;
    }

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    public String getSqlDelete() {
        return sqlDelete;
    }

    public void setSqlDelete(String sqlDelete) {
        this.sqlDelete = sqlDelete;
    }


    private LinkedHashSet<ColumnDefinition> columns = new LinkedHashSet<ColumnDefinition>(5);

    public HashSet<ColumnDefinition> getColumns() {
        return columns;
    }

    public void setColumns(LinkedHashSet<ColumnDefinition> columnDefinitions) {
        this.columns = columnDefinitions;
    }

    private Key primaryKey;

    public Key getPrimaryKey() {
        try {
            if (this.primaryKey == null && keys != null) {
                for (Key key : keys) {
                    if (key.getPrimaryKey()) {
                        this.primaryKey = key;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return primaryKey;
    }

    public void setPrimaryKey(Key primaryKey) {
        this.primaryKey = primaryKey;
    }


    public String getSourceDataSource() {
        return sourceDataSource;
    }

    public void setSourceDataSource(String sourceDataSource) {
        this.sourceDataSource = sourceDataSource;
    }


    public String getDestinationDataSource() {
        return destinationDataSource;
    }

    public void setDestinationDataSource(String destinationDataSource) {
        this.destinationDataSource = destinationDataSource;
    }


    public LinkedHashSet<Key> getKeys() {
        if (keys == null) {
            keys = new LinkedHashSet<Key>();
        }
        return keys;
    }

    public void setKeys(LinkedHashSet<Key> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "TableDefinition{"
                + "dataImportConfig=" + dataImportConfig
                + ", sourceDataSource='" + sourceDataSource + '\''
                + ", destinationDataSource='" + destinationDataSource + '\''
                + ", destinationTable='" + destinationTable + '\''
                + ", sourceTable='" + sourceTable + '\''
                + ", filter='" + filter + '\''
                + ", converter='" + converter + '\''
                + ", primaryKey=" + primaryKey
                + ", columns=" + columns
                + ", dependencies=" + dependencies
                + ", properties=" + properties
                + ", sqlSelect='" + sqlSelect + '\''
                + ", sqlInsert='" + sqlInsert + '\''
                + ", sqlUpdate='" + sqlUpdate + '\''
                + ", sqlDelete='" + sqlDelete + '\''
                + '}';
    }

	public ColumnDefinition getColumnBySource(String findSource) {
        for (ColumnDefinition columnDefinition : columns) {
        	String cdSource = columnDefinition.getSource();
        	
        	if (cdSource.contains(",")) {
	        	String[] sources = cdSource.split(",");
	        	
	        	for(String source : sources) {
		            if (source.compareTo(findSource) == 0) {
		                return columnDefinition;
		            }
	        	}
        	} else {
	            if (cdSource.compareTo(findSource) == 0) {
	                return columnDefinition;
	            }
        	}
        }
        
        return null;
	}


    public ColumnDefinition getColumnByDestination(String findDestination) {
        for (ColumnDefinition columnDefinition : columns) {
            String cdDestination = columnDefinition.getDestination();

            if (cdDestination.compareTo(findDestination) == 0) {
                return columnDefinition;
            }
        }

        return null;
    }
}
