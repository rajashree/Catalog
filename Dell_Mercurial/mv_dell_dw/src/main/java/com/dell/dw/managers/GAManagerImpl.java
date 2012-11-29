package com.dell.dw.managers;

import com.dell.dw.managers.dataimport.util.DateUtils;
import com.dell.dw.persistence.domain.*;
import com.dell.dw.persistence.repository.*;
import com.dell.dw.web.controller.formbeans.AppHealthBean;
import com.dell.dw.web.controller.formbeans.GAWebPropertyBean;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Devashree
 * Date: 6/13/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GAManagerImpl implements GAManager{
    @Transactional
    @Override
    public GAAccount createGAAccount(GAAccount gaAccount) {
        gaAccountRepository.insert(gaAccount);
        return gaAccount;
    }

    @Transactional
    @Override
    public GAWebProperty createGAWebProperty(GAWebProperty gaWebProperty) {
        gaWebPropertyRepository.insert(gaWebProperty);
        return gaWebProperty;
    }

    @Transactional
    @Override
    public GAWebPropertyProfile createGAWebPropertyProfile(GAWebPropertyProfile gaWebPropertyProfile) {
        gaWebPropertyProfileRepository.insert(gaWebPropertyProfile);
        return gaWebPropertyProfile;
    }

    @Override
    public Retailer createRetailer(Retailer newRetailer) {
        Retailer retailer = null;
        try {
            retailer = retailerRepository.getByRetailerId(newRetailer.getRetailerId());
            if(retailer == null) {
                retailer = (Retailer) newRetailer.clone();
                retailerRepository.insert(retailer);
            }
        } catch (ConstraintViolationException e) {

        }
        return retailer;
    }

    @Override
    public GAWebPropertyProfile getGAWebPropertyProfile(Long webPropertyProfileId, String webPropertyId, Long accountId) {
        return  gaWebPropertyProfileRepository.getWebPropertyProfile(webPropertyProfileId, webPropertyId, accountId);
    }

    @Override
    public GAWebPropertyProfile getGAWebPropertyProfile(Long webPropertyProfileId) {
        return  gaWebPropertyProfileRepository.get(webPropertyProfileId);
    }

    @Override
    public List<GAWebPropertyProfile> getAllWebPropertyProfiles() {
        return gaWebPropertyProfileRepository.getAll();
    }

    @Transactional
    @Override
    public void updateWebPropertyStatus(String webPropertyId, boolean active) {
        GAWebProperty webProperty = gaWebPropertyRepository.getById(webPropertyId);
        webProperty.setActive(active);
        gaWebPropertyRepository.update(webProperty);
    }

    @Transactional
    @Override
    public List<GAAccount> getGAAccounts() {
        return gaAccountRepository.getAll();
    }

    @Transactional
    @Override
    public void updateEmail(Long accountId, String email) {
        GAAccount gaAccount = gaAccountRepository.get(accountId);
        if(gaAccount != null) {
            gaAccount.setEmail(email);
            gaAccountRepository.update(gaAccount);
        }
    }

    @Transactional
    @Override
    public List<GAWebProperty> getAllWebProperties() {
        return gaWebPropertyRepository.getAll();
    }

    @Transactional
    @Override
    public void updateInitializationDate(Long profileId, Date newDate, boolean delPreviousDate) {
        GAWebPropertyProfile webPropertyProfile = gaWebPropertyProfileRepository.get(profileId);
        Date previousDate = null;
        if(webPropertyProfile != null) {
            previousDate = webPropertyProfile.getInitializationDate();
            webPropertyProfile.setInitializationDate(newDate);
            gaWebPropertyProfileRepository.update(webPropertyProfile);
        }
        if(delPreviousDate){
            removeEventsTillDate(profileId,newDate);
            removePageViewsTillDate(profileId,newDate);
            removeDataSchedulerBatchesTillDate(profileId,newDate,previousDate);
        }


    }

    @Override
    public void removeEventsTillDate(Long profileId, Date date) {
        //gaEventMetricsRepository.removeTillDate(profileId, date);
        List<GAEventDimension> eventDimensions = gaEventDimensionRepository.getEventDimensions(profileId);
        if(eventDimensions != null) {
            for(GAEventDimension eventDimension: eventDimensions) {
                if(eventDimension != null) {
                    List<GAEventMetrics> eventMetricsList = gaEventMetricsRepository.getEventMetricsTillDate(eventDimension.getId(), date);
                    if(eventMetricsList != null){
                        for(GAEventMetrics eventMetrics: eventMetricsList) {
                            StringBuilder compositeValuesStr = new StringBuilder();
                            compositeValuesStr.append(eventDimension.getEventCategory()).append(",");
                            compositeValuesStr.append(eventDimension.getEventAction()).append(",");
                            compositeValuesStr.append(eventDimension.getEventLabel()).append(",");
                            compositeValuesStr.append(profileId).append(",");
                            compositeValuesStr.append(DateUtils.getFormattedDate(eventMetrics.getDate(),"yyyy-MM-dd HH:mm:ss"));
                            String hash = StringUtils.MD5Hash(compositeValuesStr.toString());
                            Boolean success = dataImportLookupRepository.deleteRecord(hash, GAEventMetrics.class.getCanonicalName());
                            try {
                                gaEventMetricsRepository.remove(eventMetrics);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removePageViewsTillDate(Long profileId, Date date) {
        //gaPageViewMetricsRepository.removeTillDate(profileId, date);
        List<GAPageViewDimension> pageViewDimensions = gaPageViewDimensionRepository.getPageViewDimensions(profileId);
        if(pageViewDimensions != null) {
            for(GAPageViewDimension pageViewDimension: pageViewDimensions) {
                if(pageViewDimension != null) {
                    List<GAPageViewMetrics>  pageViewMetricsList = gaPageViewMetricsRepository.getPageViewMetricsTillDate(pageViewDimension.getId(), date);
                    if(pageViewMetricsList != null){
                        for(GAPageViewMetrics pageViewMetrics: pageViewMetricsList) {
                            StringBuilder compositeValuesStr = new StringBuilder();
                            compositeValuesStr.append(pageViewDimension.getPageTitle()).append(",");
                            compositeValuesStr.append(pageViewDimension.getPagePath()).append(",");
                            compositeValuesStr.append(pageViewDimension.getPageDepth()).append(",");
                            compositeValuesStr.append(profileId).append(",");
                            compositeValuesStr.append(DateUtils.getFormattedDate(pageViewMetrics.getDate(),"yyyy-MM-dd HH:mm:ss"));
                            String hash = StringUtils.MD5Hash(compositeValuesStr.toString());
                            Boolean success = dataImportLookupRepository.deleteRecord(hash, GAPageViewMetrics.class.toString());
                            try {
                                gaPageViewMetricsRepository.remove(pageViewMetrics);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeDataSchedulerBatchesTillDate(Long profileId, Date newDate, Date previousDate) {
        dataSchedulerBatchRepository.removeGABatchesTillDate(profileId, newDate);
        List<DataSchedulerBatch> batches = dataSchedulerBatchRepository.getInitialBatches(profileId, DataSchedulerBatch.EndPoint.GA_EVENTS);
        if(batches != null) {
            for(DataSchedulerBatch batch: batches) {
                if(DateUtils.getDayDiff(previousDate, newDate) > 0){
                    batch.setStartDate(newDate);
                }
                dataSchedulerBatchRepository.put(batch);
            }
        }
    }

    /**
     *
     * @param webProperty
     */
    @Transactional
    @Override
    public void synchronize(GAWebPropertyBean webProperty) {

        Retailer exampleObj = new Retailer();
        exampleObj.setName(configurationService.getProperty("app.retailer.name","dell"));
        Retailer retailer = (Retailer) retailerRepository.getUniqueByExample(exampleObj);

        // Save GAAccount
        GAAccount gaAccount = gaAccountRepository.get(webProperty.getAccountId());
        if(gaAccount == null) {
            gaAccount = new GAAccount(webProperty.getAccountId(),webProperty.getAccountName(),retailer);
            gaAccountRepository.insert(gaAccount);
        }

        if(gaAccount == null) return;

        // Save GAWebProperty
        GAWebProperty gaWebProperty = gaWebPropertyRepository.getById(webProperty.getWebPropertyId());
        if(gaWebProperty == null) {
            gaWebProperty = new GAWebProperty(webProperty.getWebPropertyId(), gaAccount, webProperty.getProfileName(), webProperty.getActive());
            gaWebPropertyRepository.insert(gaWebProperty);
        }

        if(gaWebProperty == null) return;

        // Save GAWebPropertyProfile
        GAWebPropertyProfile gaWebPropertyProfile = gaWebPropertyProfileRepository.getWebPropertyProfile(
                webProperty.getProfileId(), gaWebProperty.getId(), gaAccount.getId());

        if(gaWebPropertyProfile == null) {
            gaWebPropertyProfile = new GAWebPropertyProfile(webProperty.getProfileId(), gaWebProperty, webProperty.getProfileName(),
                    webProperty.getInitializationDate(), webProperty.getInitializationDate(),
                    GAWebPropertyProfile.Status.IN_QUEUE, webProperty.getTimezone());
            gaWebPropertyProfileRepository.insert(gaWebPropertyProfile);
        }
    }

    @Transactional
    @Override
    public List<AppHealthBean> getAppHealthDetails() {
        List<AppHealthBean> details = new ArrayList<AppHealthBean>();
        List<GAWebPropertyProfile> profiles = gaWebPropertyProfileRepository.getAll();

        for(GAWebPropertyProfile profile : profiles) {
            if(profile.getGaWebProperty().getActive()) {
                AppHealthBean appHealthBean = new AppHealthBean();
                appHealthBean.setAppName(profile.getName());

                Object[] todayPageViews = gaPageViewMetricsRepository.getTodayPageViews(profile.getId());
                if(todayPageViews != null) {
                    if(todayPageViews[0] != null) {
                        appHealthBean.setPageViewsToday(Long.parseLong(todayPageViews[0].toString()));
                    }
                    if(todayPageViews[1] != null) {
                        appHealthBean.setLoadTime(Double.parseDouble(todayPageViews[1].toString()));
                    }
                }

                DecimalFormat df = new DecimalFormat("#########0.00");

                // Parse Dialy Page views
                List<Long> dailyPageViewsList = gaPageViewMetricsRepository.getDailyPageViews(profile.getId());
                if(dailyPageViewsList != null && dailyPageViewsList.size() > 0) {
                    Long totalPageViews = 0L;
                    for(Long dailyPageViews : dailyPageViewsList) {
                        totalPageViews += dailyPageViews;
                    }
                    appHealthBean.setAvgDailyPageViews(Double.parseDouble(df.format((totalPageViews * 1.0)/dailyPageViewsList.size())));
                }

                Object avgLoadTime = gaPageViewMetricsRepository.getDailyAvgLoadTime(profile.getId());
                if(avgLoadTime != null)
                    appHealthBean.setAvgLoadTime(Double.parseDouble(df.format(avgLoadTime)));
                else
                    appHealthBean.setAvgLoadTime(0.0);

                details.add(appHealthBean);
            }
        }
        return details;
    }

    @Autowired
    GAAccountRepository gaAccountRepository;

    public GAAccountRepository getGaAccountRepository() {
        return gaAccountRepository;
    }

    public void setGaAccountRepository(GAAccountRepository gaAccountRepository) {
        this.gaAccountRepository = gaAccountRepository;
    }

    @Autowired
    GAWebPropertyRepository gaWebPropertyRepository;

    public GAWebPropertyRepository getGaWebPropertyRepository() {
        return gaWebPropertyRepository;
    }

    public void setGaWebPropertyRepository(GAWebPropertyRepository gaWebPropertyRepository) {
        this.gaWebPropertyRepository = gaWebPropertyRepository;
    }

    @Autowired
    GAWebPropertyProfileRepository gaWebPropertyProfileRepository;

    public GAWebPropertyProfileRepository getGaWebPropertyProfileRepository() {
        return gaWebPropertyProfileRepository;
    }

    public void setGaWebPropertyProfileRepository(GAWebPropertyProfileRepository gaWebPropertyProfileRepository) {
        this.gaWebPropertyProfileRepository = gaWebPropertyProfileRepository;
    }

    @Autowired
    GAEventMetricsRepository gaEventMetricsRepository;

    public GAEventMetricsRepository getGaEventMetricsRepository() {
        return gaEventMetricsRepository;
    }

    public void setGaEventMetricsRepository(GAEventMetricsRepository gaEventMetricsRepository) {
        this.gaEventMetricsRepository = gaEventMetricsRepository;
    }

    @Autowired
    GAPageViewMetricsRepository gaPageViewMetricsRepository;

    public GAPageViewMetricsRepository getGaPageViewMetricsRepository() {
        return gaPageViewMetricsRepository;
    }

    public void setGaPageViewMetricsRepository(GAPageViewMetricsRepository gaPageViewMetricsRepository) {
        this.gaPageViewMetricsRepository = gaPageViewMetricsRepository;
    }

    @Autowired
    GAEventDimensionRepository gaEventDimensionRepository;

    public GAEventDimensionRepository getGaEventDimensionRepository() {
        return gaEventDimensionRepository;
    }

    public void setGaEventDimensionRepository(GAEventDimensionRepository gaEventDimensionRepository) {
        this.gaEventDimensionRepository = gaEventDimensionRepository;
    }

    @Autowired
    GAPageViewDimensionRepository gaPageViewDimensionRepository;

    public GAPageViewDimensionRepository getGaPageViewDimensionRepository() {
        return gaPageViewDimensionRepository;
    }

    public void setGaPageViewDimensionRepository(GAPageViewDimensionRepository gaPageViewDimensionRepository) {
        this.gaPageViewDimensionRepository = gaPageViewDimensionRepository;
    }

    @Autowired
    DataSchedulerBatchRepository dataSchedulerBatchRepository;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    @Autowired
    RetailerRepository retailerRepository;

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    @Autowired
    ConfigurationService configurationService;

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @Autowired
    DataImportLookupRepository dataImportLookupRepository;

    public DataImportLookupRepository getDataImportLookupRepository() {
        return dataImportLookupRepository;
    }

    public void setDataImportLookupRepository(DataImportLookupRepository dataImportLookupRepository) {
        this.dataImportLookupRepository = dataImportLookupRepository;
    }
}
