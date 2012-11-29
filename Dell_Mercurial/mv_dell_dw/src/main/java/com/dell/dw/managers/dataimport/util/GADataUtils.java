package com.dell.dw.managers.dataimport.util;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.google.gdata.client.analytics.AnalyticsService;
import com.google.gdata.client.analytics.DataQuery;
import com.google.gdata.data.analytics.DataEntry;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.data.analytics.ManagementFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/8/12
 * Time: 6:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GADataUtils {

     private static final Logger logger = LoggerFactory.getLogger(GADataUtils.class);

    /** GA Event Tracking Dimensions & Metrics **/
    public static final String EVENT_CATEGORY = "ga:eventCategory";
    public static final String EVENT_ACTION = "ga:eventAction";
    public static final String EVENT_LABEL = "ga:eventLabel";
    public static final String TOTAL_EVENTS = "ga:totalEvents";
    public static final String UNIQUE_EVENTS = "ga:uniqueEvents";
    public static final String EVENT_VALUE = "ga:eventValue";
    public static final String AVG_EVENT_VALUE = "ga:avgEventValue";
    public static final String EVENTS_PER_VISIT_WITH_EVENT = "ga:eventsPerVisitWithEvent";
    public static final String VISITS_WITH_EVENT = "ga:visitsWithEvent";

    /** GA Page View Tracking Dimensions & Metrics **/
    public static final String PAGE_TITLE = "ga:pageTitle";
    public static final String PAGE_PATH = "ga:pagePath";
    public static final String PAGE_DEPTH = "ga:pageDepth";
    //public static final String ENTRANCES = "ga:entrances";
    public static final String PAGE_VIEWS = "ga:pageviews";
    public static final String UNIQUE_PAGE_VIEWS = "ga:uniquePageviews";
    //public static final String TIME_ON_PAGE = "ga:timeOnPage";
    // public static final String EXITS = "ga:exits";
    public static final String ENTRANCE_RATE = "ga:entranceRate";
    public static final String PAGE_VIEWS_PER_VISIT = "ga:pageviewsPerVisit";
    public static final String AVG_TIME_ON_PAGE = "ga:avgTimeOnPage";
    public static final String EXIT_RATE = "ga:exitRate";

    /** Session Dimension & Metrics **/
    public static final String VISITS = "ga:visits";
    public static final String BOUNCES = "ga:bounces";
    public static final String TIME_ON_SITE = "ga:timeOnSite";
    public static final String ENTRANCE_BOUNCE_RATE = "ga:entranceBounceRate";
    public static final String VISIT_BOUNCE_RATE = "ga:visitBounceRate";
    public static final String AVG_TIME_ON_SITE = "ga:avgTimeOnSite";

    /** Visitors Dimensions & Metrics **/
    public static final String VISITORS = "ga:visitors";
    public static final String NEW_VISITS = "ga:newVisits";
    public static final String PERCENT_NEW_VISITS = "ga:percentNewVisits";

    /** Site Speed Dimensions & Metrics **/
    public static final String PAGE_LOAD_TIME = "ga:pageLoadTime";
    public static final String PAGE_LOAD_SAMPLE = "ga:pageLoadSample";

    public static final String DATE = "ga:date";
    public static final String HOUR = "ga:hour";

    private static AnalyticsService service;

    // TODO: Need to optimize following code event and page views parsing

    /**
     *
     * @param entries
     * @param profileId
     * @return
     */
    public static List<Map<String, Object>> parseGAEvents(List<DataEntry> entries, Long profileId) {
        List<Map<String, Object>> events = new LinkedList<Map<String, java.lang.Object>>();
        int count = 1;
        for(DataEntry entry: entries) {
            Map<String, Object> event = null;
            try {
                event = new HashMap<String, Object>();

                // Parse Event TrackingDimensions
                if(entry.getDimension(EVENT_CATEGORY) != null) {
                    String eventCategory = entry.getDimension(EVENT_CATEGORY).getValue();
                    event.put("eventCategory", eventCategory != null?eventCategory:"");
                }
                if(entry.getDimension(EVENT_ACTION) != null) {
                    String eventAction = entry.getDimension(EVENT_ACTION).getValue();
                    event.put("eventAction", eventAction != null?eventAction:"");
                }
                if(entry.getDimension(EVENT_LABEL) != null) {
                    String eventLabel = entry.getDimension(EVENT_LABEL).getValue();
                    event.put("eventLabel", eventLabel != null?eventLabel:"");
                }

                if(entry.getDimension(DATE) != null) {
                    String eventDate = entry.getDimension(DATE).getValue().toString();
                    eventDate = eventDate.substring(0,4) + "-" + eventDate.substring(4, 6) + "-" + eventDate.substring(6, 8);
                    String timeStr = "00:00:00";
                    if(entry.getDimension(HOUR) != null) {
                        event.put("hour", entry.getDimension(HOUR).getValue());
                        timeStr = entry.getDimension(HOUR).getValue() + ":00:00";
                    }
                    eventDate += " " + timeStr;
                    event.put("eventDate", eventDate);
                }

                // Parse Event Tracking Metrics
                if(entry.getMetric(TOTAL_EVENTS) != null) {
                    event.put("totalEvents", entry.getMetric(TOTAL_EVENTS).getValue());
                }
                if(entry.getMetric(UNIQUE_EVENTS)!= null) {
                    event.put("uniqueEvents", entry.getMetric(UNIQUE_EVENTS).getValue());
                }
                if(entry.getMetric(EVENT_VALUE)!= null) {
                    event.put("eventValue", entry.getMetric(EVENT_VALUE).getValue());
                }
                if(entry.getMetric(AVG_EVENT_VALUE) != null) {
                    event.put("avgEventValue", entry.getMetric(AVG_EVENT_VALUE).getValue());
                }
                if(entry.getMetric(EVENTS_PER_VISIT_WITH_EVENT) != null) {
                    event.put("eventsPerVisitWithEvent", entry.getMetric(EVENTS_PER_VISIT_WITH_EVENT).getValue());
                }
                if(entry.getMetric(VISITS_WITH_EVENT) != null) {
                    event.put("visitsWithEvent", entry.getMetric(VISITS_WITH_EVENT).getValue());
                }

                event.put("profileId", profileId);
                event.put("entryIndex", count);
                events.add(event);
                count++;
            } catch (NullPointerException e) {
                throw new NullPointerException(e.getMessage());
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        return events;
    }

    /**
     *
     * @param entries
     * @param profileId
     * @return
     */
    public static List<Map<String, Object>> parseGAPageViews(List<DataEntry> entries, Long profileId) {
        List<Map<String, Object>> pageViewsList = new LinkedList<Map<String, java.lang.Object>>();
        int count = 1;
        for(DataEntry entry: entries) {
            // Process Entries
            Map<String, Object> pageView = null;

            try {
                //Long entrances = 0L;
                Long pageviews = 0L;
                Long uniquePageviews = 0L;
                //Double timeOnPage = 0.0;
                //Long exits = 0L;
                Long visits = 0L;
                Long bounces = 0L;
                Double timeOnSite = 0.0;
                Long visitors = 0L;
                Long newVisits = 0L;
                Long pageLoadTime = 0L;
                Long pageLoadSample = 0L;


                //Double entranceRate = 0.0;
                Double pageviewsPerVisit = 0.0;
                //Double avgTimeOnPage = 0.0;
                //Double exitRate = 0.0;
                //Double entranceBounceRate = 0.0;
                Double visitBounceRate = 0.0;
                Double avgTimeOnSite = 0.0;
                Double percentNewVisits = 0.0;
                Double avgPageLoadTime = 0.0;

                pageView = new HashMap<String, Object>();

                /** Parse Page Tracking Dimensions **/
                if(entry.getDimension(PAGE_TITLE) != null) {
                    pageView.put("pageTitle", entry.getDimension(PAGE_TITLE).getValue());
                }
                if(entry.getDimension(PAGE_PATH) != null) {
                    String pagePath = entry.getDimension(PAGE_PATH).getValue();
                    // TODO: Need to confirm whether to truncate page path to 1000 chars if exceeds ot not
                    if(pagePath.length() > 1000) {
                        pagePath = pagePath.substring(0, 999);
                    }
                    pageView.put("pagePath", pagePath);
                }
                if(entry.getDimension(PAGE_DEPTH) != null) {
                    pageView.put("pageDepth", entry.getDimension(PAGE_DEPTH).getValue());
                }
                if(entry.getDimension(DATE) != null) {
                    String date = entry.getDimension(DATE).getValue().toString();
                    date = date.substring(0,4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
                    if(entry.getDimension(HOUR) != null) {
                        pageView.put("hour", entry.getDimension(HOUR).getValue());
                        date += " " + entry.getDimension(HOUR).getValue() + ":00:00";
                    }
                    pageView.put("date", date);
                }

                /** Parse Page Tracking Metrics **/
                /*if(entry.getMetric(ENTRANCES) != null) {
                    entrances = Long.parseLong(entry.getMetric(ENTRANCES).getValue());
                }*/
                if(entry.getMetric(PAGE_VIEWS) != null) {
                    pageviews = Long.parseLong(entry.getMetric(PAGE_VIEWS).getValue());
                }
                if(entry.getMetric(UNIQUE_PAGE_VIEWS) != null) {
                    uniquePageviews = Long.parseLong(entry.getMetric(UNIQUE_PAGE_VIEWS).getValue());
                }
                /*if(entry.getMetric(TIME_ON_PAGE) != null) {
                    timeOnPage = Double.parseDouble(entry.getMetric(TIME_ON_PAGE).getValue());
                }*/
                /*if(entry.getMetric(EXITS) != null) {
                    exits = Long.parseLong(entry.getMetric(EXITS).getValue());
                }*/

                /** Parse Session Metrics **/
                if(entry.getMetric(VISITS) != null) {
                    visits = Long.parseLong(entry.getMetric(VISITS).getValue());
                }
                if(entry.getMetric(BOUNCES) != null) {
                    bounces = Long.parseLong(entry.getMetric(BOUNCES).getValue());
                }
                if(entry.getMetric(TIME_ON_SITE) != null) {
                    timeOnSite = Double.parseDouble(entry.getMetric(TIME_ON_SITE).getValue());
                }

                /** Parse Visitors Metrics **/
                if(entry.getMetric(VISITORS) != null) {
                    visitors = Long.parseLong(entry.getMetric(VISITORS).getValue());
                }
                if(entry.getMetric(NEW_VISITS) != null) {
                    newVisits = Long.parseLong(entry.getMetric(NEW_VISITS).getValue());
                }

                /** Parse site speed metrics **/
                if(entry.getMetric(PAGE_LOAD_TIME) != null) {
                    pageLoadTime = Long.parseLong(entry.getMetric(PAGE_LOAD_TIME).getValue());
                }
                if(entry.getMetric(PAGE_LOAD_SAMPLE) != null) {
                    pageLoadSample = Long.parseLong(entry.getMetric(PAGE_LOAD_SAMPLE).getValue());
                }

                /** Calculated Page View Tracking Metrics **/
                //ga:entranceRate  = (ga:entrances / ga:pageviews) * 100
                /*if(pageviews != 0) {
                    entranceRate = ((entrances * 1.0) / pageviews) * 100;
                }*/
                //ga:pageviewsPerVisit  = ga:pageviews / ga:visits
                if(visits != 0) {
                    pageviewsPerVisit = (pageviews * 1.0) / visits;
                }
                //ga:avgTimeOnPage = ga:timeOnPage / (ga:pageviews - ga:exits)
                /*if((pageviews - exits)  != 0) {
                    avgTimeOnPage = (timeOnPage * 1.0) / (pageviews - exits);
                }*/
                //ga:exitRate = (ga:exits / ga:pageviews) * 100
                /* if(pageviews != 0) {
                    exitRate = ((exits * 1.0) / pageviews) * 100;
                } */


                /** Calculated Session Metrics **/
                //ga:entranceBounceRate = (ga:bounces / ga:entrances) * 100
               /* if(entrances != 0) {
                    entranceBounceRate = ((bounces * 1.0) / entrances) * 100;
                }*/
                //ga:visitBounceRate = (ga:bounces / ga:visits) * 100
                if(visits != 0) {
                    visitBounceRate = ((bounces * 1.0) / visits) * 100;
                }
                //ga:avgTimeOnSite = ga:timeOnSite / ga:visits
                if(visits != 0) {
                    avgTimeOnSite = (timeOnSite * 1.0) / visits;
                }

                /** Calculated Visitor Metrics **/
                // ga:percentNewVisits = (ga:newVisits / ga:visits) * 100
                if(visits != 0) {
                    percentNewVisits = ((newVisits * 1.0) / visits) * 100;
                }

                /** Calculated Site Speed Metrics **/
                // ga:avgPageLoadTime = (ga:pageLoadTime / ga:pageLoadSample) * 0.001
                if(pageLoadSample != 0) {
                    avgPageLoadTime = ((pageLoadTime * 1.0) / pageLoadSample) * 0.001;
                }


                //pageView.put("entrances", entrances);
                pageView.put("pageviews", pageviews);
                pageView.put("uniquePageviews", uniquePageviews);
                //pageView.put("timeOnPage", timeOnPage);
                //pageView.put("exits", exits);
                pageView.put("visits", visits);
                pageView.put("bounces", bounces);
                pageView.put("timeOnSite", timeOnSite);
                pageView.put("visitors", visitors);
                pageView.put("newVisits", newVisits);
                pageView.put("pageLoadTime", pageLoadTime);
                pageView.put("pageLoadSample", pageLoadSample);

                //pageView.put("entranceRate", entranceRate);
                pageView.put("pageviewsPerVisit", pageviewsPerVisit);
                //pageView.put("avgTimeOnPage", avgTimeOnPage);
                //pageView.put("exitRate", exitRate);
                //pageView.put("entranceBounceRate", entranceBounceRate);
                pageView.put("visitBounceRate", visitBounceRate);
                pageView.put("avgTimeOnSite", avgTimeOnSite);
                pageView.put("percentNewVisits", percentNewVisits);
                pageView.put("avgPageLoadTime", avgPageLoadTime);

                pageView.put("profileId", profileId);
                pageView.put("entryIndex", count);

                pageViewsList.add(pageView);
                count++;
            } catch (NullPointerException e) {
                throw new NullPointerException(e.getMessage());
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

        }
        return pageViewsList;
    }

    /**
     * Get google analytics data query
     * @param endPoint
     * @return
     * @throws MalformedURLException
     */
    public static DataQuery getDataQuery(String endPoint) throws MalformedURLException {
        DataQuery dataQuery = null;
        try {
            // Construct Data Query
            String metrics = null;
            String dimensions =  null;
            if(endPoint.equals(DataSchedulerBatch.EndPoint.GA_EVENTS)) {
                metrics = GAProperties.EVENT_METRICS;
                dimensions = GAProperties.EVENT_DIMENSIONS;
            } else if(endPoint.equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)) {
                metrics = GAProperties.PAGE_VIEW_METRICS;
                dimensions = GAProperties.PAGE_VIEW_DIMENSIONS;
            }
            dataQuery = new DataQuery(new URL(GAProperties.DATA_FEED));
            dataQuery.setMetrics(metrics);
            dataQuery.setDimensions(dimensions);
            dataQuery.setSort("ga:date,ga:hour");
            dataQuery.setStringCustomParameter("key",GAProperties.API_KEY);

        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        }
        return dataQuery;
    }


    /**
     * Get google analytics data query
     * @param url
     * @param profileId
     * @param startDate
     * @param endDate
     * @param metrics
     * @param dimensions
     * @return
     * @throws MalformedURLException
     */
    public static DataQuery getDataQuery(String url, String profileId, String startDate, String endDate, String metrics, String dimensions) throws MalformedURLException {
        DataQuery dataQuery = null;
        try {
            dataQuery = new DataQuery(new URL(url));
            dataQuery.setMetrics(metrics);
            dataQuery.setDimensions(dimensions);
            dataQuery.setStartDate(startDate);
            dataQuery.setEndDate(endDate);
            dataQuery.setSort("ga:date,ga:hour");
            dataQuery.setIds("ga:" + profileId);
            dataQuery.setStringCustomParameter("key",GAProperties.API_KEY);

        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        }
        return dataQuery;
    }

    /**
     * Get google analytics data query
     * @param url
     * @param profileId
     * @param startDate
     * @param endDate
     * @param metrics
     * @param dimensions
     * @return
     * @throws MalformedURLException
     */
    public static DataQuery getDataQuery(String url, String profileId, Date startDate, Date endDate, String metrics, String dimensions) throws MalformedURLException {
        DataQuery dataQuery = null;
        try {
            dataQuery = new DataQuery(new URL(url));
            dataQuery.setMetrics(metrics);
            dataQuery.setDimensions(dimensions);
            dataQuery.setStartDate(DateUtils.getFormattedDate(startDate, "yyyy-MM-dd"));
            dataQuery.setEndDate(DateUtils.getFormattedDate(endDate, "yyyy-MM-dd"));
            dataQuery.setIds("ga:" + profileId);
            dataQuery.setSort("ga:date,ga:hour");
            String filter = getHourFilter(startDate, endDate);
            if(filter != null) {
                dataQuery.setFilters(filter);
            }
            dataQuery.setStringCustomParameter("key",GAProperties.API_KEY);



        } catch (MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        }
        return dataQuery;
    }

    /**
     *  Get hours to be filtered while querying to google analytics api
     *
     * @param startDate
     * @param endDate
     * @return - comma separated string (ex: ga:hour==00,ga:hour==01,ga:hour==13....)
     */
    // TODO: Need to verify if we can able construct string such that
    // TODO: hour between ga:hour>=start hour and ga:hour<=end hour
    // TODO: instead constructing string as comma separated with all the hour between start hour and end hour
    public static String getHourFilter(Date startDate, Date endDate) {
        String filter = null;
        String formattedStartDate = DateUtils.getFormattedDate(startDate, "yyyy-MM-dd");
        String formattedEndDate = DateUtils.getFormattedDate(endDate, "yyyy-MM-dd");
        if(formattedStartDate.equals(formattedEndDate)
                && (endDate.getHours() - startDate.getHours()) < 23) {
            long startHr = startDate.getHours();
            long endHr = endDate.getHours();
            for(long hr = startHr; hr <= endHr; hr++) {
                if(filter == null) filter = "";
                filter += "ga:hour=="+ (hr < 10?("0" + hr):hr);
                if(hr < endHr) filter += ",";
            }
        }
        return filter;
    }

    public static String getEntryDateTime(DataEntry entry) {
        String dateTime = null;
        if(entry != null) {
            String date = entry.getDimension("ga:date").getValue();
            String hour = entry.getDimension("ga:hour").getValue();
            dateTime = date.substring(0,4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) + " " + hour + ":00:00";
        }
        return dateTime;
    }

    /**
     * Get google analytics data feed
     * @param url
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static DataFeed getDataFeed(URL url)
            throws IOException, ServiceException {
        DataFeed feed = null;
        try {
            DataQuery dataQuery = new DataQuery(url);
            feed = getGAService().getFeed(dataQuery, DataFeed.class);
        } catch (ServiceException e) {
            logger.error("getDataFeed :: URL :: "+url.toString());
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return feed;
    }

    /**
     * Get google analytics account feed
     * @param url
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public static ManagementFeed getAccountFeed(URL url)
            throws IOException, ServiceException  {
        ManagementFeed feed = null;

        try {
            DataQuery dataQuery = new DataQuery(url);
            dataQuery.setStringCustomParameter("key",GAProperties.API_KEY);
            feed = getGAService().getFeed(dataQuery, ManagementFeed.class);
        } catch (ServiceException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
        return feed;
    }

    /**
     * Get google analytics service object
     * @return
     * @throws AuthenticationException
     */
    public static AnalyticsService getGAService() throws AuthenticationException {
        //  AnalyticsService service = null;
        try {
            if(service == null) {
                service = new AnalyticsService(GAProperties.APPLICATION_NAME);
                service.setUserCredentials(GAProperties.USERNAME, GAProperties.PASSWORD);
            }
        } catch (AuthenticationException e) {
            throw new AuthenticationException(e.getMessage());
        }
        return service;
    }


}
