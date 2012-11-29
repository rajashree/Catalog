package com.dell.dw.managers.dataimport.util;

import com.sourcen.core.config.ConfigurationServiceImpl;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/4/12
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAProperties {
    /** Google Analytics API Properties and Credentials **/
    public static String APPLICATION_NAME = ConfigurationServiceImpl.getInstance().getProperty("analytics.applicationName");
    public static String USERNAME = ConfigurationServiceImpl.getInstance().getProperty("analytics.username");
    public static String PASSWORD = ConfigurationServiceImpl.getInstance().getProperty("analytics.password");
    public static String API_KEY = ConfigurationServiceImpl.getInstance().getProperty("analytics.apiKey");
    public static String DATA_FEED = ConfigurationServiceImpl.getInstance().getProperty("analytics.dataFeed");
    public static String ACCOUNT_FEED = ConfigurationServiceImpl.getInstance().getProperty("analytics.accountFeed");


    /** Google Analytics Event Tracking Properties **/
    public static Boolean EVENT_TRACKING_ENABLED = Boolean.valueOf(ConfigurationServiceImpl.getInstance().getProperty("analytics.eventTracking.enable"));
    //public static String EVENT_COUNT = ConfigurationServiceImpl.getInstance().getProperty("analytics.eventCount");
    public static String EVENT_METRICS = ConfigurationServiceImpl.getInstance().getProperty("analytics.eventMetrics");
    public static String EVENT_DIMENSIONS = ConfigurationServiceImpl.getInstance().getProperty("analytics.eventDimensions");

    /** Google Analytics Page View Tracking Properties **/
    public static Boolean PAGE_VIEW_TRACKING_ENABLED = Boolean.valueOf(ConfigurationServiceImpl.getInstance().getProperty("analytics.pageViewTracking.enable"));
   // public static String PAGE_VIEW_COUNT = ConfigurationServiceImpl.getInstance().getProperty("analytics.pageViewCount");
    public static String PAGE_VIEW_METRICS = ConfigurationServiceImpl.getInstance().getProperty("analytics.pageViewMetrics");
    public static String PAGE_VIEW_DIMENSIONS = ConfigurationServiceImpl.getInstance().getProperty("analytics.pageViewDimensions");

}
