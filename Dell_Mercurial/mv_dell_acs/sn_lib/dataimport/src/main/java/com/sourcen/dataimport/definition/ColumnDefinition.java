/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.definition;

import com.sourcen.dataimport.transformer.ColumnTransformer;
import com.sourcen.dataimport.transformer.FkColumnTransformer;
import com.sourcen.dataimport.transformer.GenericColumnTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.sql.Date;

/**
 * ColumnDefinition is used for the specifying the Column in the rows of the {@link TableDefinition}.
 *
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: adarsh $
 * @version $Revision: 3022 $, $Date:: 2012-06-08 09:50:22#$
 */
public final class ColumnDefinition {

    /**
     * logger class
     */
    private static final Logger logger = LoggerFactory.getLogger(ColumnDefinition.class);

    /**
     * class fields
     */
    private ColumnTransformer columnTransformer;

    private Object defaultValueObject = null;

    private String lookupTable;

    private Boolean allowNull;

    private Boolean verifyLookupTableExists = true;

    private Boolean skipInsert;

    private TableDefinition tableDefinition;

    private String destination;

    private Short index;

    private String source;

    private String type;

    private TableDefinition referenceTable;

    private String defaultValue;

    private String transformerClass;

    private String transformerBean;

    /**
     * default constructor.
     */
    public ColumnDefinition() {
    }

    public void initialize() {
        if (source == null) {
            if (tableDefinition.getProperty("tableType") == null) {
                Assert.notNull(defaultValue, "defaultValue cannot be null if no source is specified for column "
                        + destination + " in table " + tableDefinition.getDestinationTable());
            }
        } else if (source.contains(" ")) {
            logger.warn(tableDefinition.getSourceTable() + "." + source + " contains spaces. spaces will be removed.");
            source = source.replaceAll(" ", "");
        }
        Assert.notNull(tableDefinition, "tableDefinition cannot be null");
        Assert.notNull(destination, "destination cannot be null for table " + tableDefinition.getDestinationTable());

        // parse FK mapping tables.
        if (this.getLookupTable() != null) {
            //We get cyclic reference if we use DataImportConfig.getInstance(), so use a soft reference.
            TableDefinition fkDefinition = tableDefinition.getDataImportConfig()
                    .getTableDefinition(this.getLookupTable());
            if (fkDefinition == null) {
                if (getVerifyLookupTableExists()) {
                    throw new IllegalArgumentException("Unable to find the lookupTable:" + this.getLookupTable()
                            + " for " + this.getDestination() + "." + this.getDestination());
                }
            }
            this.setReferenceTable(fkDefinition);
        }
    }


    public ColumnTransformer getTransformer() {
        // load the column transformer only if this is required.
        ApplicationContext context = tableDefinition.getDataImportConfig().getApplicationContext();

        if (columnTransformer == null) {
            // check the column transformers.
            if (transformerClass != null) {
                try {
                    Class clazz = Class.forName(transformerClass);
                    if (transformerClass.indexOf(".") < -1) {
                        // then its beanName.
                        columnTransformer = (ColumnTransformer) context.getBean(transformerClass, clazz);
                    } else {
                        // new instance
                        if (ColumnTransformer.class.isAssignableFrom(clazz)) {
                            columnTransformer = (ColumnTransformer) clazz.newInstance();
                        } else {
                            logger.error(clazz + " must implement " + ColumnTransformer.class.getCanonicalName());
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    //logger.error(e.getMessage(), e);
                }
            } else  if (transformerBean != null) { 
                columnTransformer = context.getBean(transformerBean, FkColumnTransformer.class);
            }

            // set the FK transformer
            if (columnTransformer == null && this.getReferenceTable() != null) {
                columnTransformer = context.getBean("fkColumnTransformer", FkColumnTransformer.class);
            }

            // default to the generic one if we dont find any.
            if (columnTransformer == null) {
                columnTransformer = context.getBean("genericColumnTransformer", GenericColumnTransformer.class);
            }

            try {
                columnTransformer.setColumnDefinition(this);
                columnTransformer.initialize();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return columnTransformer;
    }


    public Object getDefaultValueAsObject() {
        if (defaultValue == null) {
            getDefaultValue(); // initialize if its null from TableProperties.
        }

        if (type != null && defaultValue != null) {
            if (type.equalsIgnoreCase("datetime")
                    && defaultValue.equalsIgnoreCase("TIMESTAMP")) {
                defaultValueObject = new Date(System.currentTimeMillis());
            } else if (defaultValueObject == null && !defaultValue.equalsIgnoreCase("NULL")
                    && !defaultValue.equalsIgnoreCase("AUTO_GENERATED")) {
                if (type.equalsIgnoreCase("string")){
                    defaultValueObject = defaultValue;
                }else if (type.equalsIgnoreCase("long")) {
                    defaultValueObject = Long.parseLong(this.getDefaultValue());
                } else if (type.equalsIgnoreCase("int")) {
                    defaultValueObject = Integer.parseInt(this.getDefaultValue());
                } else if (type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("bool")) {
                    defaultValueObject = Boolean.valueOf(this.getDefaultValue());
                }
            } else if (defaultValue.equalsIgnoreCase("AUTO_GENERATED")) {
                return tableDefinition.getDataImportConfig().getTableSequenceService()
                        .getSequenceId(tableDefinition, this);
            }
        }
        return defaultValueObject;
    }


    public String getDefaultValue() {
        if (defaultValue == null) {
            if (tableDefinition.getProperties().containsKey("columns." + destination + ".defaultValue")) {
                defaultValue = tableDefinition.getProperties().getProperty("columns." + destination + ".defaultValue");
            }
        }
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * getters and setters
     */

    public Boolean getSkipInsert() {
        return (skipInsert != null) ? skipInsert : false;
    }

    public void setSkipInsert(final Boolean skipInsert) {
        this.skipInsert = skipInsert;
    }


    public TableDefinition getTableDefinition() {
        return tableDefinition;
    }

    public void setTableDefinition(final TableDefinition tableDefinition) {
        this.tableDefinition = tableDefinition;
    }


    public String getDestination() {
        return destination;
    }

    public void setDestination(final String destination) {
        this.destination = destination;
    }


    public Short getIndex() {
        return index;
    }

    public void setIndex(final Short index) {
        this.index = index;
    }


    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }


    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;

    }

    public String getTypeAsIntern() {
        return type.toLowerCase().intern();
    }


    public TableDefinition getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(final TableDefinition referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getTransformerClass() {
        return transformerClass;
    }

    public void setTransformerClass(final String transformerClass) {
        this.transformerClass = transformerClass;
    }


    public String getLookupTable() {
        return lookupTable;
    }

    public void setLookupTable(final String lookupTable) {
        this.lookupTable = lookupTable;
    }


    public Boolean getAllowNull() {
        return (allowNull != null) ? allowNull : false;
    }

    public void setAllowNull(final Boolean allowNull) {
        this.allowNull = allowNull;
    }


    public Boolean getVerifyLookupTableExists() {
        // default value is true.
        return (verifyLookupTableExists != null) ? verifyLookupTableExists : true;
    }

    public void setVerifyLookupTableExists(final Boolean verifyLookupTableExists) {
        this.verifyLookupTableExists = verifyLookupTableExists;
    }

    private DataImportConfig dataImportConfig;

    public DataImportConfig getDataImportConfig() {
        return dataImportConfig;
    }

	public void setTransformerBean(String beanName) {
		this.transformerBean = beanName; 
	}

    @Override
    public String toString() {
        return "ColumnDefinition{"
                + "allowNull=" + allowNull
                + ", columnTransformer=" + columnTransformer
                + ", dataImportConfig=" + dataImportConfig
                + ", defaultValue='" + defaultValue + '\''
                + ", defaultValueObject=" + defaultValueObject
                + ", destination='" + destination + '\''
                + ", index=" + index
                + ", lookupTable='" + lookupTable + '\''
                + ", referenceTable=" + referenceTable
                + ", skipInsert=" + skipInsert
                + ", source='" + source + '\''
                + ", tableDefinition=#" + tableDefinition.getSourceTable()
                + ", transformerClass='" + transformerClass + '\''
                + ", transformerBean='" + transformerBean + '\''
                + ", type='" + type + '\''
                + ", verifyLookupTableExists=" + verifyLookupTableExists
                + '}';
    }
}
