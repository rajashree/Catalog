/*
 * Copyright (c) Sourcen Inc. 2004-2012
 * All rights reserved.
 */

package com.sourcen.core.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Chethan
 * Date: 3/1/12
 * Time: 4:06 PM
 */
public class DateUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);
    public static final DateFormat TIMESTAMP_DATEFORMAT = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");

    public static final Date JVM_START_TIME = new Date();
    public static final Long JVM_START_TIME_UTC = JVM_START_TIME.getTime();
    public static final String JVM_START_TIMESTAMP = TIMESTAMP_DATEFORMAT.format(JVM_START_TIME);


    public static Date getDate(String dateString) {
        // Default format
        try {
            return getDate(dateString, "yyyy-MM-dd HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDate(String dateString, String format) throws ParseException {
        Date date = null;
        DateFormat formatter;
        formatter = new SimpleDateFormat(format);
        date = formatter.parse(dateString);
        LOG.info("Date is is " + date);
        //return date;

        return date;
    }

    /**
     * Provides custom date formatting options.
     *
     * @param format - Send the format you would like to have. If not sent then
     *               will default to this format - "yyyyMMdd_HHmmss".
     *
     * @return String of the Date object. For ex: 20120406_142036
     */
    public static String getFormattedDate(String format) {
        if (format == null)
            format = "yyyyMMdd_HHmmss";

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());

    }


    public static String toString(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuilder builder = new StringBuilder(dateformat.format(date));
        return builder.toString();
    }

    public static String to24formatString(Date date) {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder builder = new StringBuilder(dateformat.format(date));
            return builder.toString();
        }

    public static String toISO8601DateFormat(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        StringBuilder builder = new StringBuilder(dateformat.format(date));
        return builder.toString();
    }
    public static String toSqlDateFormat(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss[.fffffffff]");
        StringBuilder builder = new StringBuilder(dateformat.format(date));
        return builder.toString();
    }



}

