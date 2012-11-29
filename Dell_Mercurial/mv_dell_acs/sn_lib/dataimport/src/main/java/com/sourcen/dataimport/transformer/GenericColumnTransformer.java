/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

import com.google.common.base.Joiner;
import com.sourcen.core.util.StringUtils;
import com.sourcen.dataimport.definition.ColumnDefinition;
import com.sourcen.dataimport.definition.DataImportConfig;
import com.sourcen.dataimport.definition.TableDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Navin Raj Kumar G.S.
 * @author $LastChangedBy: sandeep $
 * @version $Revision: 3168 $, $Date:: 2012-06-14 12:45:31#$
 */

/**
 * {@inheritDoc}
 */
public class GenericColumnTransformer implements ColumnTransformer {

    /**
     * logger class
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final long timeZoneOffset = Calendar.getInstance().getTimeZone().getRawOffset();

    /**
     * class properties
     */
    protected TableDefinition tableDefinition;

    protected ColumnDefinition columnDefinition;

    protected DataImportConfig dataImportConfig;

    /**
     * static properties and its initialization in the static block
     */

    private static final String[] supportedDataTypes = new String[]{
            "string".intern(),
            "boolean".intern(),
            "int".intern(),
            "float".intern(),
            "long".intern(),
            "double".intern(),
            "date".intern(),
            "datetime".intern(),
            "time".intern()
    };

    /**
     * GenericColumnTransformer defalut constructor
     */
    public GenericColumnTransformer() {
    }

    private String[] trueValues = new String[]{"true".intern(), "1".intern()};

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        try {
            String trueValueSetting = columnDefinition.getTableDefinition().getProperty("dataTypes.boolean.trueValues");
            if (trueValueSetting != null) {
                Set<String> trueValues = new HashSet<String>(StringUtils.asCollection(trueValueSetting.split(","), true));
                trueValues.add("1");
                trueValues.add("true");
                String[] trueValuesTmp = new String[trueValues.size()];
                trueValues.toArray(trueValuesTmp);
                this.trueValues = new String[trueValues.size()];
                for (int i = 0; i < trueValuesTmp.length; i++) {
                    this.trueValues[i] = trueValuesTmp[i].intern();
                }
            }
        } catch (Exception e) {
            logger.error("Unable to initalize trueValues for column:=" +
                    columnDefinition.getTableDefinition().getSourceTable() + "." + columnDefinition.getSource(), e);
            trueValues = new String[]{"true".intern(), "1".intern()};
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object rawValue) throws
            SQLException {
        try {
            String type = columnDefinition.getTypeAsIntern();
            if (rawValue == null) {
                return columnDefinition.getDefaultValueAsObject();
            } else {
                int typeIndex = -1;
                for (int i = 0; i < supportedDataTypes.length; i++) {
                    if (supportedDataTypes[i].equals(type)) {
                        typeIndex = i;
                        break;
                    }
                }
                if (typeIndex == 0) {
                    return rawValue.toString();
                }
                String stringValue = rawValue.toString();
                if (stringValue.isEmpty()) {
                    if (columnDefinition.getAllowNull()) {
                        return null;
                    } else if (columnDefinition.getDefaultValue() != null) {
                        if (logger.isInfoEnabled()) {
                            logger.info("using default value for column :=" + columnDefinition.getDestination()
                                    + " on record :=" + Joiner.on(",").withKeyValueSeparator("=").join(record));
                        }
                        stringValue = columnDefinition.getDefaultValue();
                    } else {
                        throw new IllegalArgumentException(
                                "the value for column:=" + columnDefinition.getDestination() +
                                        " is not found, and there is no default value.");
                    }
                }

                switch (typeIndex) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        /// clean the string for ','
                        if (stringValue != null) {
                            if (stringValue.contains(",")) {
                                stringValue = stringValue.replaceAll(",", "");
                            }
                        }
                        break;
                    default:
                }

                switch (typeIndex) {
                    case 1:
                        String stringValueLowerCase = stringValue.toLowerCase().intern();
                        for (String trueValue : trueValues) {
                            if (stringValueLowerCase == trueValue) { // we are doing a .intern for all trueValues.
                                return true;
                            }
                        }
                        return false;
                    case 2:
                        return Integer.valueOf(stringValue);
                    case 3:
                        return Float.valueOf(stringValue);
                    case 4:
                        return Long.valueOf(stringValue);
                    case 5:
                        return Double.valueOf(stringValue);
                    case 6:
                    case 7:
                    case 8:
                        // UTC is 1328546220, but java time is 1328546220000
                        if (stringValue.contains("/")) {
                            try {
                                DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
                                return formatter.parse(stringValue);
                            } catch (ParseException e) {
                                logger.error("Unable to parse date value");
                            }
                        } else {
                            long longDate = Long.valueOf(stringValue);
                            if (stringValue.length() < 12) {
                                longDate = (longDate * 1000) - timeZoneOffset;

                            }
                            return new Date(longDate);
                        }

                    default:
                        logger.warn("Unable to determine dataType for type:=" + type);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
        }
        return rawValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setColumnDefinition(ColumnDefinition columnDefinition) {
        this.columnDefinition = columnDefinition;
        this.tableDefinition = columnDefinition.getTableDefinition();
        this.dataImportConfig = columnDefinition.getDataImportConfig();
    }

    ;


}
