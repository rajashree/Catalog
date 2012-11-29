package com.dell.dw.managers.dataimport.service;

import com.dell.dw.managers.dataimport.util.DateUtils;
import com.dell.dw.managers.dataimport.util.GADataUtils;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.google.gdata.client.analytics.DataQuery;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.ServiceException;
import com.sourcen.dataimport.service.DataReader;
import com.sourcen.dataimport.service.support.BaseDataAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: bhaskara
 * Date: 5/28/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class GADataReader
        extends BaseDataAdapter implements DataReader {

    @Override
    public Collection<Map<String, Object>> getRows() {
        String endPoint = tableDefinition.getProperty("endPoint");
        Long profileId = tableDefinition.getLongProperty("profileId");
        Date startDate = DateUtils.getDate(tableDefinition.getProperty("startDate"));
        Date endDate = DateUtils.getDate(tableDefinition.getProperty("endDate"));
        Integer startIndex = tableDefinition.getIntegerProperty("startIndex");
        Integer maxResults = tableDefinition.getIntegerProperty("maxResults");
        try {
            DataQuery dataQuery = GADataUtils.getDataQuery(endPoint);
            dataQuery.setStartDate(DateUtils.getFormattedDate(startDate, "yyyy-MM-dd"));
            dataQuery.setEndDate(DateUtils.getFormattedDate(endDate,"yyyy-MM-dd"));
            dataQuery.setIds("ga:" + profileId);
            dataQuery.setStartIndex(startIndex);
            dataQuery.setMaxResults(maxResults);
            String filter = GADataUtils.getHourFilter(startDate, endDate);
            if(filter != null) {
                dataQuery.setFilters(filter);
            }
            logger.error("GA Data feed - Start Date :: "+startDate.toString());
            logger.error("GA Data feed - End Date :: "+endDate.toString());
            logger.error("GA Data feed - Url :: "+dataQuery.getUrl());

            DataFeed feed = GADataUtils.getDataFeed(dataQuery.getUrl());
            if(endPoint.equals(DataSchedulerBatch.EndPoint.GA_EVENTS)) {
                return GADataUtils.parseGAEvents(feed.getEntries(), profileId);
            } else if(endPoint.equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)) {
                return GADataUtils.parseGAPageViews(feed.getEntries(), profileId);
            }
        } catch (NullPointerException e) {
            this.getExceptionHandler().onDataReaderException(e);
        } catch (ServiceException se) {
            this.getExceptionHandler().onDataReaderException(se);
        } catch (MalformedURLException me) {
            this.getExceptionHandler().onDataReaderException(me);
        } catch (IOException ie) {
            this.getExceptionHandler().onDataReaderException(ie);
        }
        return null;
    }

    @Override
    public void processRowAfterExtraction(Map<String, Object> record, Integer recordIndex) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
