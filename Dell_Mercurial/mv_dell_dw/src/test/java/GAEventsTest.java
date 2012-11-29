import com.dell.dw.managers.dataimport.util.DateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 6/14/12
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GAEventsTest {
    @Test
    public void getEvents() {

        Long totalEvents = 0L;
        Long uniqueEvents = 0L;
        Long eventValue = 0L;
        Double avgEventValue = 0.0;
        Long visitsWithEvent = 0L;
        Double visitsPerEvent = 0.0;
        int count = 0;
        Date startDate = DateUtils.getDate("2012-01-01 10:00:00");
        Date endDate = new Date();
        endDate = DateUtils.getDate("2012-01-01 24:00:00");
        List<DateRange> datesToBeProcessed = new ArrayList<DateRange>();
        String formattedStartDate = DateUtils.getFormattedDate(startDate, "yyyy-MM-dd");
        String formattedEndDate = DateUtils.getFormattedDate(endDate, "yyyy-MM-dd");
        // If it is not same day
        if(!formattedStartDate.equals(formattedEndDate)) {
            // If Start Date in the middle of the Day then separate
            if(startDate.getHours() > 0 && startDate.getHours() <= 23) {
                datesToBeProcessed.add(getDateRange(startDate, DateUtils.getEndOfDay(startDate)));
                startDate = DateUtils.getNextDayStart(startDate);
            }

            // If End date in the middle of the Day the separate
            if(endDate.getHours() >= 0 && endDate.getHours() < 23) {
                datesToBeProcessed.add(getDateRange(DateUtils.getStartOfDay(endDate), endDate));
                endDate = DateUtils.getPreviousDayEnd(endDate);
            }

            if(endDate.getTime() > startDate.getTime()) {
                datesToBeProcessed.add((datesToBeProcessed.size() > 0?datesToBeProcessed.size() - 1:0),getDateRange(startDate, endDate));
            }

        } else {
            datesToBeProcessed.add(getDateRange(startDate, endDate));
        }

        System.out.print("Length ::: " + datesToBeProcessed.size() + "\n");
        for(DateRange range : datesToBeProcessed) {
            System.out.println(range.startDate.toString() + " :: " + range.endDate.toString() + "\n");
        }




        //try {



//            AnalyticsService service = GADataUtils.getGAService();
//
//            DataQuery dataQuery = new DataQuery(new URL("https://www.google.com/analytics/feeds/data"));
//            dataQuery.setMetrics(GAProperties.EVENT_METRICS);
//            //dataQuery.setDimensions("ga:eventCategory,ga:eventAction,ga:eventLabel");
//            dataQuery.setDimensions(GAProperties.EVENT_DIMENSIONS); // 12170 & 3660
//            //dataQuery.setDimensions("ga:eventAction"); // 12170 & 4711
//            // dataQuery.setDimensions("ga:eventLabel"); // 11126 & 7692
//            Date startDate = DateUtils.getDate("2012-06-26", "yyyy-MM-dd");
//            Date endDate = DateUtils.getDate("2012-06-26", "yyyy-MM-dd");
//            dataQuery.setStartDate(DateUtils.getFormattedDate(startDate, "yyyy-MM-dd"));
//            dataQuery.setEndDate(DateUtils.getFormattedDate(endDate, "yyyy-MM-dd"));
//            // dataQuery.setFilters("ga:hour==16,ga:hour==17");
//            dataQuery.setSort("ga:date,ga:hour");
//            //60700927, 58807163
//            dataQuery.setIds("ga:" + 59657109);
//
//            DataFeed feed = service.getFeed(dataQuery, DataFeed.class);
//
//            int totalResults = feed.getTotalResults();
//            System.out.println("Total Results :: " + totalResults);
//            int batch = 100;
//            float splits = (float) totalResults/batch;
//
//            int iterations = (int) splits;
//            int remaining = Math.round((splits - iterations) * 100);
//            if(remaining > 0) iterations += 1;
//
//            //System.out.println("Total Records fetched ::: " + count);
//            System.out.println("Processing...");
//
//            for(int i = 0; i < iterations; i++) {
//                dataQuery.setStartIndex((i*batch + 1));
//                dataQuery.setMaxResults((i == iterations)?remaining:batch);
//                feed = service.getFeed(dataQuery.getUrl(), DataFeed.class);
//                for(DataEntry entry : feed.getEntries()) {
//                    System.out.println(entry.getDimension("ga:eventCategory").getValue() + " ");
//                    System.out.println(entry.getDimension("ga:eventAction").getValue() + " ");
//                    System.out.println(entry.getDimension("ga:eventLabel").getValue() + " ");
//                    System.out.println(entry.getDimension("ga:date").getValue() + " ");
//                    System.out.println(entry.getDimension("ga:hour").getValue() );
//                    //System.out.println("test ::: " + entry.getDimension("ga:eventAction"));
////                    totalEvents += Long.parseLong(entry.getMetric("ga:totalEvents").getValue());
////                    uniqueEvents += Long.parseLong(entry.getMetric("ga:uniqueEvents").getValue());
////                    eventValue += Long.parseLong(entry.getMetric("ga:eventValue").getValue());
////                    avgEventValue += Double.parseDouble(entry.getMetric("ga:avgEventValue").getValue());
////                    visitsWithEvent += Long.parseLong(entry.getMetric("ga:visitsWithEvent").getValue());
////                    visitsPerEvent += Double.parseDouble(entry.getMetric("ga:eventsPerVisitWithEvent").getValue());
//                    count++;
//                }
//            }
////            System.out.println("================================");
////            System.out.println("Results");
////            System.out.println("================================");
////            System.out.println("Total Events ::: " + totalEvents);
////            System.out.println("Unique Events ::: " + uniqueEvents);
////            System.out.println("Event Value ::: " + eventValue);
//
//
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private DateRange getDateRange(Date startDate,Date endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(new Date(startDate.getTime()));
        dateRange.setEndDate(new Date(endDate.getTime()));
        return dateRange;
    }


    @Test
    public void testHoursDiff() {
        Date startDate = DateUtils.getDate("2012-05-19 00:00:00");
        Date endDate = DateUtils.getDate("2012-06-19 24:00:00");
        List<Long> hours = new ArrayList<Long>();
//        if(DateUtils.getDayDiff(startDate, endDate) > 1) {
//            long endDayHr = endDate.getHours();
//            if(endDayHr < 24) {
//                long time = endDate.getTime() - (1000 * 60 * 60 * 24);
//                Date date = new Date(time);
//
//            } else {
//             // Take Whole hours
//            }
//        } else {
//
//        }
        System.out.println(endDate.getHours());
    }

    private class DateRange {
        public Date startDate;
        public Date endDate;

        public Date getStartDate() {
            return startDate;
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getEndDate() {
            return endDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }
    }
}
