package com.dell.dw.managers;

import com.dell.dw.managers.dataimport.util.DateRange;
import com.dell.dw.managers.dataimport.util.DateUtils;
import com.dell.dw.managers.dataimport.util.GADataUtils;
import com.dell.dw.managers.dataimport.util.GAProperties;
import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.domain.GAWebPropertyProfile;
import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.dell.dw.persistence.repository.DataSourceRepository;
import com.dell.dw.persistence.repository.GAWebPropertyProfileRepository;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.google.gdata.data.analytics.DataFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/28/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GADataSchedulerManagerImpl implements GADataSchedulerManager{
    private static final Logger logger =  LoggerFactory.getLogger(GADataSchedulerManagerImpl.class);

    //DateUtils.getDate("2012-06-18 17:00:00", "yyyy-MM-dd HH:mm:ss");
    private Date currentDateTime = new Date();

    /**
     * Get unprocessed google analytics profiles
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public List<GAWebPropertyProfile> getUnprocessedGAProfiles() {
        List<GAWebPropertyProfile> properties = gaWebPropertyProfileRepository.getUnprocessedProfiles();
        return properties;
    }

    /**
     * Process scheduler batches for a given google analytics profile
     * @param profile
     */
    @Override
    @Transactional(readOnly = false)
    public void updateSchedulerBatches(final GAWebPropertyProfile profile) {
        GAWebPropertyProfile lockedProfile = profile;
        if (!profile.getStatus().equals(GAWebPropertyProfile.Status.PROCESSING)) {
            lockedProfile = gaWebPropertyProfileRepository.acquireLock(profile,
                    profile.getStatus(), GAWebPropertyProfile.Status.PROCESSING);
            if (lockedProfile == null) {
                logger.info("Unable to lock object from IN_QUEUE TO PROCESSING  objID:="
                        + profile + " as it was locked by " + profile.getLockedThread());
            }
        }

        if(lockedProfile != null){
            String loggerName = "com.dell.dw.dataimport.ga.logs."+lockedProfile.getId();
            Logger updateGABatchesLogger = LoggerFactory.getLogger(loggerName);
            updateGABatchesLogger.info("starting to process batches data for GA Profile :=" + lockedProfile.getId());

            gaWebPropertyProfileRepository.refresh(lockedProfile);

            Integer webPropertyStatus = lockedProfile.getStatus();
            Integer updateScheduleBatchesStatus = GAWebPropertyProfile.Status.DONE;

            // Process if ga event tracking is enabled
            if(GAProperties.EVENT_TRACKING_ENABLED) {
                updateScheduleBatchesStatus = processBatches(lockedProfile, DataSchedulerBatch.EndPoint.GA_EVENTS);
            } else {
                logger.info("GA Event Tracking is not enabled.....!");
            }

            // Process if ga page view tracking is enabled
            if(GAProperties.PAGE_VIEW_TRACKING_ENABLED) {
                updateScheduleBatchesStatus = processBatches(lockedProfile, DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS);
            } else {
                logger.info("GA Page View Tracking is not enabled.....!");
            }
            if(updateScheduleBatchesStatus.equals(GAWebPropertyProfile.Status.DONE)) {
                gaWebPropertyProfileRepository.updateLastDownloadedDate(lockedProfile.getId(),currentDateTime);
                gaWebPropertyProfileRepository.acquireLock(lockedProfile,
                        lockedProfile.getStatus(), GAWebPropertyProfile.Status.DONE);
            } else {
                gaWebPropertyProfileRepository.acquireLock(lockedProfile,
                        lockedProfile.getStatus(), updateScheduleBatchesStatus);
            }
        }
    }


    private Integer processBatches(GAWebPropertyProfile lockedProfile, String endpoint) {
        Integer updateBatchesStatus = GAWebPropertyProfile.Status.DONE;
        logger.info("Initializing GA Scheduler batches for " + endpoint + " for the Profile :: " + lockedProfile.getName());
        Date initializationDate = gaWebPropertyProfileRepository.getInitializationDate(lockedProfile.getId());
        Date initialBatchStartDate = dataSchedulerBatchRepository.getInitialBatchStartDate(lockedProfile.getId(), endpoint);
        Date lastBatchEndDate = dataSchedulerBatchRepository.getLastBatchEndDate(lockedProfile.getId(), endpoint);
        currentDateTime = DateUtils.getDateInTimeZone(new Date(), lockedProfile.getTimezone());

        // Process form initialization date if no records/batches found in the data scheduler table
        if(lastBatchEndDate == null) {
            lastBatchEndDate = new Date(initializationDate.getTime());
        }

        // Start processing batches
        if(lastBatchEndDate != null){
            logger.info("Started processing scheduler batches of GA " + endpoint + " for the Profile :: " + lockedProfile.getName());
            long hrDiff = DateUtils.getHourDiff(lastBatchEndDate, currentDateTime);
            long interval = configurationService.getLongProperty("analytics.gaDataSchedulerRepeatTimeInterval",1440000L)/(1000 * 60 * 60);
            if(hrDiff >= 1 && hrDiff >= interval) {
                updateBatchesStatus = executeBatches(lockedProfile, endpoint, lastBatchEndDate, currentDateTime);
            }
        }

        // Process if any previous days left
        if(initialBatchStartDate != null
                && DateUtils.getHourDiff(initializationDate, initialBatchStartDate) >= 1) {
            logger.info("Started processing left dates scheduler batches of GA " + endpoint + " for the Profile :: " + lockedProfile.getName());
            updateBatchesStatus = executeBatches(lockedProfile, endpoint, initializationDate, DateUtils.getPreviousHourDate(initialBatchStartDate));
        }


        return updateBatchesStatus;
    }


    /**
     *
     * @param lockedProfile
     * @param endpoint
     * @param startDate
     * @param endDate
     * @return
     */
    private Integer executeBatches(GAWebPropertyProfile lockedProfile, String endpoint, Date startDate, Date endDate) {

        Integer updateBatchesStatus = GAWebPropertyProfile.Status.DONE;

        long batchCount = configurationService.getLongProperty("analytics.gaBatchCount");
        DataSource ds = dataSourceRepository.get(DataSource.DSConstants.GA);
        Retailer retailer = lockedProfile.getGaWebProperty().getGaAccount().getRetailer();
        int iteration = 0;

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

            // Remaining between dates if any
            if(endDate.getTime() > startDate.getTime()) {
                datesToBeProcessed.add((datesToBeProcessed.size() > 0?datesToBeProcessed.size() - 1:0),getDateRange(startDate, endDate));
            }

        } else { // if it is same day
            datesToBeProcessed.add(getDateRange(startDate, endDate));
        }

        for(DateRange dateRange: datesToBeProcessed) {
            try {
                logger.info("Executing GA " + endpoint + " Scheduler batches for Profile :: " + lockedProfile.getName());
                DataFeed feed = GADataUtils.getDataFeed(
                        GADataUtils.getDataQuery(
                                GAProperties.DATA_FEED,
                                lockedProfile.getId().toString(),
                                dateRange.getStartDate(),
                                dateRange.getEndDate(),
                                (endpoint.equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)?GAProperties.PAGE_VIEW_METRICS:GAProperties.EVENT_METRICS),
                                (endpoint.equals(DataSchedulerBatch.EndPoint.GA_PAGE_VIEWS)?GAProperties.PAGE_VIEW_DIMENSIONS:GAProperties.EVENT_DIMENSIONS)).getUrl());
                if(feed != null && feed.getEntries() != null && feed.getEntries().size() > 0) {
                    Date lastEntryDate = DateUtils.getDate(GADataUtils.getEntryDateTime(feed.getEntries().get(feed.getEntries().size() - 1)));
                    Date now = DateUtils.getDateInTimeZone(new Date(), lockedProfile.getTimezone());
                    // Check if last entry date hour is same as current date hour
                    if(DateUtils.getHourDiff(lastEntryDate, now) < 1
                            && DateUtils.getHourDiff(startDate, lastEntryDate) >= 1) {
                        lastEntryDate = DateUtils.getPreviousHourDate(lastEntryDate);
                    }

                    long totalEventCount = feed.getTotalResults();
                    if(totalEventCount > 0){
                        long counter = totalEventCount/batchCount;
                        DataSchedulerBatch obj = null;
                        Date dateTillProcessed = (iteration == (datesToBeProcessed.size()-1))?lastEntryDate:dateRange.getEndDate();
                        for(long i = 0; i < counter; i++){
                            obj = new DataSchedulerBatch(ds, lockedProfile.getId(), dateRange.getStartDate(), dateTillProcessed, i*batchCount+1, batchCount, endpoint, 0, DataSchedulerBatch.Status.IN_QUEUE, retailer, StringUtils.getSimpleString("/ga/"+lockedProfile.getName()+"_"+endpoint).toLowerCase());
                            dataSchedulerBatchRepository.insert(obj);
                        }
                        obj = new DataSchedulerBatch(ds, lockedProfile.getId(), dateRange.getStartDate(), dateTillProcessed, counter*batchCount+1, totalEventCount - (counter*batchCount), endpoint, 0, DataSchedulerBatch.Status.IN_QUEUE, retailer,StringUtils.getSimpleString("/ga/"+lockedProfile.getName()+"_"+endpoint).toLowerCase());
                        dataSchedulerBatchRepository.insert(obj);
                    }
                }

            } catch (AuthenticationException e) {
                updateBatchesStatus = GAWebPropertyProfile.Status.AUTHENCATION_FAILURE;
                logger.error(e.getMessage(), e);
            } catch (ServiceException e) {
                updateBatchesStatus = GAWebPropertyProfile.Status.ERROR_READ;
                logger.error(e.getMessage(), e);
            } catch (MalformedURLException e) {
                updateBatchesStatus = GAWebPropertyProfile.Status.ERROR_READ;
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                updateBatchesStatus = GAWebPropertyProfile.Status.ERROR_READ;
                logger.error(e.getMessage(), e);
            }
            iteration++;
        }
        return updateBatchesStatus;
    }

    private DateRange getDateRange(Date startDate,Date endDate) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(new Date(startDate.getTime()));
        dateRange.setEndDate(new Date(endDate.getTime()));
        return dateRange;
    }


    @Autowired
    GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    @Autowired
    DataSchedulerBatchRepository dataSchedulerBatchRepository;

    @Autowired
    DataSourceRepository dataSourceRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    ConfigurationServiceImpl configurationService;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }

    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    public ConfigurationServiceImpl getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationServiceImpl configurationService) {
        this.configurationService = configurationService;
    }
}

