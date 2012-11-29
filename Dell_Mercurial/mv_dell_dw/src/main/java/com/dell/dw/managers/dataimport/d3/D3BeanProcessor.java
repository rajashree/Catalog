package com.dell.dw.managers.dataimport.d3;

import com.dell.dw.persistence.domain.*;
import com.dell.dw.persistence.repository.CampaignRepository;
import com.dell.dw.persistence.repository.D3LinkRepository;
import com.dell.dw.persistence.repository.D3LinkTrackerMetricsRepository;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.util.WebUtils;
import com.sourcen.dataimport.service.support.hibernate.BeanProcessorAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 5/30/12
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class D3BeanProcessor extends BeanProcessorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(D3BeanProcessor.class);
    @Override
    public boolean supportsBean(final Class clazz) {
        return (D3LinkTrackerMetrics.class.equals(clazz) || (D3RevenueMetrics.class.equals(clazz)));
    }

    @Override
    public Object preProcessBeforePersist(final Object bean, Map<String, Object> row) {
        Retailer exampleObj = new Retailer();
        exampleObj.setName(configurationService.getProperty("app.retailer.name","dell"));
        Retailer retailer = (Retailer) retailerRepository.getUniqueByExample(exampleObj);

        Object d3LinkTrackerMetrics =  bean;
        try {
            Long linkId = (Long)row.get("linkId");
            String campaignId = row.get("campaignId").toString();
            String description = row.get("description").toString();
            Campaign campaign = null;
            if(!WebUtils.isNullOrEmpty("campaignId"))
                campaign= (Campaign) campaignRepository.get(Long.parseLong(campaignId));
            D3Link d3Link = d3LinkRepository.get(linkId);

            if(d3Link == null){
                d3Link = new D3Link(linkId,campaign,description,retailer);
                d3LinkRepository.insert(d3Link);
            }
            if(bean.getClass().equals(D3LinkTrackerMetrics.class))
                ((D3LinkTrackerMetrics)d3LinkTrackerMetrics).setD3Link(d3Link);
            else if(bean.getClass().equals(D3RevenueMetrics.class))
                ((D3RevenueMetrics)d3LinkTrackerMetrics).setD3Link(d3Link);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }

        return d3LinkTrackerMetrics;
    }

    @Autowired
    D3LinkRepository d3LinkRepository;

    @Autowired
    D3LinkTrackerMetricsRepository d3LinkTrackerMetricsRepository;

    @Autowired
    RetailerRepository retailerRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    ConfigurationService configurationService;

    public D3LinkTrackerMetricsRepository getD3LinkTrackerMetricsRepository() {
        return d3LinkTrackerMetricsRepository;
    }

    public void setD3LinkTrackerMetricsRepository(D3LinkTrackerMetricsRepository d3LinkTrackerMetricsRepository) {
        this.d3LinkTrackerMetricsRepository = d3LinkTrackerMetricsRepository;
    }

    public D3LinkRepository getD3LinkRepository() {
        return d3LinkRepository;
    }

    public void setD3LinkRepository(D3LinkRepository d3LinkRepository) {
        this.d3LinkRepository = d3LinkRepository;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public CampaignRepository getCampaignRepository() {
        return campaignRepository;
    }

    public void setCampaignRepository(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }
}
