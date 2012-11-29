package com.dell.dw.managers;

import com.dell.dw.persistence.domain.DataSchedulerBatch;
import com.dell.dw.persistence.domain.DataSource;
import com.dell.dw.persistence.domain.Retailer;
import com.dell.dw.persistence.repository.DataSchedulerBatchRepository;
import com.dell.dw.persistence.repository.DataSourceRepository;
import com.dell.dw.persistence.repository.RetailerRepository;
import com.sourcen.core.ObjectNotFoundException;
import com.sourcen.core.config.ConfigurationService;
import com.sourcen.core.config.ConfigurationServiceImpl;
import com.sourcen.core.util.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.hibernate.NonUniqueObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ IDEA.
 * User: Rajashree
 * Date: 6/26/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class OrderDataSchedulerManagerImpl  implements OrderDataSchedulerManager, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(OrderDataSchedulerManagerImpl.class);


    @Override
    @Transactional
    public void updateDataSchedulerBatches() {
        DataSource ds = dataSourceRepository.get(DataSource.DSConstants.ORDER);
        Retailer exampleObj = new Retailer();
        exampleObj.setName(configurationService.getProperty("app.retailer.name","dell"));
        Retailer retailer = (Retailer) retailerRepository.getUniqueByExample(exampleObj);

        DataSchedulerBatch dataSchedulerBatch =null;
        dataSchedulerBatch = new DataSchedulerBatch(ds,retailer.getId(), new Date(), new Date(), 0L,0L,DataSchedulerBatch.EndPoint.ORDERS,DataSchedulerBatch.Priority.UNKNOWN, DataSchedulerBatch.Status.IN_QUEUE,retailer,null, "/order/orders") ;
        if(dataSchedulerBatch != null){
            dataSchedulerBatchRepository.insert(dataSchedulerBatch);
        }
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSchedulerBatchRepository dataSchedulerBatchRepository;

    @Autowired
    DataSourceRepository dataSourceRepository;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private RetailerRepository retailerRepository;

    public DataSchedulerBatchRepository getDataSchedulerBatchRepository() {
        return dataSchedulerBatchRepository;
    }

    public void setDataSchedulerBatchRepository(DataSchedulerBatchRepository dataSchedulerBatchRepository) {
        this.dataSchedulerBatchRepository = dataSchedulerBatchRepository;
    }

    public DataSourceRepository getDataSourceRepository() {
        return dataSourceRepository;
    }

    public void setDataSourceRepository(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public RetailerRepository getRetailerRepository() {
        return retailerRepository;
    }

    public void setRetailerRepository(RetailerRepository retailerRepository) {
        this.retailerRepository = retailerRepository;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}


