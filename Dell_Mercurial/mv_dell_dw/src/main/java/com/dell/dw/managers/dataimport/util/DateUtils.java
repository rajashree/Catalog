package com.dell.dw.managers.dataimport.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/21/12
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateUtils extends com.sourcen.core.util.DateUtils{
    public static SimpleDateFormat gaDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Gives day(s) difference between given last date and today
     * @param lastDate
     * @return
     */
    public static long getDayDiff(Date lastDate) {
        long dayDiff = 0;
        if(lastDate != null) {
            Date currDate = new Date();
            dayDiff = (currDate.getTime() - lastDate.getTime()) / (1000 * 60 * 60 * 23);
        }
        return dayDiff;
    }

    /**
     * Gives day(s) difference between given start date and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDayDiff(Date startDate, Date endDate) {
        long dayDiff = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 23);
        return dayDiff;
    }

    /**
     * Gives hour(s) difference between given start date and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getHourDiff(Date startDate, Date endDate) {
        long hrDiff = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60);
        return hrDiff;
    }


    /**
     *  Gives hour(s) difference between given last date and today
     * @param lastDate
     * @return
     */
    public static  long getHourDiff(Date lastDate) {
        long timeDiff = 0;
        if(lastDate != null) {
            Date currDate = new Date();
            timeDiff = (currDate.getTime() - lastDate.getTime()) / (1000 * 60 * 60);
        }
        return timeDiff;
    }

    /**
     * Provides formatted date
     *
     * @param date - Date to be formatted.
     * @param format - Send the format you would like to have. If not sent then
     *               will default to this format - "yyyyMMdd_HHmmss".
     * @return String of the Date object. For ex: 20120406_142036
     */
    public static String getFormattedDate(Date date,String format) {
        if (format == null)
            format = "yyyyMMdd_HHmmss";
        if(date == null)
            date = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);

    }

    /**
     * Increment or Decrement the date by specified number of days
     * @param date - to be incremented or decremented
     * @param incrementBy - no. of days by which date needs to be incremented or decremented
     * @return Date object
     */
    public static Date incrementDate(Date date, int incrementBy){
        if(date == null)
            date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,incrementBy);
        return cal.getTime();
    }



    /**
     *
     * @param dateString
     * @param format
     * @return
     * @throws Exception
     */
    public static Date getFormattedDate(String dateString, String format) throws Exception{
        Date date = null;
        DateFormat formatter;
        formatter = new SimpleDateFormat(format);
        try {
            date = (Date) formatter.parse(dateString);
        } catch (ParseException e) {
            throw new Exception();
        }
        return date;
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Date dayEnd = new Date(date.getTime());
        dayEnd.setHours(23);
        dayEnd.setMinutes(0);
        dayEnd.setSeconds(0);
        return dayEnd;
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date) {
        Date dayStart = new Date(date.getTime());
        dayStart.setHours(0);
        dayStart.setMinutes(0);
        dayStart.setSeconds(0);
        return dayStart;
    }

    /**
     *
     * @param date
     * @return
     */
    public static Date getNextDayStart(Date date) {
        Date nextDayStart = new Date(date.getTime());
        nextDayStart.setHours(24);
        nextDayStart.setMinutes(0);
        nextDayStart.setMinutes(0);
        return nextDayStart;
    }

    /**
     * Gives previous day date with last hour of the day for a given date
     * @param date
     * @return
     */
    public static Date getPreviousDayEnd(Date date) {
        long time = date.getTime() - (1000 * 60 * 60 * 24);
        Date prev = new Date(time);
        prev.setHours(23);
        prev.setMinutes(0);
        prev.setSeconds(0);
        return prev;
    }

    /**
     * Gives date with next successive hour of a given date
     * @param date
     * @return
     */
    public static Date getNextHourDate(Date date) {
        date.setMinutes(0);
        date.setSeconds(0);
        long time = date.getTime() + (1000 * 60 * 60);
        return new Date(time);
    }

    /**
     * Gives date with next successive hour of a given date
     * @param date
     * @return
     */
    public static Date getPreviousHourDate(Date date) {
        date.setMinutes(0);
        date.setSeconds(0);
        long time = date.getTime() - (1000 * 60 * 60);
        return new Date(time);
    }

    /**
     *
     * @param date
     * @param timezoneId
     * @return
     */
    public static Date getDateInTimeZone(Date date, String timezoneId) {
        TimeZone tz = TimeZone.getTimeZone(timezoneId);

        Calendar mbCal = new GregorianCalendar(tz);
        mbCal.setTimeInMillis(date.getTime() + tz.getDSTSavings());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));
        //cal.setTime(new Date(cal.getTime().getTime() + tz.getDSTSavings()));
        return cal.getTime();
    }


}
