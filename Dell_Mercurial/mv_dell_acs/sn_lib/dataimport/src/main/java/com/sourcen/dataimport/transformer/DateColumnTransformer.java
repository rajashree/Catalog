/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.dataimport.transformer;

import com.sourcen.dataimport.definition.ColumnDefinition;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA. User: Adarsh Date: 7/8/11 Time: 1:07 PM To change this template use File | Settings | File
 * Templates.
 */
public class DateColumnTransformer implements ColumnTransformer {

    @Override
    public Object transform(Map<String, Object> record, ColumnDefinition columnDefinition, Object value) throws
            SQLException {
        String dataFormat = "mm-dd-yyyy";
        java.sql.Date sqlDate = null;
        try {

            Pattern p = Pattern.compile("[*\\.]|[*\\/]|[*\\-]|[*\\_]|[*\\,]|[*\\s]");
            String[] sqlDateArray = p.split(((String) value).trim());
            StringBuffer stringBufferObject = new StringBuffer();

            for (String s : sqlDateArray) {
                stringBufferObject.append(s);
            }

            if (stringBufferObject.length() > 0) {
                if (stringBufferObject.length() == 8) {
                    stringBufferObject.insert(2, '-');
                }
                stringBufferObject.insert(5, '-');
            }
            DateFormat formater = new SimpleDateFormat(dataFormat);
            java.util.Date parsedUtilDate = formater.parse(stringBufferObject.toString());
            value = parsedUtilDate.getTime();
            stringBufferObject = null;
        } catch (Exception exceptionObject) {
            exceptionObject.printStackTrace();
        }

        return value;
    }

    /* public Object transform(ColumnDefinition columnDefinition, Object value) throws SQLException {
        logger.info("Date value Before " + value);
        String dataFormat = "mm-dd-yyyy";
        java.sql.Date sqlDate = null;
        try {

            Pattern p = Pattern.compile("[*\\.]|[*\\/]|[*\\-]|[*\\_]|[*\\,]|[*\\s]");
            String[] sqlDateArray = p.split(((String) value).trim());
            StringBuffer stringBufferObject = new StringBuffer();

            for (String s : sqlDateArray) {
                stringBufferObject.append(s);
            }

            if (stringBufferObject.length() > 0) {
                if (stringBufferObject.length() == 8)
                    stringBufferObject.insert(2, '-');
                stringBufferObject.insert(5, '-');
            }
            DateFormat formater = new SimpleDateFormat(dataFormat);
            java.util.Date parsedUtilDate = formater.parse(stringBufferObject.toString());
            value = parsedUtilDate.getTime();
            stringBufferObject = null;
        } catch (Exception exceptionObject) {
            exceptionObject.printStackTrace();
        }

        logger.info("Date value After" + value);
        return value;
    }*/

    @Override
    public void setColumnDefinition(ColumnDefinition columnDefinition) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void initialize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
